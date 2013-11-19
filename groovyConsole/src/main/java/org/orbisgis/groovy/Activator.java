/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. 
 * 
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 * 
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
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
package org.orbisgis.groovy;

import org.apache.log4j.Logger;
import org.orbisgis.view.docking.DockingPanel;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Registers the groovy console.
 *
 * @author Erwan Bocher
 */
public class Activator implements BundleActivator {

    private static final Logger LOGGER = Logger.getLogger("gui." + Activator.class);
    private static final I18n I18N = I18nFactory.getI18n(Activator.class);

    /**
     * Starting bundle, register services.
     *
     * @param bc
     * @throws Exception
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        LOGGER.info(I18N.tr("Groovy console starting..."));
        bc.registerService(DockingPanel.class,
                new GroovyConsolePanel(),
                null);

    }

    /**
     * Called before the bundle is unloaded.
     *
     * @param bc
     * @throws Exception
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        LOGGER.info(I18N.tr("Groovy console stoping..."));
    }
}
