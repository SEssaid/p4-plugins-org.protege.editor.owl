package org.protege.editor.owl.ui.frame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.OWLObjectComparator;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLRuntimeException;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLClassAssertionAxiomTypeFrameSection extends AbstractOWLFrameSection<OWLIndividual, OWLClassAssertionAxiom, OWLDescription> {

    private static final Logger logger = Logger.getLogger(OWLClassAssertionAxiomTypeFrameSection.class);

    public static final String LABEL = "Types";

    private Set<OWLDescription> added = new HashSet<OWLDescription>();


    public OWLClassAssertionAxiomTypeFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLIndividual> frame) {
        super(editorKit, LABEL, frame);
    }


    protected void clear() {
        added.clear();
    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        for (OWLClassAssertionAxiom ax : ontology.getClassAssertionAxioms(getRootObject())) {
            addRow(new OWLClassAssertionAxiomTypeFrameSectionRow(getOWLEditorKit(),
                                                                 this,
                                                                 ontology,
                                                                 getRootObject(),
                                                                 ax));
            added.add(ax.getDescription());
        }
    }


    protected void refillInferred() {
        try {
            for (OWLClass inferredType : OWLReasonerAdapter.flattenSetOfSets(getOWLModelManager().getReasoner().getTypes(
                    getRootObject(),
                    true))) {
                if (!added.contains(inferredType)) {
                    OWLClassAssertionAxiom ax = getOWLDataFactory().getOWLClassAssertionAxiom(getRootObject(),
                                                                                              inferredType);
                    addRow(new OWLClassAssertionAxiomTypeFrameSectionRow(getOWLEditorKit(),
                                                                         this,
                                                                         null,
                                                                         getRootObject(),
                                                                         ax));
                    added.add(inferredType);
                }
            }
        }
        catch (OWLReasonerException e) {
            throw new OWLRuntimeException(e);
        }
    }


    protected OWLClassAssertionAxiom createAxiom(OWLDescription object) {
        return getOWLDataFactory().getOWLClassAssertionAxiom(getRootObject(), object);
    }


    public OWLFrameSectionRowObjectEditor<OWLDescription> getObjectEditor() {
        return new OWLClassDescriptionEditor(getOWLEditorKit(), null);
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
                OWLAxiom ax = getOWLDataFactory().getOWLClassAssertionAxiom(getRootObject(), desc);
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
    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLClassAssertionAxiom, OWLDescription>> getRowComparator() {
        return new Comparator<OWLFrameSectionRow<OWLIndividual, OWLClassAssertionAxiom, OWLDescription>>() {

            private OWLObjectComparator comparator = new OWLObjectComparator(getOWLModelManager());


            public int compare(OWLFrameSectionRow<OWLIndividual, OWLClassAssertionAxiom, OWLDescription> o1,
                               OWLFrameSectionRow<OWLIndividual, OWLClassAssertionAxiom, OWLDescription> o2) {
                return comparator.compare(o1.getAxiom().getDescription(), o2.getAxiom().getDescription());
            }
        };
    }


    public void visit(OWLClassAssertionAxiom axiom) {
        if (axiom.getIndividual().equals(getRootObject())) {
            reset();
        }
    }
}
