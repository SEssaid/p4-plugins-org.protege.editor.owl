package org.protege.editor.owl.ui.action;

import java.awt.event.ActionEvent;

import org.protege.editor.owl.model.refactor.ontology.ConvertEntityURIsToIdentifierPattern;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 30-Aug-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ConvertEntityURIsToLabels extends ProtegeOWLAction {

    public void actionPerformed(ActionEvent e) {
        ConvertEntityURIsToIdentifierPattern converter = new ConvertEntityURIsToIdentifierPattern(getOWLModelManager(),
                                                                                                  getOWLModelManager().getActiveOntologies());
        converter.performConversion();
    }


    public void initialise() throws Exception {
    }


    public void dispose() {
    }
}
