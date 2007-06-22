package org.protege.editor.owl.ui.find;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.find.EntityFinderPreferences;
import org.protege.editor.owl.ui.OWLEntityComparator;
import org.semanticweb.owl.model.OWLEntity;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.TreeSet;
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
 * Date: 16-May-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class EntityFinderField extends JTextField {

    private OWLEditorKit editorKit;

    private JList resultsList;

    private JWindow window;

    private JComponent parent;


    public EntityFinderField(JComponent parent, OWLEditorKit editorKit) {
        super(20);
        this.parent = parent;
        this.editorKit = editorKit;
        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    closeResults();
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectEntity();
                }
            }


            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    decrementListSelection();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    incrementListSelection();
                }
            }
        });
        getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }


            public void insertUpdate(DocumentEvent e) {
                performFind();
            }


            public void removeUpdate(DocumentEvent e) {
                performFind();
            }
        });
        resultsList = new JList();
        resultsList.setCellRenderer(editorKit.getOWLWorkspace().createOWLCellRenderer());
        resultsList.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectEntity();
                }
            }
        });
    }


    private void selectEntity() {
        OWLEntity selEntity = (OWLEntity) resultsList.getSelectedValue();
        if (selEntity != null) {
            closeResults();
            EntityFinderField.this.editorKit.getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(selEntity);
            editorKit.getOWLWorkspace().displayOWLEntity(selEntity);
        }
    }


    private void incrementListSelection() {
        if (resultsList.getModel().getSize() > 0) {
            int selIndex = resultsList.getSelectedIndex();
            selIndex++;
            if (selIndex > resultsList.getModel().getSize() - 1) {
                selIndex = 0;
            }
            resultsList.setSelectedIndex(selIndex);
            resultsList.scrollRectToVisible(resultsList.getCellBounds(selIndex, selIndex));
        }
    }


    private void decrementListSelection() {
        if (resultsList.getModel().getSize() > 0) {
            int selIndex = resultsList.getSelectedIndex();
            selIndex--;
            if (selIndex < 0) {
                selIndex = resultsList.getModel().getSize() - 1;
            }
            resultsList.setSelectedIndex(selIndex);
            resultsList.scrollRectToVisible(resultsList.getCellBounds(selIndex, selIndex));
        }
    }


    private void closeResults() {
        getWindow().setVisible(false);
        resultsList.setListData(new Object []{});
    }


    private Timer timer = new Timer(400, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            executeFind();
            timer.stop();
        }
    });


    private void executeFind() {
        if (getText().trim().length() > 0) {
            Set<OWLEntity> results = results = editorKit.getOWLModelManager().getEntityFinder().getEntities(getText());
            showResults(results);
        }
        else {
            closeResults();
        }
    }


    private void performFind() {
        timer.setDelay((int) EntityFinderPreferences.getInstance().getSearchDelay());
        timer.restart();
    }


    private JWindow getWindow() {
        if (window == null) {
            Window w = (Window) SwingUtilities.getAncestorOfClass(Window.class, parent);
            window = new JWindow(w);
            window.setFocusableWindowState(false);
            JScrollPane sp = ComponentFactory.createScrollPane(resultsList);
            sp.setBorder(null);
            window.setContentPane(sp);
            addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    window.setVisible(false);
                    resultsList.setListData(new Object []{});
                }
            });
            SwingUtilities.getRoot(this).addComponentListener(new ComponentAdapter() {
                public void componentMoved(ComponentEvent e) {
                    closeResults();
                }
            });
        }
        return window;
    }


    private void showResults(Set<OWLEntity> results) {
        JWindow window = getWindow();
        if (results.size() > 0) {
            Point pt = new Point(0, 0);
            SwingUtilities.convertPointToScreen(pt, this);
            window.setLocation(pt.x, pt.y + getHeight() + 2);
            window.setSize(getWidth(), 200);
            TreeSet ts = new TreeSet(new OWLEntityComparator(editorKit.getOWLModelManager()));
            ts.addAll(results);
            resultsList.setListData(ts.toArray());
            window.setVisible(true);
            window.validate();
            resultsList.setSelectedIndex(0);
        }
        else {
            resultsList.setListData(new Object [0]);
        }
//        owlModelManager.getEditorKit().getWorkspace().showResultsView("FINDER_RESULTS", "Finder results", Color.GRAY,
//                new OWLEntityFinderViewComponent(results), true);
    }
}
