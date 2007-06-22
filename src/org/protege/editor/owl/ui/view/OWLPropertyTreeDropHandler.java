package org.protege.editor.owl.ui.view;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.tree.OWLTreeDragAndDropHandler;
import org.semanticweb.owl.model.*;

import java.util.ArrayList;
import java.util.List;
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
 * Date: 06-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class OWLPropertyTreeDropHandler<N extends OWLPropertyExpression> implements OWLTreeDragAndDropHandler<N> {

    private static final Logger logger = Logger.getLogger(OWLPropertyTreeDropHandler.class);

    private OWLModelManager owlModelManager;


    public OWLPropertyTreeDropHandler(OWLModelManager owlModelManager) {
        this.owlModelManager = owlModelManager;
    }


    public void move(N child, N fromParent, N toParent) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        OWLDataFactory df = owlModelManager.getOWLDataFactory();
        changes.add(new AddAxiom(owlModelManager.getActiveOntology(), getAxiom(df, child, toParent)));

        if (fromParent != null) {
            changes.add(new RemoveAxiom(owlModelManager.getActiveOntology(), getAxiom(df, child, fromParent)));
        }
        owlModelManager.applyChanges(changes);
    }


    public void add(N child, N parent) {
        OWLDataFactory df = owlModelManager.getOWLDataFactory();
        owlModelManager.applyChange(new AddAxiom(owlModelManager.getActiveOntology(), getAxiom(df, child, parent)));
    }


    protected abstract OWLAxiom getAxiom(OWLDataFactory df, N child, N parent);
}
