package org.protege.editor.owl.model.selection.axioms;

import org.semanticweb.owl.model.OWLOntology;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;/*
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
 * Date: May 30, 2008<br><br>
 */
public abstract class AbstractAxiomSelectionStrategy implements AxiomSelectionStrategy {

    private Set<OWLOntology> ontologies = new HashSet<OWLOntology>();

    private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

    public static final String ONTOLOGIES_CHANGED = "change.ontologies";


    public final void setOntologies(Set<OWLOntology> ontologies){
        this.ontologies = ontologies;
        notifyPropertyChange(ONTOLOGIES_CHANGED);
    }


    protected void notifyPropertyChange(String property) {
        for (PropertyChangeListener l : listeners){
        l.propertyChange(new PropertyChangeEvent(this, property, null, null));
        }
    }


    public final Set<OWLOntology> getOntologies() {
        return ontologies;
    }


    public void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.add(l);
    }


    public void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.remove(l);
    }
}
