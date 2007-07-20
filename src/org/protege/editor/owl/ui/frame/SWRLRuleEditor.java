package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.description.OWLExpressionParserException;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionChecker;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLRuntimeException;
import org.semanticweb.owl.model.SWRLRule;

import javax.swing.*;
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
 * Date: 06-Jul-2007<br><br>
 */
public class SWRLRuleEditor extends AbstractOWLFrameSectionRowObjectEditor<SWRLRule> {

    private ExpressionEditor<SWRLRule> editor;

    private OWLEditorKit editorKit;


    public SWRLRuleEditor(final OWLEditorKit editorKit) {
        this.editorKit = editorKit;
        editor = new ExpressionEditor<SWRLRule>(editorKit, new OWLExpressionChecker<SWRLRule>() {

            public void check(String text) throws OWLExpressionParserException, OWLException {
                editorKit.getOWLModelManager().getOWLDescriptionParser().isSWRLRuleWellFormed(text);
            }


            public SWRLRule createObject(String text) throws OWLExpressionParserException, OWLException {
                return editorKit.getOWLModelManager().getOWLDescriptionParser().createSWRLRule(text);
            }
        });
        editor.setPreferredSize(new Dimension(300, 200));
    }


    public JComponent getEditorComponent() {
        return editor;
    }


    public void setObject(SWRLRule rule) {
        editor.setText(editorKit.getOWLModelManager().getRendering(rule));
    }


    public SWRLRule getEditedObject() {
        try {
            return editor.createObject();
        }
        catch (OWLException e) {
            throw new OWLRuntimeException(e);
        }
    }


    public void clear() {
        editor.setText("");
    }


    public void dispose() {
    }
}
