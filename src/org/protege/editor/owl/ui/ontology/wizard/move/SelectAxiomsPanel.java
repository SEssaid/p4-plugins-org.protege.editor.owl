package org.protege.editor.owl.ui.ontology.wizard.move;

import org.protege.editor.core.ui.util.CheckTable;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.ontology.wizard.merge.SelectTargetOntologyPage;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.OWLAxiom;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;/*
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
 * Date: May 30, 2008<br><br>
 */
public class SelectAxiomsPanel extends AbstractMoveAxiomsWizardPanel {

    public static String ID = "AxiomSelectionPanel";

    private CheckTable<OWLAxiom> list;


    public SelectAxiomsPanel(OWLEditorKit eKit) {
        super(ID, "Confirm axioms to move", eKit);
    }


    protected void createUI(JComponent parent) {
        parent.setLayout(new BorderLayout());

        list = new CheckTable<OWLAxiom>("Axioms");
        final OWLCellRenderer owlCellRenderer = new OWLCellRenderer(getOWLEditorKit());
        owlCellRenderer.setHighlightKeywords(true);
        owlCellRenderer.setTransparent();
        list.setDefaultRenderer(owlCellRenderer);

        final JScrollPane scroller = new JScrollPane(list);
        parent.add(scroller);
    }

    
    public void aboutToDisplayPanel() {
//        list.setData(new ArrayList<OWLAxiom>(getUnfilteredAxioms()));
        list.checkAll(true);        
    }


    public Object getBackPanelDescriptor() {
        return getWizard().getLastPanelIDForKit();
    }


    public Object getNextPanelDescriptor() {
        return SelectTargetOntologyPage.ID;
    }


    private Set<? extends OWLAxiom> getUnfilteredAxioms() {
        return getWizard().getAxiomsToBeMoved();
    }


    public java.util.List<OWLAxiom> getFilteredAxioms() {
        return list.getFilteredValues();
    }
}
