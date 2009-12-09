package org.protege.editor.owl.ui.frame.objectproperty;

import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.property.AbstractPropertyDomainFrameSection;
import org.protege.editor.owl.ui.frame.property.AbstractPropertyDomainFrameSectionRow;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLObjectPropertyDomainFrameSection extends AbstractPropertyDomainFrameSection<OWLObjectProperty, OWLObjectPropertyDomainAxiom> {

    public OWLObjectPropertyDomainFrameSection(OWLEditorKit editorKit, OWLFrame<OWLObjectProperty> owlObjectPropertyOWLFrame) {
        super(editorKit, owlObjectPropertyOWLFrame);
    }


    protected OWLObjectPropertyDomainAxiom createAxiom(OWLClassExpression object) {
        return getOWLDataFactory().getOWLObjectPropertyDomainAxiom(getRootObject(), object);
    }


    protected AbstractPropertyDomainFrameSectionRow<OWLObjectProperty, OWLObjectPropertyDomainAxiom> createFrameSectionRow(OWLObjectPropertyDomainAxiom domainAxiom, OWLOntology ontology) {
        return new OWLObjectPropertyDomainFrameSectionRow(getOWLEditorKit(), this, ontology, getRootObject(), domainAxiom);
    }


    protected Set<OWLObjectPropertyDomainAxiom> getAxioms(OWLOntology ontology) {
        return ontology.getObjectPropertyDomainAxioms(getRootObject());
    }


    protected NodeSet<OWLClass> getInferredDomains() {
        return getOWLModelManager().getReasoner().getObjectPropertyDomains(getRootObject(), true);
    }


    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        if (axiom.getProperty().equals(getRootObject())) {
            reset();
        }
    }
}
