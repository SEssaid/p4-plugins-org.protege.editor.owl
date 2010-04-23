package org.protege.editor.owl.ui.editor;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionCheckerFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.SWRLRule;


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
 * Date: 06-Jul-2007<br><br>
 */
public class SWRLRuleEditor extends AbstractOWLObjectEditor<SWRLRule> implements VerifiedInputEditor {
    private OWLModelManager mngr;

    private ExpressionEditor<SWRLRule> editor;

    private JScrollPane scrollpane;
    
    public SWRLRuleEditor(OWLEditorKit editorKit) {
        final OWLExpressionCheckerFactory fac = editorKit.getModelManager().getOWLExpressionCheckerFactory();
        editor = new ExpressionEditor<SWRLRule>(editorKit, fac.getSWRLChecker());

        scrollpane = new JScrollPane(editor);
        scrollpane.setPreferredSize(new Dimension(500, 200));
        
        mngr = editorKit.getModelManager();
    }


    public String getEditorTypeName() {
        return "SWRL Rule";
    }


    public boolean canEdit(Object object) {
        return object instanceof SWRLRule;
    }


    public JComponent getEditorComponent() {
        return scrollpane;
    }


    public SWRLRule getEditedObject() {
        try {
            return editor.createObject();
        }
        catch (OWLException e) {
            throw new OWLRuntimeException(e);
        }
    }

  /*
   * Workaround for owlapi feature request 2896097.  Remove this fix when 
   * the simple rule renderer and parser is implemented.  Svn at time of 
   * commit is approximately 16831
   */
    public boolean setEditedObject(SWRLRule rule) {
        if (rule == null){
            editor.setText("");
        }
        else{
            editor.setText(mngr.getRendering(rule));
        }
        return true;
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
