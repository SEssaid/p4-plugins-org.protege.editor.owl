package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.selector.OWLIndividualSelectorPanel;
import org.semanticweb.owl.model.OWLIndividual;

import javax.swing.*;
import java.awt.*;
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
 * Date: 01-Aug-2007<br><br>
 */
public class OWLIndividualAnnotationValueEditor implements OWLAnnotationValueEditor {

    private OWLIndividualSelectorPanel individualSelectorPanel;


    public OWLIndividualAnnotationValueEditor(OWLEditorKit owlEditorKit) {
        individualSelectorPanel = new OWLIndividualSelectorPanel(owlEditorKit);
        individualSelectorPanel.setPreferredSize(new Dimension(400, 300));
    }


    public boolean canEdit(Object object) {
        return object instanceof OWLIndividual;
    }


    public Object getEditedObject() {
        return individualSelectorPanel.getSelectedIndividual();
    }


    public void setEditedObject(Object object) {
        if (object != null) {
            individualSelectorPanel.setSelectedIndividual((OWLIndividual) object);
        }
    }


    public String getEditorTypeName() {
        return "Individual";
    }


    public JComponent getComponent() {
        return individualSelectorPanel;
    }
}
