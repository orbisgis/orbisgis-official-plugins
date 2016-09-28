/*
 * OrbisGIS is an open source GIS application dedicated to research for
 * all geographic information science.
 * 
 * OrbisGIS is distributed under GPL 3 license. It is developped by the "GIS"
 * team of the Lab-STICC laboratory <http://www.lab-sticc.fr/>.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 Lab-STICC CNRS, UMR 6285.
 * 
 * This file is part of OrbisGIS.
 * 
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.r_engine;

import org.renjin.aether.AetherPackageLoader;
import org.renjin.aether.ConsoleRepositoryListener;
import org.renjin.aether.ConsoleTransferListener;
import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.primitives.packaging.PackageLoader;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.io.OutputStreamWriter;

/**
 * Class managing and configuring the R engine get from Renjin.
 *
 * @author Erwan Bocher
 */
public class REngine {

    private static final I18n I18N = I18nFactory.getI18n(REngine.class);
    private static final Logger LOGGER = LoggerFactory.getLogger("gui." + REngine.class);
    private static final String[] CORE_PACKAGES = new String[]{"datasets", "graphics", "grDevices", "hamcrest",
            "methods", "splines", "stats", "stats4", "utils", "grid", "parallel", "tools", "tcltk", "compiler"};
    private RenjinScriptEngine engine = null;

    public REngine(){
        AetherPackageLoader aetherLoader = new AetherPackageLoader();
        aetherLoader.setTransferListener(new ConsoleTransferListener());
        aetherLoader.setRepositoryListener(new ConsoleRepositoryListener(System.out));
        Session session = new SessionBuilder()
                .bind(ClassLoader.class, aetherLoader.getClassLoader())
                .bind(PackageLoader.class, aetherLoader)
                .build();

        engine = new RenjinScriptEngineFactory().getScriptEngine(session);
        engine.getContext().setWriter(new OutputStreamWriter(new LoggingOutputStream(LOGGER, false)));
        engine.getContext().setErrorWriter(new OutputStreamWriter(new LoggingOutputStream(LOGGER, true)));

        for (String pkg : CORE_PACKAGES) {
            try {
                engine.eval("library(" + pkg + ")");
            } catch (Exception e) {
                LOGGER.warn(I18N.tr("Unable to load the library '" + pkg + "'.\nCause : " + e.getMessage()));
            }
        }
    }

    /**
     * Returns the R ScriptEngine
     * @return The R ScriptEngine
     */
    public RenjinScriptEngine getScriptEngine() {
        return engine;
    }
}
