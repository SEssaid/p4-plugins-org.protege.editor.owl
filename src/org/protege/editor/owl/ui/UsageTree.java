package org.protege.editor.owl.ui;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.LinkedObjectComponent;
import org.protege.editor.owl.ui.renderer.LinkedObjectComponentMediator;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
 * Date: 21-Feb-2007<br><br>
 */
public class UsageTree extends JTree implements LinkedObjectComponent {

    private OWLEditorKit owlEditorKit;

    private OWLEntity entity;


    public UsageTree(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = owlEditorKit;
        setCellRenderer(new UsageTreeCellRenderer(owlEditorKit));
        setRowHeight(-1);
        LinkedObjectComponentMediator mediator = new LinkedObjectComponentMediator(owlEditorKit, this);
    }


    public void setOWLEntity(OWLEntity entity) {
        this.entity = entity;
        setModel(new UsageTreeModel(owlEditorKit, entity));
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
            if (i > 100) {
                break;
            }
        }
    }


    private class UsageTreeCellRenderer extends OWLCellRenderer {


        public UsageTreeCellRenderer(OWLEditorKit owlEditorKit) {
            super(owlEditorKit);
        }


        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            setFocusedEntity(entity);
            JComponent c = (JComponent) super.getTreeCellRendererComponent(tree,
                                                                           node.getUserObject(),
                                                                           sel,
                                                                           expanded,
                                                                           leaf,
                                                                           row,
                                                                           hasFocus);
            if (node.getUserObject() instanceof OWLAxiom) {
                if (node.getParent().getIndex(node) == node.getParent().getChildCount() - 1) {
                    c.setBorder(BorderFactory.createMatteBorder(1, 20, 20, 0, tree.getBackground()));
                }
                else {
                    c.setBorder(BorderFactory.createMatteBorder(1, 20, 0, 0, tree.getBackground()));
                }
            }
            else {
                c.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
            }
            setHighlightKeywords(true);
            return c;
        }
    }


    private OWLObject linkedObject;


    public JComponent getComponent() {
        return this;
    }


    public OWLObject getLinkedObject() {
        return linkedObject;
    }


    public Point getMouseCellLocation() {
        Point mousePos = getMousePosition();
        if (mousePos == null) {
            return null;
        }
        Rectangle r = getMouseCellRect();
        if (r == null) {
            return null;
        }
        return new Point(mousePos.x - r.x, mousePos.y - r.y);
    }


    public Rectangle getMouseCellRect() {
        Point mousePos = getMousePosition();
        if (mousePos == null) {
            return null;
        }
        int row = getRowForLocation(mousePos.x, mousePos.y);
        if (row == -1) {
            return null;
        }
        Rectangle r = getRowBounds(row);
        return r;
    }


    //    public Object getCellObject();
    public void setLinkedObject(OWLObject object) {
        linkedObject = object;
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color oldColor = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < getRowCount(); i++) {
            Rectangle rowBounds = getRowBounds(i);
            if (g.getClipBounds().intersects(rowBounds)) {
                if (getPathForRow(i).getPathCount() == 2) {
                    g.drawLine(0, rowBounds.y, getWidth(), rowBounds.y);
                }
            }
        }
        g.setColor(oldColor);
    }
}
