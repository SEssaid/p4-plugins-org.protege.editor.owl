package org.protege.editor.owl.ui.frame.editor;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRowObjectEditor;
import org.protege.editor.owl.ui.selector.OWLDataPropertySelectorPanel;
import org.semanticweb.owl.model.OWLDataProperty;

import javax.swing.*;
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
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 6, 2009<br><br>
 */
public class OWLDataPropertySetEditor extends AbstractOWLFrameSectionRowObjectEditor<Set<OWLDataProperty>> {

    private OWLDataPropertySelectorPanel editor;


    public OWLDataPropertySetEditor(OWLEditorKit owlEditorKit) {
        editor = new OWLDataPropertySelectorPanel(owlEditorKit);
    }


    public Set<OWLDataProperty> getEditedObject() {
        return editor.getSelectedObjects();
    }

    public void setEditedObject(Set<OWLDataProperty> p){
        editor.setSelection(p);
    }


    public JComponent getEditorComponent() {
        return editor;
    }


    public void clear() {
        editor.setSelection((OWLDataProperty)null);
    }


    public void dispose() {
        editor.dispose();
    }
}
