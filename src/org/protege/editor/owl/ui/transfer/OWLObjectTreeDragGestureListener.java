package org.protege.editor.owl.ui.transfer;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.tree.OWLObjectTree;
import org.semanticweb.owl.model.OWLObject;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
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
 * Medical Informatics Group<br>
 * Date: 04-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLObjectTreeDragGestureListener extends OWLObjectDragGestureListener {

    private OWLObjectTree tree;


    public OWLObjectTreeDragGestureListener(OWLEditorKit owlEditorKit, OWLObjectTree tree) {
        super(owlEditorKit, tree);
        this.tree = tree;
    }


    protected List<OWLObject> getSelectedObjects() {
        return tree.getSelectedOWLObjects();
    }


    protected JComponent getRendererComponent() {
        JComponent c = (JComponent) tree.getCellRenderer().getTreeCellRendererComponent(tree,
                                                                                        tree.getSelectionPath().getLastPathComponent(),
                                                                                        false,
                                                                                        true,
                                                                                        true,
                                                                                        0,
                                                                                        false);
        return c;
    }


    protected Dimension getRendererComponentSize() {
        Rectangle bounds = tree.getRowBounds(tree.getRowForPath(tree.getSelectionPath()));
        return bounds.getSize();
    }


    protected Point getImageOffset() {
        TreePath selPath = tree.getSelectionPath();
        Rectangle rowBounds = tree.getRowBounds(tree.getRowForPath(selPath));
        Point pt = tree.getMousePosition();
        return new Point(rowBounds.x - pt.x, rowBounds.y - pt.y);
    }
}
