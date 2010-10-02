package org.protege.editor.owl.ui.inference;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.protege.editor.owl.model.inference.ReasonerPreferences;
import org.protege.editor.owl.model.inference.ReasonerPreferencesListener;
import org.protege.editor.owl.ui.inference.PrecomputePreferencesTableModel.Column;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;
import org.semanticweb.owlapi.reasoner.InferenceType;

public class PrecomputePreferencesPanel extends OWLPreferencesPanel {
    private static final long serialVersionUID = -8812068573828834020L;
    private Set<InferenceType>             required;
    private Set<InferenceType>             disallowed;
    private Map<InferenceType, JCheckBox> selectedInferences = new EnumMap<InferenceType, JCheckBox>(InferenceType.class);
    private boolean applied = false;
    
    public void initialise() throws Exception {
    	readPreferences();
        setLayout(new BorderLayout());
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    public void dispose() throws Exception {
    }

    private void readPreferences() {
    	ReasonerPreferences preferences = getOWLModelManager().getOWLReasonerManager().getReasonerPreferences();
    	required = EnumSet.noneOf(InferenceType.class);
    	required.addAll(preferences.getRequired());
    	disallowed = EnumSet.noneOf(InferenceType.class);
    	disallowed.addAll(preferences.getDisallowed());
    }
    
    @Override
    public void applyChanges() {
    	ReasonerPreferences preferences = getOWLModelManager().getOWLReasonerManager().getReasonerPreferences();
        for (InferenceType type : InferenceType.values()) {
        	preferences.setRequired(type, required.contains(type));
        	preferences.setDisallowed(type, disallowed.contains(type));
        }
        preferences.save();
    }
    
    
    private JPanel createCenterPanel() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(new JLabel("It is generally recommended that users don't touch this panel.  The"));
        center.add(new JLabel("default setting is to allow plugins to configure what precomputation"));
        center.add(new JLabel("tasks (e.g. classification, realization,...) should be done when a"));
        center.add(new JLabel("reasoner is initialized.  Requiring extra precomputation steps may"));
        center.add(new JLabel("slow the time it takes to initialize a reasoner with no performance"));
        center.add(new JLabel("advantage.  Disallowing precomputation steps may improve the"));
        center.add(new JLabel("performance of reasoner initialization but may slow the performance of"));
        center.add(new JLabel("the plugins that requested that precomputation step."));
        center.add(Box.createRigidArea(new Dimension(0,10)));
        
        JComponent tableContainer = new JPanel(new BorderLayout());
        final PrecomputePreferencesTableModel tableModel = new PrecomputePreferencesTableModel(required, disallowed);
        JTable table = new JTable(tableModel);
        tableContainer.add(table.getTableHeader(), BorderLayout.PAGE_START);
        tableContainer.add(table, BorderLayout.CENTER);
        double preferredWidth = 0;
        for (InferenceType type  : InferenceType.values()) {
        	double width = new JLabel(getInferenceTypeName(type)).getPreferredSize().getWidth();
        	if (width > preferredWidth) preferredWidth = width;
        }
        table.getColumnModel().getColumn(Column.INFERENCE_TYPE.ordinal()).setPreferredWidth((int) preferredWidth);
        table.getColumnModel().getColumn(Column.REQUIRED.ordinal()).setPreferredWidth((int) new JLabel(Column.REQUIRED.getColumnName()).getPreferredSize().getWidth());
        table.getColumnModel().getColumn(Column.DISALLOWED.ordinal()).setPreferredWidth((int) new JLabel(Column.DISALLOWED.getColumnName()).getPreferredSize().getWidth());

        center.add(tableContainer);

        return center;
    }
    
    
    public Set<InferenceType> getPreCompute() {
        Set<InferenceType> preCompute = EnumSet.noneOf(InferenceType.class);
        for (Entry<InferenceType, JCheckBox> entry : selectedInferences.entrySet()) {
            JCheckBox check = entry.getValue();
            if (check.isSelected()) {
                preCompute.add(entry.getKey());
            }
        }
        return preCompute;
    }
    
    public static String getInferenceTypeName(InferenceType type) {
        switch (type)  {
        case CLASS_ASSERTIONS:           return "Inferred Individuals and types";
        case CLASS_HIERARCHY:            return "Inferred Class Hierarchy";
        case DISJOINT_CLASSES:           return "Inferred disjoint classes";
        case DATA_PROPERTY_ASSERTIONS:   return "Inferred Data Property Assertions (incomplete)";
        case DATA_PROPERTY_HIERARCHY:    return "Inferred Data Property Hierarchy";
        case OBJECT_PROPERTY_ASSERTIONS: return "Inferred Object Property Values";
        case OBJECT_PROPERTY_HIERARCHY:  return "Inferred Object Property Hierarchy";
        case SAME_INDIVIDUAL:            return "Inferred equal individuals";
        case DIFFERENT_INDIVIDUALS:      return "Inferred different individuals";
        default:                         throw new IllegalStateException("Programmer error");
        }
    }
    
}
