package org.protege.editor.owl.ui.clsdescriptioneditor;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.OWLObject;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
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
 * Date: May 4, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AutoCompleterMatcherImpl implements AutoCompleterMatcher {

    private OWLModelManager owlModelManager;


    public AutoCompleterMatcherImpl(OWLModelManager owlModelManager) {
        this.owlModelManager = owlModelManager;
    }


    public Set getMatches(String fragment, boolean classes, boolean objectProperties, boolean dataProperties,
                          boolean individuals, boolean datatypes) {
        TreeSet set = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                String ren1 = owlModelManager.getOWLObjectRenderer().render((OWLObject) o1,
                                                                            owlModelManager.getOWLEntityRenderer());
                String ren2 = owlModelManager.getOWLObjectRenderer().render((OWLObject) o2,
                                                                            owlModelManager.getOWLEntityRenderer());
                return ren1.compareTo(ren2);
            }
        });
        if (classes) {
            set.addAll(owlModelManager.getMatchingOWLClasses(fragment));
        }
        if (objectProperties) {
            set.addAll(owlModelManager.getMatchingOWLObjectProperties(fragment));
        }
        if (dataProperties) {
            set.addAll(owlModelManager.getMatchingOWLDataProperties(fragment));
        }
        if (individuals) {
            set.addAll(owlModelManager.getMatchingOWLIndividuals(fragment));
        }

        if (datatypes) {
            set.addAll(owlModelManager.getMatchingOWLDataTypes(fragment));
        }
        return set;
    }
}
