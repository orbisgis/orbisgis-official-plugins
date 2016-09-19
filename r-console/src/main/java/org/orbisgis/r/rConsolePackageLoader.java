package org.orbisgis.r;

import org.orbisgis.frameworkapi.CoreWorkspace;
import org.renjin.primitives.packaging.ClasspathPackage;
import org.renjin.primitives.packaging.FqPackageName;
import org.renjin.primitives.packaging.Package;
import org.renjin.primitives.packaging.PackageLoader;
import org.renjin.repackaged.guava.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Package loader for the R console
 *
 * @author Sylvain PALOMINOS
 */
public class rConsolePackageLoader implements PackageLoader{
    private static final I18n I18N = I18nFactory.getI18n(rConsolePackageLoader.class);
    protected static Logger LOGGER = LoggerFactory.getLogger(rConsolePackageLoader.class);
    private static final Class[] parameters = new Class[]{URL.class};
    private URLClassLoader sysloader;
    private final static String baseNexusUrl = "https://nexus.bedatadriven.com/content/groups/public/";
    private CoreWorkspace coreWorkspace;

    public rConsolePackageLoader(CoreWorkspace coreWorkspace){
        this.sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        this.coreWorkspace = coreWorkspace;
    }

    public File downloadDependencyFromNexus(FqPackageName fqPackageName){
        File renjinDirectory = new File(coreWorkspace.getApplicationFolder() + "/renjin/");
        renjinDirectory.mkdirs();
        if(!renjinDirectory.exists()) {
            LOGGER.error(I18N.tr("Unable to create the '" + renjinDirectory.getAbsolutePath() + "' directory."));
            return null;
        }

        try {
            String packagePath = fqPackageName.getGroupId().replace('.','/') + "/" + fqPackageName.getPackageName() + "/";
            String pomJarPath = renjinDirectory + "/" + packagePath;
            String urlStr = baseNexusUrl + packagePath;
            URL url = new URL(urlStr + "maven-metadata.xml");
            String latestVersion = null;

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();

            try {
                if (huc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(url.openStream());
                    NodeList nList = doc.getElementsByTagName("latest");
                    if (nList.getLength() == 1) {
                        Node node = nList.item(0);
                        if (node != null) {
                            latestVersion = node.getTextContent();
                        }
                    }
                }
            } catch (UnknownHostException e){
                LOGGER.warn(I18N.tr("The host '" + baseNexusUrl +"' is not reachable. Working with local packages."));
                File localJarFile = new File(pomJarPath);
                File jarFile = null;
                if(localJarFile.exists() && localJarFile.isDirectory()) {
                    for (File file : localJarFile.listFiles()) {
                        if (file.getName().endsWith(".jar")) {
                            jarFile = file;
                            break;
                        }
                    }
                }
                return jarFile;
            }

            //If can't contact distant nexus, try the local packages to find a jar
            if(latestVersion == null || latestVersion.isEmpty()){
                return null;
            }

            String versionExt = latestVersion;
            //if the package is a renjin one (like utils) and it is a snapshot, get the build number.
            if(fqPackageName.getGroupId().equals("org.renjin") && latestVersion.contains("SNAPSHOT")){
                URL metaUrl = new URL(baseNexusUrl + packagePath + latestVersion + "/maven-metadata.xml");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(metaUrl.openStream());
                String timestamp = doc.getElementsByTagName("timestamp").item(0).getTextContent();
                String buildNumber = doc.getElementsByTagName("buildNumber").item(0).getTextContent();
                versionExt = versionExt.replace("SNAPSHOT", "");
                versionExt += timestamp + "-" + buildNumber;
            }

            //Files path
            String jarName = fqPackageName.getPackageName() + "-" + versionExt + ".jar";
            String pomName = fqPackageName.getPackageName() + "-" + versionExt + ".pom";

            //Test if there is a local copy of the package
            File localJarFile = new File(pomJarPath + "/" + jarName);
            if(localJarFile.exists()){
                return localJarFile;
            }

            LOGGER.warn(I18N.tr("Downloading the R package : " + fqPackageName.getGroupId() + ":" + fqPackageName.getPackageName()));

            //copy jar
            URL jarUrl = new URL(urlStr + latestVersion + "/" + jarName);
            ReadableByteChannel rbc = Channels.newChannel(jarUrl.openStream());
            File jarFile = new File(pomJarPath, jarName);
            jarFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(jarFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            //copy pom
            URL pomUrl = new URL(urlStr + latestVersion + "/" + pomName);
            rbc = Channels.newChannel(pomUrl.openStream());
            fos = new FileOutputStream(new File(pomJarPath, pomName));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return jarFile;
        } catch (MalformedURLException e) {
            LOGGER.error(I18N.tr("The files URL is malformed.", e));
        } catch (ParserConfigurationException e) {
            LOGGER.error(I18N.tr("Error on building parsing object.", e));
        } catch (SAXException e) {
            LOGGER.error(I18N.tr("Error on parsing the maven-metadata.xml files.", e));
        } catch (IOException e) {
            LOGGER.error(I18N.tr("Error on opening the streams.", e));
        }
        return null;
    }

    @Override
    public Optional<Package> load(FqPackageName fqPackageName) {
        //Test if the package has already been loaded
        ClasspathPackage pkg = new ClasspathPackage(sysloader, fqPackageName);
        if(!pkg.resourceExists("environment")) {
            //Get the jar file
            final File file = downloadDependencyFromNexus(fqPackageName);
            if (file != null) {
                //Adds the jar to the classPath
                LOGGER.warn(I18N.tr("Loading the R package : " + fqPackageName.getGroupId() + ":" + fqPackageName.getPackageName()));
                Class sysclass = URLClassLoader.class;
                try {
                    Method method = sysclass.getDeclaredMethod("addURL", parameters);
                    method.setAccessible(true);
                    method.invoke(sysloader, new Object[]{file.toURL()});
                } catch (Throwable t) {
                    LOGGER.error(I18N.tr("Error, could not add URL to system classloader", t));
                }
            }
            //If the package is in the class path, return it, else return an absent
            if(pkg.resourceExists("environment")) {
                return Optional.<Package>of(pkg);
            }
            else{
                return Optional.absent();
            }
        }
        else{
            return Optional.<Package>of(pkg);
        }
    }
}