package org.protege.editor.owl.ui.clsdescriptioneditor;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.OWLClassAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.SWRLRule;

import java.util.List;
import java.util.Set;
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
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jul 23, 2008<br><br>
 */
public class ManchesterOWLExpressionCheckerFactory implements OWLExpressionCheckerFactory {

    private OWLModelManager mngr;


    public ManchesterOWLExpressionCheckerFactory(OWLModelManager mngr) {
        this.mngr = mngr;
    }


    public OWLExpressionChecker<OWLDescription> getOWLDescriptionChecker() {
        return new OWLDescriptionChecker(mngr);
    }


    public OWLExpressionChecker<Set<OWLDescription>> getOWLDescriptionSetChecker() {
        return new OWLDescriptionSetChecker(mngr);
    }


    public OWLExpressionChecker<OWLClassAxiom> getClassAxiomChecker() {
        return new OWLClassAxiomChecker(mngr);
    }


    public OWLExpressionChecker<List<OWLObjectPropertyExpression>> getPropertyChainChecker() {
        return new OWLPropertyChainChecker(mngr);
    }


    public OWLExpressionChecker<SWRLRule> getSWRLChecker() {
        return new SWRLRuleChecker(mngr);
    }
}
