package org.protege.editor.owl.model.selection.axioms;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;

import java.util.HashSet;
import java.util.Set;

/**
 * User: nickdrummond
 * Date: May 22, 2008
 */
public class IndividualReferencingAxiomStrategy extends EntityReferencingAxiomsStrategy<OWLIndividual> {

    public String getName() {
        return "Axioms referencing a given individual (or individuals)";
    }

    public Set<OWLAxiom> getAxioms() {
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        for (OWLIndividual individual : getEntities()){
            for (OWLOntology ont : getOntologies()){
                axioms.addAll(ont.getReferencingAxioms(individual));
            }
        }
        return axioms;

    }

    public Class<OWLIndividual> getType() {
        return OWLIndividual.class;
    }
}
