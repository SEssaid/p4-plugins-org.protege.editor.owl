package org.protege.editor.owl.ui.frame;

import java.util.Arrays;
import java.util.List;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLSubClassAxiom;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 19-Jan-2007<br><br>
 */
public class OWLSubClassAxiomFrameSectionRow extends AbstractOWLFrameSectionRow<OWLClass, OWLSubClassAxiom, OWLDescription> {

    public OWLSubClassAxiomFrameSectionRow(OWLEditorKit owlEditorKit, OWLFrameSection section, OWLOntology ontology,
                                           OWLClass rootObject, OWLSubClassAxiom axiom) {
        super(owlEditorKit, section, ontology, rootObject, axiom);
    }


    protected OWLFrameSectionRowObjectEditor<OWLDescription> getObjectEditor() {
        return new OWLClassDescriptionEditor(getOWLEditorKit(), getAxiom().getSuperClass());
    }


    protected OWLSubClassAxiom createAxiom(OWLDescription editedObject) {
        return getOWLDataFactory().getOWLSubClassAxiom(getRootObject(), editedObject);
    }


    /**
     * Gets a list of objects contained in this row.
     */
    public List getManipulatableObjects() {
        return Arrays.asList(getAxiom().getSuperClass());
    }
}
