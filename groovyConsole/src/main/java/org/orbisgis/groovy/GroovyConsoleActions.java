package org.orbisgis.groovy;


import org.orbisgis.sif.components.actions.ActionFactoryService;

/**
 * @author Nicolas Fortin
 */
public abstract class GroovyConsoleActions implements ActionFactoryService<GroovyConsolePanel> {
    // Action, MENU IDs
    public static final String A_EXECUTE = "M_EXECUTE";
    public static final String A_CLEAR = "M_CLEAR";
    public static final String A_OPEN = "M_OPEN";
    public static final String A_SAVE = "M_SAVE";
    public static final String A_SEARCH = "M_SEARCH";
    public static final String A_COMMENT = "M_COMMENT";
    public static final String A_BLOCKCOMMENT = "M_BLOCKCOMMENT";
}
