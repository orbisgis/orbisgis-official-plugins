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
package org.orbisgis.memory;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.orbisgis.viewapi.docking.DockingPanel;
import org.orbisgis.viewapi.docking.DockingPanelParameters;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Memory usage panel, auto-refresh with Timer.
 * @author Erwan Bocher
 * @author Nicolas Fortin
 */
public class MemoryPanel extends JPanel implements DockingPanel {
    private DockingPanelParameters dockingPanelParameters = new DockingPanelParameters();
    private static final int MEMORY_REFRESH_RATE = 500; // Refresh rate in ms
    private static final Color FREE_MEM_COLOR = new Color(32, 128, 32);
    private static final Color USE_MEM_COLOR = new Color(230, 0, 0);
    private final Timer refreshTimer;
    private I18n _ = I18nFactory.getI18n(MemoryPanel.class);

    public MemoryPanel() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MemoryPanel.this.repaint();
            }
        }, MEMORY_REFRESH_RATE, MEMORY_REFRESH_RATE);

        dockingPanelParameters.setName("MemoryPanel");
        dockingPanelParameters.setTitle(_.tr("Java Memory Usage"));
        dockingPanelParameters.setTitleIcon(new ImageIcon(MemoryPanel.class.getResource("utilities-system-monitor.png")));
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        refreshTimer.cancel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Runtime runtime = Runtime.getRuntime();
        final long maxMemory = runtime.maxMemory() / 1024;
        final long allocatedMemory = runtime.totalMemory() / 1024;
        final long freeMemory = runtime.freeMemory() / 1024;
        final long totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);
        final long memoryUsed = maxMemory - totalFreeMemory;

        final int pos = (int) (memoryUsed * getHeight() / maxMemory);
        g.setColor(FREE_MEM_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(USE_MEM_COLOR);
        g.fillRect(0, getHeight() - pos, getWidth(), pos);

        g.setColor(Color.yellow);
        g.drawString(maxMemory / 1024 + _.tr("MB"), 10, 20);
        g.drawString(memoryUsed / 1024 + _.tr("MB"), 10, getHeight() - 20);
    }

    @Override
    public DockingPanelParameters getDockingParameters() {
        return dockingPanelParameters;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
