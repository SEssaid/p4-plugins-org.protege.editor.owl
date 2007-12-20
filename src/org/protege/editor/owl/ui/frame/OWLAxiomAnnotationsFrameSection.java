package org.protege.editor.owl.ui.frame;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLOntology;
import org.protege.editor.owl.OWLEditorKit;

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
 * Date: 06-Dec-2007<br><br>
 */
public class OWLAxiomAnnotationsFrameSection extends AbstractOWLFrameSection<OWLAxiomAnnotationsRoot, OWLAxiomAnnotationAxiom, OWLAnnotation>{

    private static final String LABEL = "Annotations";

    private OWLAnnotationEditor editor;

    public OWLAxiomAnnotationsFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLAxiomAnnotationsRoot> owlFrame) {
        super(editorKit, LABEL, owlFrame);
        editor = new OWLAnnotationEditor(editorKit);
    }


    protected OWLAxiomAnnotationAxiom createAxiom(OWLAnnotation object) {
        OWLAxiom subject = getRootObject().getSubject();
        return getOWLDataFactory().getOWLAxiomAnnotationAxiom(subject, object);
    }


    public OWLFrameSectionRowObjectEditor<OWLAnnotation> getObjectEditor() {
        return editor;
    }


    public boolean canAddRows() {
        return getRootObject().getSubject() != null;
    }


    protected void refill(OWLOntology ontology) {
        for(OWLAxiom ax : getRootObject().getAxioms()) {
            for(OWLAxiomAnnotationAxiom annoAx : ax.getAnnotationAxioms(ontology)) {
                // Add row
                addRow(new OWLAxiomAnnotationsFrameSectionRow(getOWLEditorKit(),
                                                             this,
                                                             ontology,
                                                             getRootObject(),
                                                             annoAx));
            }
        }
    }


    protected void clear() {
    }


    public Comparator<OWLFrameSectionRow<OWLAxiomAnnotationsRoot, OWLAxiomAnnotationAxiom, OWLAnnotation>> getRowComparator() {
        return null;
    }


    public void visit(OWLAxiomAnnotationAxiom axiom) {
        if(getRootObject() != null && getRootObject().getAxioms().contains(axiom)) {
            reset();
        }
    }
}