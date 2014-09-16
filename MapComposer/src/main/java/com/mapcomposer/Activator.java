package com.mapcomposer;

import com.mapcomposer.model.utils.LinkToOrbisGIS;
import com.mapcomposer.view.ui.MainWindow;
import org.orbisgis.viewapi.main.frames.ext.MainFrameAction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Registers services provided by this plugin bundle.
 */
public class Activator implements BundleActivator {
        /**
         * Starting bundle, register services.
         * @param bc
         * @throws Exception
         */
        @Override
        public void start(BundleContext bc) throws Exception {
            bc.registerService(MainFrameAction.class,MainWindow.getInstance(),null);
            LinkToOrbisGIS.setBundleContext(bc);
        }

        /**
         * Called before the bundle is unloaded.
         * @param bc
         * @throws Exception
         */
        @Override
        public void stop(BundleContext bc) throws Exception {

        }
}
