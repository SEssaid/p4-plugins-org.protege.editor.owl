package org.protege.editor.owl.ui.frame.cls;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.inference.OWLReasonerException;
import org.semanticweb.owlapi.inference.UnsupportedReasonerOperationException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.*;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 19-Jan-2007<br><br>
 */
public class OWLEquivalentClassesAxiomFrameSection extends AbstractOWLClassAxiomFrameSection<OWLEquivalentClassesAxiom, OWLClassExpression> {

    private static final String LABEL = "Equivalent classes";

    private Set<OWLClassExpression> added = new HashSet<OWLClassExpression>();

    private boolean inferredEquivalentClasses = true;


    public OWLEquivalentClassesAxiomFrameSection(OWLEditorKit editorKit, OWLFrame<OWLClass> frame) {
        super(editorKit, LABEL, "Equivalent class", frame);
    }


    protected void clear() {
        added.clear();
    }


    protected void addAxiom(OWLEquivalentClassesAxiom ax, OWLOntology ontology) {
        addRow(new OWLEquivalentClassesAxiomFrameSectionRow(getOWLEditorKit(),
                                                            this,
                                                            ontology,
                                                            getRootObject(),
                                                            ax));
        for (OWLClassExpression desc : ax.getClassExpressions()) {
            added.add(desc);
        }
    }


    protected Set<OWLEquivalentClassesAxiom> getClassAxioms(OWLClassExpression descr, OWLOntology ont) {
        if (!descr.isAnonymous()){
            return ont.getEquivalentClassesAxioms(descr.asOWLClass());
        }
        else{
            Set<OWLEquivalentClassesAxiom> axioms = new HashSet<OWLEquivalentClassesAxiom>();
            for (OWLAxiom ax : ont.getGeneralClassAxioms()){
                if (ax instanceof OWLEquivalentClassesAxiom &&
                    ((OWLEquivalentClassesAxiom)ax).getClassExpressions().contains(descr)){
                    axioms.add((OWLEquivalentClassesAxiom)ax);
                }
            }
            return axioms;
        }
    }


    protected void refillInferred() {
        if (!inferredEquivalentClasses) {
            return;
        }
        try {
            if (!getOWLModelManager().getReasoner().isSatisfiable(getRootObject())) {
                addRow(new OWLEquivalentClassesAxiomFrameSectionRow(getOWLEditorKit(),
                                                                    this,
                                                                    null,
                                                                    getRootObject(),
                                                                    getOWLDataFactory().getOWLEquivalentClassesAxiom(
                                                                            CollectionFactory.createSet(getRootObject(),
                                                                                                        getOWLModelManager().getOWLDataFactory().getOWLNothing()))));
            }
            else{
                for (OWLClassExpression cls : getOWLModelManager().getReasoner().getEquivalentClasses(getRootObject())) {
                    if (!added.contains(cls) && !cls.equals(getRootObject())) {
                        addRow(new OWLEquivalentClassesAxiomFrameSectionRow(getOWLEditorKit(),
                                                                            this,
                                                                            null,
                                                                            getRootObject(),
                                                                            getOWLDataFactory().getOWLEquivalentClassesAxiom(
                                                                                    CollectionFactory.createSet(
                                                                                            getRootObject(),
                                                                                            cls))));
                    }
                }
            }
        }
        catch (UnsupportedReasonerOperationException e) {
            inferredEquivalentClasses = false;
        }
        catch (OWLReasonerException e) {
            e.printStackTrace();
        }
    }


    public void visit(OWLEquivalentClassesAxiom axiom) {
        if (axiom.getClassExpressions().contains(getRootObject())) {
            reset();
        }
    }


    protected OWLEquivalentClassesAxiom createAxiom(OWLClassExpression object) {
        return getOWLDataFactory().getOWLEquivalentClassesAxiom(CollectionFactory.createSet(getRootObject(), object));
    }


    public OWLObjectEditor<OWLClassExpression> getObjectEditor() {
        return getOWLEditorKit().getWorkspace().getOWLComponentFactory().getOWLClassDescriptionEditor(null, AxiomType.EQUIVALENT_CLASSES);
    }


    public boolean canAcceptDrop(List<OWLObject> objects) {
        for (OWLObject obj : objects) {
            if (!(obj instanceof OWLClassExpression)) {
                return false;
            }
        }
        return true;
    }


    public boolean dropObjects(List<OWLObject> objects) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLObject obj : objects) {
            if (obj instanceof OWLClassExpression) {
                OWLClassExpression desc = (OWLClassExpression) obj;
                OWLAxiom ax = getOWLDataFactory().getOWLEquivalentClassesAxiom(CollectionFactory.createSet(getRootObject(),
                                                                                                           desc));
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
    public Comparator<OWLFrameSectionRow<OWLClassExpression, OWLEquivalentClassesAxiom, OWLClassExpression>> getRowComparator() {
        return null;
    }
}
