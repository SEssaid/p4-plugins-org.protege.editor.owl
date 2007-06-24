package org.protege.editor.owl.ui.frame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLRuntimeException;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLObjectPropertyRangeFrameSection extends AbstractOWLFrameSection<OWLObjectProperty, OWLObjectPropertyRangeAxiom, OWLDescription> {

    public static final String LABEL = "Ranges (intersection)";

    Set<OWLDescription> addedRanges = new HashSet<OWLDescription>();


    public OWLObjectPropertyRangeFrameSection(OWLEditorKit owlEditorKit, OWLFrame<? extends OWLObjectProperty> frame) {
        super(owlEditorKit, LABEL, frame);
    }


    protected void clear() {
        addedRanges.clear();
    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {

        for (OWLObjectPropertyRangeAxiom ax : ontology.getObjectPropertyRangeAxioms(getRootObject())) {
            addRow(new OWLObjectPropertyRangeFrameSectionRow(getOWLEditorKit(), this, ontology, getRootObject(), ax));
            addedRanges.add(ax.getRange());
        }
    }


    protected void refillInferred() {
        try {
            // Inferred stuff
            for (OWLDescription inferredRange : getOWLModelManager().getReasoner().getRanges(getRootObject())) {
                if (!addedRanges.contains(inferredRange)) {
                    OWLObjectPropertyRangeAxiom inferredAxiom = getOWLDataFactory().getOWLObjectPropertyRangeAxiom(
                            getRootObject(),
                            inferredRange);
                    addRow(new OWLObjectPropertyRangeFrameSectionRow(getOWLEditorKit(),
                                                                     this,
                                                                     null,
                                                                     getRootObject(),
                                                                     inferredAxiom));
                }
                addedRanges.add(inferredRange);
            }
        }
        catch (OWLReasonerException e) {
            throw new OWLRuntimeException(e);
        }
    }


    protected OWLObjectPropertyRangeAxiom createAxiom(OWLDescription object) {
        return getOWLDataFactory().getOWLObjectPropertyRangeAxiom(getRootObject(), object);
    }


    public OWLFrameSectionRowObjectEditor<OWLDescription> getObjectEditor() {
        return new OWLClassDescriptionEditor(getOWLEditorKit(), null);
    }


    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        if (axiom.getProperty().equals(getRootObject())) {
            reset();
        }
    }


    public boolean canAcceptDrop(List<OWLObject> objects) {
        for (OWLObject obj : objects) {
            if (!(obj instanceof OWLDescription)) {
                return false;
            }
        }
        return true;
    }


    public boolean dropObjects(List<OWLObject> objects) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLObject obj : objects) {
            if (obj instanceof OWLDescription) {
                OWLDescription desc = (OWLDescription) obj;
                OWLAxiom ax = getOWLDataFactory().getOWLObjectPropertyRangeAxiom(getRootObject(), desc);
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
            }
            else {
                return false;
            }
        }
        getOWLModelManager().applyChanges(changes);
        return true;
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */
    public Comparator<OWLFrameSectionRow<OWLObjectProperty, OWLObjectPropertyRangeAxiom, OWLDescription>> getRowComparator() {
        return null;
    }
}
