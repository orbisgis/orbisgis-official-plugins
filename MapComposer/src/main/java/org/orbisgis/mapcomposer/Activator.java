package org.orbisgis.mapcomposer;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.utils.LinkToOrbisGIS;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
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
            UIController uic = new UIController();
            bc.registerService(MainFrameAction.class,uic.getMainWindow(),null);
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
