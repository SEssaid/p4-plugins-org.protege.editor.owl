package org.protege.editor.owl.ui.renderer;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLOntology;

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
 * Date: 13-Sep-2007<br><br>
 */
public class OWLOntologyCellRenderer extends DefaultListCellRenderer {

    private OWLEditorKit editorKit;


    public OWLOntologyCellRenderer(OWLEditorKit editorKit) {
        this.editorKit = editorKit;
    }


    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String uriString = (((OWLOntology) value).getURI().toString());
        int lastSepIndex = uriString.lastIndexOf('/');
        StringBuilder sb = new StringBuilder();
        if (lastSepIndex != -1) {
            sb.append("<html><body>");
            sb.append(uriString.substring(lastSepIndex + 1, uriString.length()));
            sb.append("    ");
            sb.append("<font color=\"gray\">");
            sb.append(uriString);
            sb.append("</font>");
            sb.append("</body></html>");
        }
        else {
            sb.append(uriString);
        }
        label.setText(sb.toString());
        label.setIcon(editorKit.getOWLWorkspace().getOWLIconProvider().getIcon((OWLOntology) value));
        return label;
    }
}