package org.protege.editor.owl.ui.hierarchy.creation;

import org.protege.editor.core.ProtegeManager;
import org.protege.editor.core.ui.wizard.Wizard;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLClass;
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
 * Date: 17-Jul-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class CreateClassHierarchyWizard extends Wizard {

    private PickRootClassPanel pickRootClassPanel;

    private TabIndentedHierarchyPanel tabIndentedHierarchyPanel;

    private MakeSiblingClassesDisjointPanel makeSiblingClassesDisjointPanel;


    public CreateClassHierarchyWizard(OWLEditorKit owlEditorKit) {
        super(ProtegeManager.getInstance().getFrame(owlEditorKit.getWorkspace()));
        setTitle("Create Class Hierarchy");
        pickRootClassPanel = new PickRootClassPanel(owlEditorKit);
        registerWizardPanel(PickRootClassPanel.ID, pickRootClassPanel);
        tabIndentedHierarchyPanel = new TabIndentedHierarchyPanel(owlEditorKit);
        registerWizardPanel(TabIndentedHierarchyPanel.ID, tabIndentedHierarchyPanel);
        makeSiblingClassesDisjointPanel = new MakeSiblingClassesDisjointPanel(owlEditorKit);
        registerWizardPanel(MakeSiblingClassesDisjointPanel.ID, makeSiblingClassesDisjointPanel);
        setCurrentPanel(PickRootClassPanel.ID);
    }


    public OWLClass getRootClass() {
        return pickRootClassPanel.getRootClass();
    }


    public String getHierarchy() {
        return tabIndentedHierarchyPanel.getHierarchy();
    }


    public String getSuffix() {
        return tabIndentedHierarchyPanel.getSuffix();
    }


    public String getPrefix() {
        return tabIndentedHierarchyPanel.getPrefix();
    }


    public boolean isMakeSiblingClassesDisjoint() {
        return makeSiblingClassesDisjointPanel.isMakeSiblingClassesDisjoint();
    }


    public void dispose() {
        pickRootClassPanel.dispose();
        super.dispose();
    }
}
