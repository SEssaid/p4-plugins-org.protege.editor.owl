package org.protege.editor.owl.ui;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.core.ui.util.UIUtil;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owl.io.OWLXMLOntologyFormat;
import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyFormat;
import org.semanticweb.owl.util.SimpleURIShortFormProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;
import java.util.List;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 21-Mar-2007<br><br>
 */
public class GatherOntologiesPanel extends JPanel {

    private OWLEditorKit owlEditorKit;

    private OWLModelManager owlModelManager;

    private JComboBox formatComboBox;

    private File saveLocation;

    private Set<OWLOntology> ontologiesToSave;


    public GatherOntologiesPanel(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = owlEditorKit;
        this.owlModelManager = owlEditorKit.getOWLModelManager();
        ontologiesToSave = new HashSet<OWLOntology>();
        createUI();
    }


    private void createUI() {

        JPanel holderPanel = new JPanel(new BorderLayout());
        JPanel comboBoxLabelPanel = new JPanel(new BorderLayout(7, 7));
        List<Object> formats = new ArrayList<Object>();
        formats.add("Original");
        formats.add(new RDFXMLOntologyFormat());
        formats.add(new OWLXMLOntologyFormat());
        formats.add(new OWLFunctionalSyntaxOntologyFormat());
        formatComboBox = new JComboBox(formats.toArray());
        comboBoxLabelPanel.add(new JLabel("Format"), BorderLayout.WEST);
        comboBoxLabelPanel.add(formatComboBox, BorderLayout.EAST);
        JPanel formatPanelHolder = new JPanel();
        formatPanelHolder.add(comboBoxLabelPanel);
        holderPanel.add(formatPanelHolder, BorderLayout.NORTH);

        Box box = new Box(BoxLayout.Y_AXIS);

        final List<OWLOntology> orderedOntologies = new ArrayList<OWLOntology>(owlModelManager.getOntologies());
        Collections.sort(orderedOntologies, new OWLObjectComparator<OWLOntology>(owlModelManager));
        for (final OWLOntology ont : orderedOntologies) {
            ontologiesToSave.add(ont);
            String name = new SimpleURIShortFormProvider().getShortForm(ont.getURI());
            String label = ont.getURI().toString();
            if (name != null) {
                label = "<html><b>" + name + "</b>  <font color=\"gray\">(" + label + "</font></html>";
            }

            final JCheckBox cb = new JCheckBox(new AbstractAction(label) {
                public void actionPerformed(ActionEvent e) {
                    if (!ontologiesToSave.contains(ont)) {
                        ontologiesToSave.remove(ont);
                    }
                    else {
                        ontologiesToSave.add(ont);
                    }
                }
            });
            cb.setSelected(true);
            cb.setOpaque(false);
            box.add(cb);
            box.add(Box.createVerticalStrut(3));
        }

        box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 150));
        box.setBackground(Color.WHITE);
        JPanel boxHolder = new JPanel(new BorderLayout());
        boxHolder.setBorder(ComponentFactory.createTitledBorder("Ontologies"));
        boxHolder.add(new JScrollPane(box));
        boxHolder.setPreferredSize(new Dimension(boxHolder.getPreferredSize().width,
                                                 Math.min(boxHolder.getPreferredSize().height, 300)));
        holderPanel.add(boxHolder, BorderLayout.CENTER);
        holderPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new BorderLayout());
        add(holderPanel, BorderLayout.CENTER);
    }


    public Set<OWLOntology> getOntologiesToSave() {
        return ontologiesToSave;
    }


    public OWLOntologyFormat getOntologyFormat() {
        Object selFormat = formatComboBox.getSelectedItem();
        if (selFormat instanceof OWLOntologyFormat) {
            return (OWLOntologyFormat) selFormat;
        }
        else {
            return null;
        }
    }


    public File getSaveLocation() {
        return saveLocation;
    }


    public void setSaveLocation(File saveLocation) {
        this.saveLocation = saveLocation;
    }


    public static GatherOntologiesPanel showDialog(OWLEditorKit owlEditorKit) {
        GatherOntologiesPanel panel = new GatherOntologiesPanel(owlEditorKit);
        panel.setPreferredSize(new Dimension(600, 400));

        int ret = JOptionPane.showConfirmDialog(null,
                                                panel,
                                                "Gather ontologies",
                                                JOptionPane.OK_CANCEL_OPTION,
                                                JOptionPane.PLAIN_MESSAGE);
        if (ret != JOptionPane.OK_OPTION) {
            return null;
        }
        File file = UIUtil.chooseFolder(owlEditorKit.getOWLWorkspace(), "Select folder to save the ontologies to");
        if (file == null) {
            return null;
        }
        panel.setSaveLocation(file);
        return panel;
    }
}
