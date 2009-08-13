package org.protege.editor.owl.ui.editor;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLClassExpression;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 15-Feb-2007<br><br>
 */
public class OWLClassDescriptionEditor extends AbstractOWLObjectEditor<OWLClassExpression>
        implements VerifiedInputEditor {

    private OWLEditorKit editorKit;

    private JComponent editingComponent;

    private JTabbedPane tabbedPane;

    private java.util.List<OWLClassExpressionEditor> activeEditors = new ArrayList<OWLClassExpressionEditor>();

    private Set<OWLClassExpressionEditor> editors = new HashSet<OWLClassExpressionEditor>();

    private boolean currentStatus = false;

    private Set<InputVerificationStatusChangedListener> listeners = new HashSet<InputVerificationStatusChangedListener>();

    private ChangeListener changeListener = new ChangeListener(){
        public void stateChanged(ChangeEvent changeEvent) {
            handleVerifyEditorContents();
        }
    };

    private OWLClassExpression expression;

    private InputVerificationStatusChangedListener inputListener = new InputVerificationStatusChangedListener(){
        public void verifiedStatusChanged(boolean newState) {
            handleVerifyEditorContents();
        }
    };


    public OWLClassDescriptionEditor(OWLEditorKit editorKit, OWLClassExpression expression) {

        this.editorKit = editorKit;

        this.expression = expression;

        editingComponent = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);

        editingComponent.add(tabbedPane);

        editingComponent.setPreferredSize(new Dimension(600, 400));

        tabbedPane.addChangeListener(changeListener);
    }


    public void addPanel(OWLClassExpressionEditor editorPanel){
        editors.add(editorPanel);

        if (editorPanel.setDescription(expression)){
            activeEditors.add(editorPanel);
            tabbedPane.add(editorPanel.getEditorName(), editorPanel.getComponent());
            editorPanel.addStatusChangedListener(inputListener);
            tabbedPane.setSelectedIndex(0);
        }
    }


    private void handleVerifyEditorContents() {
        boolean newStatus = isValidated();
        if (currentStatus != newStatus){
            currentStatus = newStatus;
            for (InputVerificationStatusChangedListener l : listeners){
                l.verifiedStatusChanged(newStatus);
            }
        }
    }


    private boolean isValidated() {
            OWLClassExpressionEditor editor = getSelectedEditor();
            return editor.isValidInput();
    }


    private OWLClassExpressionEditor getSelectedEditor() {
        return activeEditors.get(tabbedPane.getSelectedIndex());
    }


    public String getEditorTypeName() {
        return "Class expression";
    }


    public boolean canEdit(Object object) {
        return object instanceof OWLClassExpression;
    }


    /**
     * Gets a component that will be used to edit the specified
     * object.
     * @return The component that will be used to edit the object
     */
    public JComponent getEditorComponent() {
        return editingComponent;
    }


    public Set<OWLClassExpression> getEditedObjects() {
        return getSelectedEditor().getClassExpressions();
    }


    public boolean setEditedObject(OWLClassExpression expression) {
        this.expression = expression;

        activeEditors.clear();
        tabbedPane.removeChangeListener(changeListener);
        tabbedPane.removeAll();

        for (OWLClassExpressionEditor editor : editors){
            if (editor.setDescription(this.expression)){
                activeEditors.add(editor);
                tabbedPane.add(editor.getEditorName(), editor.getComponent());
                editor.addStatusChangedListener(inputListener);
            }
        }

        tabbedPane.validate();
        tabbedPane.addChangeListener(changeListener);
        
        return !activeEditors.isEmpty(); // then no editors are appropriate for this expression
    }


    public OWLClassExpression getEditedObject() {
        Set<OWLClassExpression> sel = getSelectedEditor().getClassExpressions();
        if (sel.isEmpty()){
            return null;
        }
        else{
            return sel.iterator().next();
        }
    }


    public void dispose() {
        for (OWLClassExpressionEditor editor : editors){
            try {
                editor.dispose();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void addStatusChangedListener(InputVerificationStatusChangedListener l) {
        listeners.add(l);
        l.verifiedStatusChanged(isValidated());
    }


    public void removeStatusChangedListener(InputVerificationStatusChangedListener l) {
        listeners.remove(l);
    }
}
