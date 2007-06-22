package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.util.CollectionFactory;

import java.util.Comparator;
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
 * Date: 29-Jan-2007<br><br>
 */
public class OWLEquivalentObjectPropertiesAxiomFrameSection extends AbstractOWLFrameSection<OWLObjectProperty, OWLEquivalentObjectPropertiesAxiom, OWLObjectProperty> {

    public static final String LABEL = "Equivalent object properties";


    public OWLEquivalentObjectPropertiesAxiomFrameSection(OWLEditorKit editorKit,
                                                          OWLFrame<? extends OWLObjectProperty> frame) {
        super(editorKit, LABEL, frame);
    }


    protected void clear() {
    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        for (OWLEquivalentObjectPropertiesAxiom ax : ontology.getEquivalentObjectPropertiesAxioms(getRootObject())) {
            addRow(new OWLEquivalentObjectPropertiesAxiomFrameSectionRow(getOWLEditorKit(),
                                                                         this,
                                                                         ontology,
                                                                         getRootObject(),
                                                                         ax));
        }
    }


    protected OWLEquivalentObjectPropertiesAxiom createAxiom(OWLObjectProperty object) {
        return getOWLDataFactory().getOWLEquivalentObjectPropertiesAxiom(CollectionFactory.createSet(getRootObject(),
                                                                                                     object));
    }


    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        if (axiom.getProperties().contains(getRootObject())) {
            reset();
        }
    }


    public OWLFrameSectionRowObjectEditor<OWLObjectProperty> getObjectEditor() {
        return new OWLObjectPropertyEditor(getOWLEditorKit());
    }


    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        if (axiom.getProperties().contains(getRootObject())) {
            reset();
        }
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */
    public Comparator<OWLFrameSectionRow<OWLObjectProperty, OWLEquivalentObjectPropertiesAxiom, OWLObjectProperty>> getRowComparator() {
        return null;
    }
}
