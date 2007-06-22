package org.protege.editor.owl.model.hierarchy;

import org.semanticweb.owl.model.*;

import java.util.HashSet;
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
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jan-2007<br><br>
 */
public class OWLObjectPropertyHierarchyProvider extends AbstractOWLPropertyHierarchyProvider<OWLObjectPropertyExpression, OWLObjectProperty> {

    public OWLObjectPropertyHierarchyProvider(OWLOntologyManager owlOntologyManager) {
        super(owlOntologyManager);
    }


    protected Set<OWLObjectProperty> getPropertiesReferencedInChange(List<? extends OWLOntologyChange> changes) {
        Set<OWLObjectProperty> properties = new HashSet<OWLObjectProperty>();
        for (OWLOntologyChange change : changes) {
            if (change.isAxiomChange()) {
                OWLAxiomChange axiomChange = (OWLAxiomChange) change;
                for (OWLEntity entity : axiomChange.getEntities()) {
                    if (entity instanceof OWLObjectProperty) {
                        properties.add((OWLObjectProperty) entity);
                    }
                }
            }
        }
        return properties;
    }


    /**
     * Gets the relevant properties in the specified ontology that are contained
     * within the property hierarchy.  For example, for an object property hierarchy
     * this would constitute the set of referenced object properties in the specified
     * ontology.
     * @param ont The ontology
     */
    protected Set<? extends OWLObjectProperty> getReferencedProperties(OWLOntology ont) {
        return ont.getReferencedObjectProperties();
    }


    protected Set<? extends OWLSubPropertyAxiom> getSubPropertyAxiomForRHS(OWLObjectProperty prop, OWLOntology ont) {
        return ont.getObjectSubPropertyAxiomsForRHS(prop);
    }


    protected boolean containsReference(OWLOntology ont, OWLObjectProperty prop) {
        return ont.containsObjectPropertyReference(prop.getURI());
    }
}
