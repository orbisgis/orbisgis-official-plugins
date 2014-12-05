package org.orbisgis.mapcomposer.model.configurationattribute.interfaces;

import org.orbisgis.mapcomposer.controller.UIController;

/**
 * This interface for the ConfigurationAttribute allows to define a refresh method.
 * It will be called to refresh the value contained by the GraphicalElement and to verify if it still right.
 */
public interface RefreshCA {
    /**
     * Refresh function.
     * @param uic UIController of the application. It permits to have an access to all the information necessary to verify the ConfigurationAttribute value.
     */
    public void refresh(UIController uic);
}
