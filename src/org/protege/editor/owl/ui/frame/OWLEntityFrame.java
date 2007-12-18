package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.EventType;
import org.semanticweb.owl.model.*;

import java.util.ArrayList;
import java.util.List;
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
 * Date: 11-Jul-2007<br><br>
 */
public class OWLEntityFrame extends AbstractOWLFrame implements OWLEntityVisitor {

    private OWLEditorKit editorKit;


    private List<OWLFrameSection> owlOntologyFrameSections;

    private List<OWLFrameSection> owlClassSections;

    private List<OWLFrameSection> owlObjectPropertySections;

    private List<OWLFrameSection> owlDataPropertySections;

    private List<OWLFrameSection> owlIndividualSections;


    public OWLEntityFrame(OWLEditorKit editorKit) {
        super(editorKit.getOWLModelManager().getOWLOntologyManager());
        this.editorKit = editorKit;

        owlOntologyFrameSections = new ArrayList<OWLFrameSection>();
        owlOntologyFrameSections.add(new OWLOntologyAnnotationAxiomFrameSection(editorKit, this));
        owlOntologyFrameSections.add(new OWLImportsDeclarationFrameSection(editorKit, this));
//        owlOntologyFrameSections.add(new OWLIndirectImportsFrameSection(editorKit, this));
        owlOntologyFrameSections.add(new InferredAxiomsFrameSection(editorKit, this));


        owlClassSections = new ArrayList<OWLFrameSection>();

        owlClassSections.add(new OWLAnnotationFrameSection(editorKit, this));
        owlClassSections.add(new OWLEquivalentClassesAxiomFrameSection(editorKit, this));
        owlClassSections.add(new OWLSubClassAxiomFrameSection(editorKit, this));
        owlClassSections.add(new OWLClassAssertionAxiomIndividualSection(editorKit, this));
        owlClassSections.add(new OWLDisjointClassesAxiomFrameSection(editorKit, this));

        owlObjectPropertySections = new ArrayList<OWLFrameSection>();

        owlObjectPropertySections.add(new OWLAnnotationFrameSection(editorKit, this));
        owlObjectPropertySections.add(new OWLObjectPropertyDomainFrameSection(editorKit, this));
        owlObjectPropertySections.add(new OWLObjectPropertyRangeFrameSection(editorKit, this));
        owlObjectPropertySections.add(new OWLEquivalentObjectPropertiesAxiomFrameSection(editorKit, this));
        owlObjectPropertySections.add(new OWLSubObjectPropertyAxiomSuperPropertyFrameSection(editorKit, this));
        owlObjectPropertySections.add(new OWLDisjointObjectPropertiesFrameSection(editorKit, this));
        owlObjectPropertySections.add(new OWLPropertyChainAxiomFrameSection(editorKit, this));


        owlDataPropertySections = new ArrayList<OWLFrameSection>();

        owlDataPropertySections.add(new OWLAnnotationFrameSection(editorKit, this));
        owlDataPropertySections.add(new OWLDataPropertyDomainFrameSection(editorKit, this));
        owlDataPropertySections.add(new OWLDataPropertyRangeFrameSection(editorKit, this));
        owlDataPropertySections.add(new OWLEquivalentDataPropertiesFrameSection(editorKit, this));
        owlDataPropertySections.add(new OWLSubDataPropertyAxiomSuperPropertyFrameSection(editorKit, this));
        owlDataPropertySections.add(new OWLDisjointDataPropertiesFrameSection(editorKit, this));

        owlIndividualSections = new ArrayList<OWLFrameSection>();

        owlIndividualSections.add(new OWLAnnotationFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLClassAssertionAxiomTypeFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLObjectPropertyAssertionAxiomFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLNegativeObjectPropertyAssertionFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLDataPropertyAssertionAxiomFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLNegativeDataPropertyAssertionFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLSameIndividualsAxiomFrameSection(editorKit, this));
        owlIndividualSections.add(new OWLDifferentIndividualsAxiomFrameSection(editorKit, this));

        setRootObject(editorKit.getOWLModelManager().getOWLDataFactory().getOWLThing());

        editorKit.getOWLModelManager().addListener(new OWLModelManagerListener() {

            public void handleChange(OWLModelManagerChangeEvent event) {
                if(event.isType(EventType.ONTOLOGY_CLASSIFIED)) {
                    refill();
                    setRootObject(OWLEntityFrame.this.editorKit.getOWLModelManager().getActiveOntology());
                }
            }
        });
    }


    public void setRootObject(OWLObject rootObject) {
        clearSections();
        if (rootObject != null) {
            if(rootObject instanceof OWLOntology) {
                setupSections(owlOntologyFrameSections);
            }
            else {
                ((OWLEntity) rootObject).accept(this);
            }

        }
        super.setRootObject(rootObject);
    }


    public void visit(OWLClass owlClass) {
        setupSections(owlClassSections);
    }


    public void visit(OWLDataProperty owlDataProperty) {
        setupSections(owlDataPropertySections);
    }


    public void visit(OWLDataType owlDataType) {
    }


    public void visit(OWLIndividual owlIndividual) {
        setupSections(owlIndividualSections);
    }


    public void visit(OWLObjectProperty owlObjectProperty) {
        setupSections(owlObjectPropertySections);
    }

    private void setupSections(List<OWLFrameSection> sections) {
        for(OWLFrameSection sec : sections) {
            addSection(sec);
        }
    }



    public void dispose() {
        super.dispose();
        disposeOfSections(owlClassSections);
        disposeOfSections(owlObjectPropertySections);
        disposeOfSections(owlDataPropertySections);
        disposeOfSections(owlIndividualSections);
    }


    private void disposeOfSections(List<OWLFrameSection> sections) {
        for (OWLFrameSection sec : sections) {
            sec.dispose();
        }
    }
}
