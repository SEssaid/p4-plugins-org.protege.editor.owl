package org.protege.editor.owl.ui.frame.individual;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.ReasonerPreferences.OptionalInferenceTask;
import org.protege.editor.owl.ui.editor.OWLIndividualSetEditor;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLSameIndividualsAxiomFrameSection extends AbstractOWLFrameSection<OWLNamedIndividual, OWLSameIndividualAxiom, Set<OWLNamedIndividual>> {

    public static final String LABEL = "Same individuals";


    public OWLSameIndividualsAxiomFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLNamedIndividual> frame) {
        super(editorKit, LABEL, LABEL, frame);
    }


    protected void clear() {
    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        for (OWLSameIndividualAxiom ax : ontology.getSameIndividualAxioms(getRootObject())) {
            addRow(new OWLSameIndividualsAxiomFrameSectionRow(getOWLEditorKit(), this, ontology, getRootObject(), ax));
        }
    }
    
    @Override
    protected void refillInferred() {
    	getOWLModelManager().getReasonerPreferences().executeTask(OptionalInferenceTask.SHOW_INFERRED_SAMEAS_INDIVIDUAL_ASSERTIONS, new Runnable() {
    		public void run() {
    			Set<OWLIndividual> existingSameIndividuals = getCurrentlyDisplayedSameIndividuals();
    			Set<OWLNamedIndividual> newSameIndividuals = new HashSet<OWLNamedIndividual>();
    			for (OWLNamedIndividual i : getCurrentReasoner().getSameIndividuals(getRootObject()).getEntities()) {
    				if (!i.equals(getRootObject()) && !existingSameIndividuals.contains(i)) {
    					newSameIndividuals.add(i);    					
    				}
    			}
    			if (!newSameIndividuals.isEmpty()) {
    				newSameIndividuals.add(getRootObject());
    				addRow(new OWLSameIndividualsAxiomFrameSectionRow(getOWLEditorKit(), 
    						OWLSameIndividualsAxiomFrameSection.this, 
    						null, 
    						getRootObject(),
    						getOWLDataFactory().getOWLSameIndividualAxiom(newSameIndividuals)
    				));
    			}
    		}
    	});
    }
    
    public Set<OWLIndividual> getCurrentlyDisplayedSameIndividuals() {
		Set<OWLIndividual> existingSameIndividuals = new HashSet<OWLIndividual>();
		for (OWLFrameSectionRow<OWLNamedIndividual, OWLSameIndividualAxiom, Set<OWLNamedIndividual>> existingRow : getRows()) {
			OWLSameIndividualAxiom existingAxiom = existingRow.getAxiom();
			for (OWLIndividual existingSameIndividual : existingAxiom.getIndividuals()) {
				existingSameIndividuals.add(existingSameIndividual);
			}
		}
		return existingSameIndividuals;
    }


    public void visit(OWLSameIndividualAxiom axiom) {
        if (axiom.getIndividuals().contains(getRootObject())) {
            reset();
        }
    }


    protected OWLSameIndividualAxiom createAxiom(Set<OWLNamedIndividual> object) {
        object.add(getRootObject());
        OWLSameIndividualAxiom ax = getOWLDataFactory().getOWLSameIndividualAxiom(object);
        return ax;
    }


    public OWLObjectEditor<Set<OWLNamedIndividual>> getObjectEditor() {
        return new OWLIndividualSetEditor(getOWLEditorKit());
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */
    public Comparator<OWLFrameSectionRow<OWLNamedIndividual, OWLSameIndividualAxiom, Set<OWLNamedIndividual>>> getRowComparator() {
        return null;
    }
}
