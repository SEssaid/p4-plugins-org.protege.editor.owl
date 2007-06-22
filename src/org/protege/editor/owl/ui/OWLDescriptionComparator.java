package org.protege.editor.owl.ui;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.renderer.OWLEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLObjectRenderer;
import org.semanticweb.owl.model.*;

import java.util.Comparator;
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
 * Date: 09-May-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLDescriptionComparator implements Comparator<OWLDescription> {

    private TypeVisitor typeVisitor;

    private OWLModelManager owlModelManager;


    public OWLDescriptionComparator(OWLModelManager owlModelManager) {
        this.owlModelManager = owlModelManager;
        this.typeVisitor = new TypeVisitor();
    }


    public int compare(OWLDescription o1, OWLDescription o2) {
        o1.accept(typeVisitor);
        int type1 = typeVisitor.getType();
        o2.accept(typeVisitor);
        int type2 = typeVisitor.getType();
        int diff = type1 - type2;
        if (diff != 0) {
            return diff;
        }
        if (owlModelManager != null) {
            OWLObjectRenderer renderer = owlModelManager.getOWLObjectRenderer();
            OWLEntityRenderer entityRenderer = owlModelManager.getOWLEntityRenderer();
            return renderer.render(o1, entityRenderer).compareTo(renderer.render(o2, entityRenderer));
        }
        return -1;
    }


    private class TypeVisitor implements OWLDescriptionVisitor {

        private int type;


        public int getType() {
            return type;
        }


        public void visit(OWLClass owlClass) {
            type = 1000;
        }


        public void visit(OWLObjectIntersectionOf owlAnd) {
            type = 2000;
        }


        public void visit(OWLObjectComplementOf owlNot) {
            type = 2001;
        }


        public void visit(OWLObjectUnionOf owlOr) {
            type = 2002;
        }


        public void visit(OWLObjectSomeRestriction owlObjectSomeRestriction) {
            type = 3001;
        }


        public void visit(OWLObjectValueRestriction owlObjectValueRestriction) {
            type = 3002;
        }


        public void visit(OWLObjectAllRestriction owlObjectAllRestriction) {
            type = 3003;
        }


        public void visit(OWLDataSomeRestriction owlDataSomeRestriction) {
            type = 3004;
        }


        public void visit(OWLDataValueRestriction owlDataValueRestriction) {
            type = 3005;
        }


        public void visit(OWLDataAllRestriction owlDataAllRestriction) {
            type = 3006;
        }


        public void visit(OWLObjectMinCardinalityRestriction desc) {
            type = 3010;
        }


        public void visit(OWLObjectExactCardinalityRestriction desc) {
            type = 3011;
        }


        public void visit(OWLObjectMaxCardinalityRestriction desc) {
            type = 3012;
        }


        public void visit(OWLDataMinCardinalityRestriction desc) {
            type = 3007;
        }


        public void visit(OWLDataExactCardinalityRestriction desc) {
            type = 3008;
        }


        public void visit(OWLDataMaxCardinalityRestriction desc) {
            type = 3009;
        }


        public void visit(OWLObjectOneOf owlEnumeration) {
            type = 4000;
        }


        public void visit(OWLObjectSelfRestriction desc) {
            type = 6000;
        }
    }
}
