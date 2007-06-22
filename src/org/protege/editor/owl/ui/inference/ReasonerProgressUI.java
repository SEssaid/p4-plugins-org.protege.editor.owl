package org.protege.editor.owl.ui.inference;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.ReasonerProgressMonitor;

import javax.swing.*;
import java.awt.*;
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
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 10-Oct-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ReasonerProgressUI implements ReasonerProgressMonitor {

    private OWLEditorKit owlEditorKit;

    private JLabel label;

    private JProgressBar progressBar;

    private JDialog window;

    private boolean cancelled;

    private Action cancelledAction;

    private String currentClass;


    public ReasonerProgressUI(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = owlEditorKit;
        JPanel panel = new JPanel(new BorderLayout(7, 7));
        progressBar = new JProgressBar();
        panel.add(progressBar, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label = new JLabel("Classifying...");
        panel.add(label, BorderLayout.NORTH);

        window = new JDialog((Frame) (SwingUtilities.getAncestorOfClass(Frame.class, owlEditorKit.getWorkspace())),
                             "Reasoner progress",
                             true);
        cancelledAction = new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent e) {
                setCancelled(true);
            }
        };
        JButton cancelledButton = new JButton(cancelledAction);

        window.setLocation(400, 400);
        JPanel holderPanel = new JPanel(new BorderLayout(7, 7));
        holderPanel.add(panel, BorderLayout.NORTH);
        holderPanel.add(cancelledButton, BorderLayout.EAST);
        holderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        window.getContentPane().setLayout(new BorderLayout());
        window.getContentPane().add(holderPanel, BorderLayout.NORTH);
        window.pack();
        Dimension windowSize = window.getSize();
        window.setSize(400, windowSize.height);
        window.setResizable(false);
    }


    public void setSize(final long size) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(0);
                progressBar.setMaximum((int) size);
            }
        });
    }


    public void setStarted() {
        label.setText("Classifying ontology                    ");
        currentClass = null;
        setCancelled(false);
        showWindow();
    }


    public void setProgress(final long progress) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue((int) progress);
                double percentDone = (progress * 100.0) / progressBar.getMaximum();
//                if(percentDone / 100.0 == 0) {
                label.setText("Classifying ontology " + ((int) percentDone) + " %");
//                }
            }
        });
    }


    public void setIndeterminate(boolean b) {
        progressBar.setIndeterminate(b);
    }


    private void setCancelled(boolean b) {
        cancelledAction.setEnabled(!b);
        cancelled = b;
        if (currentClass != null) {
            JOptionPane.showMessageDialog(window,
                                          "Cancelled while classifying " + currentClass,
                                          "Cancelled classification",
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public void setMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                label.setText(message);
            }
        });
    }


    public void setProgressIndeterminate(boolean b) {
        progressBar.setIndeterminate(b);
    }


    public void setFinished() {
        hideWindow();
        currentClass = null;
    }


    public boolean isCancelled() {
        return cancelled;
    }


    private void showWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                window.setLocation(screenSize.width / 2 - window.getWidth() / 2,
                                   screenSize.height / 2 - window.getHeight() / 2);
                window.setVisible(true);
            }
        });
    }


    private void hideWindow() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (cancelled && currentClass != null) {
                    JOptionPane.showMessageDialog(window,
                                                  "Cancelled while classifying " + currentClass,
                                                  "Cancelled classification",
                                                  JOptionPane.INFORMATION_MESSAGE);
                }
                window.setVisible(false);
            }
        });
    }
}
