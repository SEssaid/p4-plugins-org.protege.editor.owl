package org.protege.editor.owl.ui.frame.cls;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLFrameSection;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.*;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Feb-2007<br><br>
 */
public class InheritedAnonymousClassesFrameSectionRow extends AbstractOWLFrameSectionRow<OWLClass, OWLClassAxiom, OWLClassExpression> {

    public InheritedAnonymousClassesFrameSectionRow(OWLEditorKit owlEditorKit, OWLFrameSection section,
                                                    OWLOntology ontology, OWLClass rootObject, OWLClassAxiom axiom) {
        super(owlEditorKit, section, ontology, rootObject, axiom);
    }


    protected OWLObjectEditor<OWLClassExpression> getObjectEditor() {
        if (getAxiom() instanceof OWLSubClassOfAxiom) {
            OWLClassExpression superCls = ((OWLSubClassOfAxiom) getAxiom()).getSuperClass();
            return getOWLEditorKit().getWorkspace().getOWLComponentFactory().getOWLClassDescriptionEditor(superCls, AxiomType.SUBCLASS);
        }
        else {
            Set<OWLClassExpression> descs = new HashSet<OWLClassExpression>(((OWLEquivalentClassesAxiom) getAxiom()).getClassExpressions());
            descs.remove(getRootObject());
            return getOWLEditorKit().getWorkspace().getOWLComponentFactory().getOWLClassDescriptionEditor(descs.iterator().next(), AxiomType.EQUIVALENT_CLASSES);
        }
    }


    protected OWLClassAxiom createAxiom(OWLClassExpression editedObject) {
        if (getAxiom() instanceof OWLSubClassOfAxiom) {
            return getOWLDataFactory().getOWLSubClassOfAxiom(getRoot(), editedObject);
        }
        else {
            return getOWLDataFactory().getOWLEquivalentClassesAxiom(CollectionFactory.createSet(getRoot(),
                                                                                                editedObject));
        }
    }


    public List<? extends OWLObject> getManipulatableObjects() {
        if (getAxiom() instanceof OWLSubClassOfAxiom) {
            return Arrays.asList(((OWLSubClassOfAxiom) getAxiom()).getSuperClass());
        }
        else {
            Set<OWLClassExpression> descs = new HashSet<OWLClassExpression>(((OWLEquivalentClassesAxiom) getAxiom()).getClassExpressions());
            descs.remove(getRootObject());
            if (descs.isEmpty()){
                // in the weird case that something is asserted equiv to itself
                return Collections.singletonList(getRootObject());
            }
            return Arrays.asList(descs.iterator().next());
        }
    }


    public String getTooltip() {
        return "Inherited from " + getOWLModelManager().getRendering(getRootObject());
    }
}
