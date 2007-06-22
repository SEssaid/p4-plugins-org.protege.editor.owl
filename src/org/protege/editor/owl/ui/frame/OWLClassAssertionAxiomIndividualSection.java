package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;

import java.util.*;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 27-Jan-2007<br><br>
 */
public class OWLClassAssertionAxiomIndividualSection extends AbstractOWLFrameSection<OWLClass, OWLClassAssertionAxiom, OWLIndividual> {

    public static final String LABEL = "Instances";

    private Set<OWLIndividual> added = new HashSet<OWLIndividual>();


    public OWLClassAssertionAxiomIndividualSection(OWLEditorKit editorKit, OWLFrame<? extends OWLClass> frame) {
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
            addRow(new OWLClassAssertionAxiomIndividualSectionRow(getOWLEditorKit(),
                                                                  this,
                                                                  ontology,
                                                                  getRootObject(),
                                                                  ax));
            added.add(ax.getIndividual());
        }
    }


    protected void refillInferred() {
        try {
            for (OWLIndividual ind : getOWLModelManager().getReasoner().getIndividuals(getRootObject(), true)) {
                if (!added.contains(ind)) {
                    addRow(new OWLClassAssertionAxiomIndividualSectionRow(getOWLEditorKit(),
                                                                          this,
                                                                          null,
                                                                          getRootObject(),
                                                                          getOWLModelManager().getOWLDataFactory().getOWLClassAssertionAxiom(
                                                                                  ind,
                                                                                  getRootObject())));
                    added.add(ind);
                }
            }
        }
        catch (OWLReasonerException e) {
            throw new OWLRuntimeException(e);
        }
    }


    protected OWLClassAssertionAxiom createAxiom(OWLIndividual object) {
        return getOWLDataFactory().getOWLClassAssertionAxiom(object, getRootObject());
    }


    public OWLFrameSectionRowObjectEditor<OWLIndividual> getObjectEditor() {
        return new OWLIndividualEditor(getOWLEditorKit());
    }


    public boolean canAcceptDrop(List<OWLObject> objects) {
        for (OWLObject obj : objects) {
            if (!(obj instanceof OWLIndividual)) {
                return false;
            }
        }
        return true;
    }


    public boolean dropObjects(List<OWLObject> objects) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLObject obj : objects) {
            if (obj instanceof OWLIndividual) {
                OWLIndividual ind = (OWLIndividual) obj;
                OWLAxiom ax = getOWLDataFactory().getOWLClassAssertionAxiom(ind, getRootObject());
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
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
    public Comparator<OWLFrameSectionRow<OWLClass, OWLClassAssertionAxiom, OWLIndividual>> getRowComparator() {
        return new Comparator<OWLFrameSectionRow<OWLClass, OWLClassAssertionAxiom, OWLIndividual>>() {


            public int compare(OWLFrameSectionRow<OWLClass, OWLClassAssertionAxiom, OWLIndividual> o1,
                               OWLFrameSectionRow<OWLClass, OWLClassAssertionAxiom, OWLIndividual> o2) {
                return getOWLModelManager().getRendering(o1.getAxiom().getIndividual()).compareToIgnoreCase(
                        getOWLModelManager().getRendering(o2.getAxiom().getIndividual()));
            }
        };
    }


    public void visit(OWLClassAssertionAxiom axiom) {
        if (axiom.getDescription().equals(getRootObject())) {
            reset();
        }
    }
}
