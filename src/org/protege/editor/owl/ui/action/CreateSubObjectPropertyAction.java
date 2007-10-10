package org.protege.editor.owl.ui.action;

import java.awt.event.ActionEvent;

import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 03-Feb-2007<br><br>
 */
public class CreateSubObjectPropertyAction extends SelectedOWLObjectPropertyAction {

    protected void initialiseAction() throws Exception {
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        OWLObjectProperty selectedProperty = getOWLWorkspace().getOWLSelectionModel().getLastSelectedObjectProperty();
        OWLObjectProperty subProperty = getOWLWorkspace().createOWLObjectProperty().getOWLEntity();
        OWLObjectSubPropertyAxiom ax = getOWLDataFactory().getOWLSubObjectPropertyAxiom(subProperty, selectedProperty);
        getOWLModelManager().applyChange(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
    }
}
