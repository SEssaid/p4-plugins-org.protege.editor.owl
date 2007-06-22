package org.protege.editor.owl.model;

import org.protege.editor.core.ModelManager;
import org.protege.editor.owl.model.description.OWLDescriptionParser;
import org.protege.editor.owl.model.entity.OWLEntityFactory;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.find.EntityFinder;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.model.history.HistoryManager;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.library.OntologyLibraryManager;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLObjectRenderer;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.*;

import java.net.URI;
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
 * Medical Informatics Group<br>
 * Date: 19-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface OWLModelManager extends ModelManager {

    void addListener(OWLModelManagerListener listener);


    void removeListener(OWLModelManagerListener listener);


    void fireEvent(EventType event);


    /**
     * Loads the ontology that has the specified ontology URI.
     * <p/>
     * @param uri The URI of the ontology to be loaded.  Note
     *            that this is <b>not</b> the physical URI of a document
     *            that contains a representation of the ontology.  The
     *            physical location of any concrete representation of the
     *            ontology is determined by the resolving mechanism.
     */
    OWLOntology loadOntology(URI uri) throws OWLOntologyCreationException;

//
//    /**
//     * Forces the system to use a particular mapping from
//     * an ontology URI (logicalURI) to a physical URI. When
//     * the system is asked to load the ontology specified by
//     * the logicalURI it will load it from the location
//     * specified by the physical URI.
//     * @param logicalURI
//     * @param physicalURI
//     */
//    void setPhysicalURIMapping(URI logicalURI, URI physicalURI);


    /**
     * Creates a new, empty ontology that has the specified
     * ontology URI.  Note that this is NOT the physical URI,
     * it is the logical URI - i.e. the name of the ontology.
     */
    OWLOntology createNewOntology(URI uri, URI physicalURI) throws OWLOntologyCreationException;


    /**
     * Performs a save operation.  The behaviour of this is implementation
     * specific.  For example, some implementations may choose to save the
     * active ontology, other implementations may choose to save all open
     * ontologies etc.
     */
    void save() throws OWLOntologyStorageException;


    void saveAs() throws OWLOntologyStorageException;


    /**
     * Gets the ontologies that are loaded into this model.
     * These are usually ontologies that are the imports closure
     * of some base ontology.  For example, if OntA imports OntB
     * and OntC then the collection of returned ontology will
     * typically contain OntA, OntB and OntC.
     * @return A <code>Set</code> of open <code>OWLOntology</code>
     *         objects.
     */
    Set<OWLOntology> getOntologies();


    /**
     * Gets the set of loaded ontologies that have been edited
     * but haven't been saved.
     * @return A <code>Set</code> of <code>OWLOntology</code>
     *         objects
     */
    Set<OWLOntology> getDirtyOntologies();


    /**
     * Forces the system to believe that an ontology
     * has been modified.
     * @param ontology The ontology to be made dirty.
     */
    void setDirty(OWLOntology ontology);


    /**
     * Gets the active ontology.  This is the ontology that
     * edits that cause information to be added to an ontology
     * usually take place in.
     */
    OWLOntology getActiveOntology();


    /**
     * Gets the ontologies that are in the imports
     * closure of the the active ontology.
     * @return A <code>Set</code> of <code>OWLOntologies</code>
     */
    Set<OWLOntology> getActiveOntologies();


    /**
     * A convenience method that determines if the active
     * ontology is mutable.
     * @return <code>true</code> if the active ontology is mutable
     *         or <code>false</code> if the active ontology isn't mutable.
     */
    boolean isActiveOntologyMutable();


    /**
     * Determines if the specified ontology is mutable.
     * @param ontology The ontology to be tested/
     * @return <code>true</code> if the ontology is mutable
     *         and can be edited, <code>false</code> if the ontology
     *         is not mutable i.e. can't be edited.
     */
    boolean isMutable(OWLOntology ontology);


    /**
     * Gets the ontology library manager which is an interface
     * for a repository of "standard"/frequently used ontologies (e.g. upper
     * ontologies).
     */
    public OntologyLibraryManager getOntologyLibraryManager();


    /**
     * This returns the class hierarchy provider whose hierarchy is
     * generated from told information about the active ontologies.
     */
    OWLObjectHierarchyProvider<OWLClass> getOWLClassHierarchyProvider();


    OWLObjectHierarchyProvider<OWLClass> getInferredOWLClassHierarchyProvider();


    OWLObjectHierarchyProvider<OWLObjectProperty> getOWLObjectPropertyHierarchyProvider();


    OWLObjectHierarchyProvider<OWLDataProperty> getOWLDataPropertyHierarchyProvider();


    void rebuildOWLClassHierarchy();


    void rebuildOWLObjectPropertyHierarchy();


    void rebuildOWLDataPropertyHierarchy();


    void setActiveOntology(OWLOntology activeOntology);

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Change
    //
    ////////////////////////////////////////////////////////////////////////////////////////////


    void applyChange(OWLOntologyChange change);


    void applyChanges(List<? extends OWLOntologyChange> changes);


    boolean isChangedEntity(OWLEntity entity);


    /**
     * Gets the change history manager.  This tracks the changes that have
     * been made to various ontologies and has support for undo and redo.
     */
    public HistoryManager getHistoryManager();


    /**
     * Adds an ontology history listener.  The listener will be notified of
     * any changes to any of the ontologies that are managed by this model
     * manager.
     */
    void addOntologyChangeListener(OWLOntologyChangeListener listener);


    /**
     * Removes a previously added listener.
     */
    void removeOntologyChangeListener(OWLOntologyChangeListener listener);


    OWLModelManagerEntityRenderer getOWLEntityRenderer();


    void setOWLEntityRenderer(OWLModelManagerEntityRenderer renderer);


    OWLObjectRenderer getOWLObjectRenderer();


    OWLDescriptionParser getOWLDescriptionParser();


    List<OWLClass> getMatchingOWLClasses(String s);


    List<OWLObjectProperty> getMatchingOWLObjectProperties(String s);


    List<OWLDataProperty> getMatchingOWLDataProperties(String s);


    List<OWLIndividual> getMatchingOWLIndividuals(String s);


    List<OWLDataType> getMatchingOWLDataTypes(String s);


    public OWLClass getOWLClass(String rendering);


    public OWLObjectProperty getOWLObjectProperty(String rendering);


    public OWLDataProperty getOWLDataProperty(String rendering);


    public OWLIndividual getOWLIndividual(String rendering);


    public OWLEntity getOWLEntity(String rendering);


    public Set<String> getOWLEntityRenderings();


    EntityFinder getEntityFinder();


    OWLReasonerManager getOWLReasonerManager();


    OWLReasoner getReasoner();


    void setMissingImportHandler(MissingImportHandler handler);


    URI getOntologyPhysicalURI(OWLOntology ontology);


    void setPhysicalURI(OWLOntology ontology, URI physicalURI);


    OWLEntityFactory getOWLEntityFactory();


    void setOWLEntityFactory(OWLEntityFactory owlEntityFactory);


    OWLOntologyManager getOWLOntologyManager();


    /**
     * Gets the data factory for the active ontology
     */
    OWLDataFactory getOWLDataFactory();


    String getRendering(OWLObject object);


    boolean isCommentedOut(OWLOntology ontology, OWLAxiom axiom);


    void setCommentedOut(OWLOntology ontology, OWLAxiom axiom, boolean b);


    Set<OWLAxiom> getCommentedOutAxioms(OWLOntology ontology);


    Set<OWLOntology> getCommentOntologies();
}
