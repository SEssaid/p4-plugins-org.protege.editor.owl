package org.protege.editor.owl.ui.inference;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.ReasonerProgressMonitor;


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
