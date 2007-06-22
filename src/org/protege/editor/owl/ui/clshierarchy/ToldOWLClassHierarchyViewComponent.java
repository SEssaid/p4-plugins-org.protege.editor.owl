package org.protege.editor.owl.ui.clshierarchy;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.tree.OWLTreeDragAndDropHandler;
import org.protege.editor.owl.ui.view.CreateNewChildTarget;
import org.protege.editor.owl.ui.view.CreateNewSiblingTarget;
import org.protege.editor.owl.ui.view.CreateNewTarget;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntitySetProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
 * Medical Informatics Group<br>
 * Date: Mar 21, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * A view that contains a JTree that displays a
 * hierarchy of OWLClasses. The class hierarchy
 * is derived from told information.
 */
public class ToldOWLClassHierarchyViewComponent extends AbstractOWLClassHierarchyViewComponent implements CreateNewTarget, CreateNewChildTarget, CreateNewSiblingTarget {

    public void performExtraInitialisation() throws Exception {
        // Add in the manipulation actions - we won't need to keep track
        // of these, as this will be done by the view - i.e. we won't
        // need to dispose of these actions.
        addAction(new AddSubClassAction(getOWLEditorKit(), getTree()), "A", "A");
        addAction(new AddSiblingClassAction(getOWLEditorKit(), getTree()), "A", "B");
        addAction(new DeleteClassAction(getOWLEditorKit(), new OWLEntitySetProvider<OWLClass>() {
            public Set<OWLClass> getEntities() {
                return new HashSet<OWLClass>(getTree().getSelectedOWLObjects());
            }
        }), "B", "A");

        getTree().setDragAndDropHandler(new OWLTreeDragAndDropHandler<OWLClass>() {
            public void move(OWLClass child, OWLClass fromParent, OWLClass toParent) {
                if (child.equals(getOWLModelManager().getOWLDataFactory().getOWLThing())) {
                    return;
                }
                List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
                                         getOWLModelManager().getOWLDataFactory().getOWLSubClassAxiom(child,
                                                                                                      toParent)));
                changes.add(new RemoveAxiom(getOWLModelManager().getActiveOntology(),
                                            getOWLModelManager().getOWLDataFactory().getOWLSubClassAxiom(child,
                                                                                                         fromParent)));
                getOWLModelManager().applyChanges(changes);
            }


            public void add(OWLClass child, OWLClass parent) {
                if (child.equals(getOWLModelManager().getOWLDataFactory().getOWLThing())) {
                    return;
                }
                List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
                                         getOWLModelManager().getOWLDataFactory().getOWLSubClassAxiom(child, parent)));
                getOWLModelManager().applyChanges(changes);
            }
        });
    }


    protected OWLObjectHierarchyProvider<OWLClass> getOWLClassHierarchyProvider() {
        return getOWLModelManager().getOWLClassHierarchyProvider();
    }


    public void setSelectedClass(OWLClass cls) {
        getTree().setSelectedOWLObject(cls);
    }


    public OWLClass getSelectedClass() {
        return getTree().getSelectedOWLObject();
    }


    public Set<OWLDescription> getSelectedClasses() {
        return new HashSet<OWLDescription>(getTree().getSelectedOWLObjects());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Create new target
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean canCreateNew() {
        return true;
    }


    public boolean canCreateNewChild() {
        return !getSelectedClasses().isEmpty();
    }


    public boolean canCreateNewSibling() {
        return !getSelectedClasses().isEmpty();
    }


    public void createNewChild() {
        OWLEntityCreationSet<OWLClass> set = getOWLWorkspace().createOWLClass();
        OWLClass newClass = set.getOWLEntity();
        OWLClass selectedClass = getSelectedClass();
        OWLSubClassAxiom ax = getOWLDataFactory().getOWLSubClassAxiom(newClass, selectedClass);
        getOWLModelManager().applyChange(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
        getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(newClass);
    }


    public void createNewObject() {
        OWLEntityCreationSet<OWLClass> set = getOWLWorkspace().createOWLClass();
        OWLClass newClass = set.getOWLEntity();
        OWLClass selectedClass = getOWLDataFactory().getOWLThing();
        OWLSubClassAxiom ax = getOWLDataFactory().getOWLSubClassAxiom(newClass, selectedClass);
        getOWLModelManager().applyChange(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
        getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(newClass);
    }


    public void createNewSibling() {
        OWLClass cls = getTree().getSelectedOWLObject();
        if (cls == null) {
            // Shouldn't really get here, because the
            // action should be disabled
            return;
        }
        // We need to apply the changes in the active ontology
        OWLEntityCreationSet<OWLClass> creationSet = getOWLWorkspace().createOWLClass();
        if (creationSet == null) {
            return;
        }
        // Combine the changes that are required to create the OWLClass, with the
        // changes that are required to make it a sibling class.
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.addAll(creationSet.getOntologyChanges());
        OWLModelManager owlModelManager = getOWLModelManager();
        for (OWLClass par : owlModelManager.getOWLClassHierarchyProvider().getParents(cls)) {
            OWLDataFactory df = owlModelManager.getOWLDataFactory();
            OWLAxiom ax = df.getOWLSubClassAxiom(creationSet.getOWLEntity(), par);
            changes.add(new AddAxiom(owlModelManager.getActiveOntology(), ax));
        }
        owlModelManager.applyChanges(changes);
        // Select the new class
        getTree().setSelectedOWLObject(creationSet.getOWLEntity());
    }
}
