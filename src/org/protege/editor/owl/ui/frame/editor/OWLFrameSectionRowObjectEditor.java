package org.protege.editor.owl.ui.frame.editor;

import javax.swing.*;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 15-Feb-2007<br><br>
 */
public interface OWLFrameSectionRowObjectEditor<O> {

    void setHandler(OWLFrameSectionRowObjectEditorHandler<O> handler);


    OWLFrameSectionRowObjectEditorHandler<O> getHandler();


    JComponent getEditorComponent();


    O getEditedObject();


    Set<O> getEditedObjects();


    boolean isMultiEditSupported();

    
    void clear();


    void dispose();
}