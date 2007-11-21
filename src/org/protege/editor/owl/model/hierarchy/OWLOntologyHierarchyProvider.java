package org.protege.editor.owl.model.hierarchy;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.EventType;

import java.util.*;
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
 * Date: 28-Oct-2007<br><br>
 */
public class OWLOntologyHierarchyProvider extends AbstractOWLObjectHierarchyProvider<OWLOntology> {

    private Set<OWLOntology> roots;

    private Map<OWLOntology, Set<OWLOntology>> parent2ChildMap;

    private Map<OWLOntology, Set<OWLOntology>> child2ParentMap;

    private OWLModelManager owlModelManager;


    public OWLOntologyHierarchyProvider(OWLModelManager owlModelManager) {
        super(owlModelManager.getOWLOntologyManager());
        this.owlModelManager = owlModelManager;
        roots = new HashSet<OWLOntology>();
        parent2ChildMap = new HashMap<OWLOntology, Set<OWLOntology>>();
        child2ParentMap = new HashMap<OWLOntology, Set<OWLOntology>>();
        rebuild();
        owlModelManager.addListener(new OWLModelManagerListener() {

            public void handleChange(OWLModelManagerChangeEvent event) {
                if(event.isType(EventType.ONTOLOGY_LOADED)) {
                    rebuild();
                }
            }
        });
        
    }


    public void setOntologies(Set<OWLOntology> ontologies) {
    }

    private void rebuild() {
        roots.clear();
        parent2ChildMap.clear();
        child2ParentMap.clear();
        for(OWLOntology ont : owlModelManager.getOntologies()) {
            for(OWLOntology imp : owlModelManager.getOWLOntologyManager().getImports(ont)) {
                add(ont, imp);
            }
        }
        for(OWLOntology ont : owlModelManager.getOntologies()) {
            if(!child2ParentMap.containsKey(ont)) {
                roots.add(ont);
            }
        }
        fireHierarchyChanged();
    }

    private void add(OWLOntology ont, OWLOntology imp) {
        getChildren(ont, true).add(imp);
        getParents(imp, true).add(ont);
    }

    private Set<OWLOntology> getChildren(OWLOntology parent, boolean add) {
        Set<OWLOntology> children = parent2ChildMap.get(parent);
        if(children == null) {
            children = new HashSet<OWLOntology>();
            if(add) {
                parent2ChildMap.put(parent, children);
            }
        }
        return children;
    }

    private Set<OWLOntology> getParents(OWLOntology child, boolean add) {
        Set<OWLOntology> parents = child2ParentMap.get(child);
        if(parents == null) {
            parents = new HashSet<OWLOntology>();
            if(add) {
                child2ParentMap.put(child, parents);
            }
        }
        return parents;
    }


    public Set<OWLOntology> getRoots() {
        return Collections.unmodifiableSet(roots);
    }


    public Set<OWLOntology> getParents(OWLOntology object) {
        return getParents(object, true);
    }


    public Set<OWLOntology> getEquivalents(OWLOntology object) {
        return Collections.emptySet();
    }


    public Set<OWLOntology> getChildren(OWLOntology object) {
        return getChildren(object, true);
    }


    public boolean containsReference(OWLOntology object) {
        return parent2ChildMap.containsKey(object) ||
                roots.contains(object);
    }
}
