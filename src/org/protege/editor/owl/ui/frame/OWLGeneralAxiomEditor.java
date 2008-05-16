package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLClassAxiomChecker;
import org.semanticweb.owl.model.OWLClassAxiom;
import org.semanticweb.owl.model.OWLException;

import javax.swing.*;
import java.awt.*;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Apr-2007<br><br>
 */
public class OWLGeneralAxiomEditor extends AbstractOWLFrameSectionRowObjectEditor<OWLClassAxiom> implements VerifiedInputEditor {

    private OWLEditorKit editorKit;

    private OWLClassAxiomChecker checker;

    private ExpressionEditor<OWLClassAxiom> editor;

    private JComponent editingComponent;


    public OWLGeneralAxiomEditor(OWLEditorKit editorKit) {
        this.editorKit = editorKit;

        checker = new OWLClassAxiomChecker(editorKit);
        editor = new ExpressionEditor<OWLClassAxiom>(editorKit, checker);

        editingComponent = new JPanel(new BorderLayout());
        editingComponent.add(editor);
        editingComponent.setPreferredSize(new Dimension(400, 200));
    }


    public void setEditedObject(OWLClassAxiom axiom) {
        editor.setText(editorKit.getOWLModelManager().getRendering(axiom));
    }


    public JComponent getInlineEditorComponent() {
        // Same as general editor component
        return editingComponent;
    }


    /**
     * Gets a component that will be used to edit the specified
     * object.
     * @return The component that will be used to edit the object
     */
    public JComponent getEditorComponent() {
        return editingComponent;
    }


    public void clear() {
        editor.setText("");
    }


    /**
     * Gets the object that has been edited.
     * @return The edited object
     */
    public OWLClassAxiom getEditedObject() {
        try {
            if (editor.isWellFormed()) {
                String expression = editor.getText();
                return editor.getExpressionChecker().createObject(expression);
            }
            else {
                return null;
            }
        }
        catch (OWLException e) {
            return null;
        }
    }


    public void dispose() {
    }


    public void addStatusChangedListener(InputVerificationStatusChangedListener listener) {
        editor.addStatusChangedListener(listener);
    }


    public void removeStatusChangedListener(InputVerificationStatusChangedListener listener) {
        editor.removeStatusChangedListener(listener);
    }
}
