package org.protege.editor.owl.ui.action;

import org.semanticweb.owl.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 21-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RenameClassAction extends AbstractRenameEntityAction {

    protected OWLEntity getEntity() {
        return null;//getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass();
    }

    protected boolean isSuitable() {
        return false;//getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass() != null;
    }
}
