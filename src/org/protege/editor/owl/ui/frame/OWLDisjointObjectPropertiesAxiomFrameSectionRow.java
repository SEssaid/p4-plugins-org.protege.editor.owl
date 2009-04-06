package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.editor.OWLObjectPropertySetEditor;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLDisjointObjectPropertiesAxiomFrameSectionRow extends AbstractOWLFrameSectionRow<OWLObjectProperty, OWLDisjointObjectPropertiesAxiom, Set<OWLObjectProperty>> {

    private OWLFrameSection section;

    public OWLDisjointObjectPropertiesAxiomFrameSectionRow(OWLEditorKit owlEditorKit, OWLFrameSection section,
                                                           OWLOntology ontology, OWLObjectProperty rootObject,
                                                           OWLDisjointObjectPropertiesAxiom axiom) {
        super(owlEditorKit, section, ontology, rootObject, axiom);

        this.section = section;
    }


    protected OWLFrameSectionRowObjectEditor<Set<OWLObjectProperty>> getObjectEditor() {
        OWLObjectPropertySetEditor editor = (OWLObjectPropertySetEditor) section.getEditor();
        final Set<OWLObjectPropertyExpression> disjoints = getAxiom().getProperties();
        final Set<OWLObjectProperty> namedDisjoints = new HashSet<OWLObjectProperty>();
        for (OWLObjectPropertyExpression p : disjoints){
            if (!p.isAnonymous()){
                namedDisjoints.add(p.asOWLObjectProperty());
            }
        }
        namedDisjoints.remove(getRootObject());
        editor.setEditedObject(namedDisjoints);
        // @@TODO handle property expressions
        return editor;
    }


    protected OWLDisjointObjectPropertiesAxiom createAxiom(Set<OWLObjectProperty> editedObject) {
        Set<OWLObjectProperty> props = new HashSet<OWLObjectProperty>();
        props.add(getRootObject());
        props.addAll(editedObject);
        return getOWLDataFactory().getOWLDisjointObjectPropertiesAxiom(props);
    }


    /**
     * Gets a list of objects contained in this row.  These objects
     * could be placed on the clip board during a copy operation,
     * or navigated to etc.
     */
    public List getManipulatableObjects() {
        List<OWLObjectPropertyExpression> props = new ArrayList<OWLObjectPropertyExpression>(getAxiom().getProperties());
        props.remove(getRoot());
        return props;
    }
}

