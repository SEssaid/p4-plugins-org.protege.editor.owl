package org.protege.editor.owl.ui.view.annotationproperty;

import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.view.AbstractOWLEntityHierarchyViewComponent;
import org.protege.editor.owl.ui.view.CreateNewChildTarget;
import org.protege.editor.owl.ui.view.CreateNewSiblingTarget;
import org.protege.editor.owl.ui.view.OWLPropertyTreeDropHandler;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.action.AbstractDeleteEntityAction;
import org.protege.editor.owl.ui.action.AbstractOWLTreeAction;
import org.protege.editor.core.ui.view.DisposableAction;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntitySetProvider;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.awt.event.ActionEvent;
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
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 23, 2009<br><br>
 */
public class OWLAnnotationPropertyHierarchyViewComponent extends AbstractOWLEntityHierarchyViewComponent<OWLAnnotationProperty>
        implements CreateNewChildTarget, CreateNewSiblingTarget {

    protected void performExtraInitialisation() throws Exception {
        addAction(new AbstractOWLTreeAction<OWLAnnotationProperty>("Add sub property", OWLIcons.getIcon("property.annotation.addsub.png"),
                                                                   getTree().getSelectionModel()){
            public void actionPerformed(ActionEvent event) {
                createNewChild();
            }
            protected boolean canPerform(OWLAnnotationProperty prop) {
                return canCreateNewChild();
            }
        }, "A", "A");

        addAction(new AbstractOWLTreeAction<OWLAnnotationProperty>("Add sibling property", OWLIcons.getIcon("property.annotation.addsib.png"),
                                                                   getTree().getSelectionModel()){

            public void actionPerformed(ActionEvent event) {
                createNewSibling();
            }
            protected boolean canPerform(OWLAnnotationProperty cls) {
                return canCreateNewSibling();
            }
        }, "A", "B");

        addAction(new DeleteAnnotationPropertyAction(), "B", "A");

//        getTree().setDragAndDropHandler(new OWLPropertyTreeDropHandler<OWLAnnotationProperty>(getOWLModelManager()){
//            protected OWLAxiom getAxiom(OWLDataFactory df, OWLAnnotationProperty child, OWLAnnotationProperty parent) {
//                return df.getOWLSubAnnotationPropertyOfAxiom(child, parent);
//            }
//        });
    }


    protected OWLObjectHierarchyProvider<OWLAnnotationProperty> getHierarchyProvider() {
        return getOWLModelManager().getOWLHierarchyManager().getOWLAnnotationPropertyHierarchyProvider();
    }


    protected OWLObject updateView() {
        return updateView(getOWLWorkspace().getOWLSelectionModel().getLastSelectedAnnotationProperty());
    }


    public List<OWLAnnotationProperty> find(String match) {
        return new ArrayList<OWLAnnotationProperty>(getOWLModelManager().getEntityFinder().getMatchingOWLAnnotationProperties(match));
    }


    public boolean canCreateNewChild() {
        return getSelectedEntity() != null;
    }


    public void createNewChild() {
        OWLAnnotationProperty selProp = getSelectedEntity();
        if (selProp == null) {
            return;
        }
        OWLEntityCreationSet<OWLAnnotationProperty> set = getOWLWorkspace().createOWLAnnotationProperty();
        if (set != null) {
            java.util.List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
            changes.addAll(set.getOntologyChanges());
            OWLDataFactory df = getOWLModelManager().getOWLDataFactory();
            OWLAxiom ax = df.getOWLSubAnnotationPropertyOfAxiom(set.getOWLEntity(), selProp);
            changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
            getOWLModelManager().applyChanges(changes);
            setSelectedEntity(set.getOWLEntity());
        }
    }


    public boolean canCreateNewSibling() {
        return getSelectedEntity() != null;
    }


    public void createNewSibling() {
        OWLAnnotationProperty property = getTree().getSelectedOWLObject();
        if (property == null) {
            // Shouldn't really get here, because the
            // action should be disabled
            return;
        }
        // We need to apply the changes in the active ontology
        OWLEntityCreationSet<OWLAnnotationProperty> creationSet = getOWLWorkspace().createOWLAnnotationProperty();
        if (creationSet != null) {
            // Combine the changes that are required to create the OWLAnnotationProperty, with the
            // changes that are required to make it a sibling property.
            List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
            changes.addAll(creationSet.getOntologyChanges());
            OWLModelManager mngr = getOWLModelManager();
            OWLDataFactory df = mngr.getOWLDataFactory();
            for (OWLAnnotationProperty par : getHierarchyProvider().getParents(property)) {
                OWLAxiom ax = df.getOWLSubAnnotationPropertyOfAxiom(creationSet.getOWLEntity(), par);
                changes.add(new AddAxiom(mngr.getActiveOntology(), ax));
            }
            mngr.applyChanges(changes);
            setSelectedEntity(creationSet.getOWLEntity());
        }
    }


    public class DeleteAnnotationPropertyAction extends AbstractDeleteEntityAction<OWLAnnotationProperty> {


        public DeleteAnnotationPropertyAction() {
            super("Delete selected properties",
                  OWLIcons.getIcon("property.annotation.delete.png"),
                  getOWLEditorKit(),
                  getHierarchyProvider(),
                  new OWLEntitySetProvider<OWLAnnotationProperty>() {
                      public Set<OWLAnnotationProperty> getEntities() {
                          return new HashSet<OWLAnnotationProperty>(getTree().getSelectedOWLObjects());
                      }
                  });
        }


        protected String getPluralDescription() {
            return "properties";
        }
    }
}
