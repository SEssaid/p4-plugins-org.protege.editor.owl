package org.protege.editor.owl.model.library.folder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.protege.editor.owl.model.library.AbstractOntologyLibrary;
import org.protege.editor.owl.model.library.OntologyLibraryMemento;
import org.protege.editor.owl.model.library.auto.Algorithm;
import org.protege.editor.owl.model.library.auto.XMLCatalogUpdater;
import org.protege.editor.owl.model.library.auto.XmlBaseAlgorithm;
import org.protege.xmlcatalog.CatalogUtilities;
import org.protege.xmlcatalog.XMLCatalog;
import org.semanticweb.owlapi.model.IRI;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 23-Aug-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class FolderOntologyLibrary extends AbstractOntologyLibrary {
	public static final String CATALOG_NAME = "catalog.xml";

    public static final String ID = FolderOntologyLibrary.class.getName();

    public static final String FILE_KEY = "FILE";

    private File folder;

    private XMLCatalog catalog;
    
    private XMLCatalogUpdater updater;

    public FolderOntologyLibrary(File folder) throws IOException {
    	Set<Algorithm> algorithms = new HashSet<Algorithm>();
    	algorithms.add(new XmlBaseAlgorithm());
        this.folder = folder;
        updater = new XMLCatalogUpdater();
        updater.setAlgorithms(algorithms);
        rebuild();
    }


    public String getClassExpression() {
        try {
            return "Folder: " + folder.getCanonicalPath();
        }
        catch (IOException e) {
            return "Folder";
        }
    }
    
    @Override
    public URI getXmlCatalogName() {
    	return new File(folder, CATALOG_NAME).toURI();
    }
    
    @Override
    public XMLCatalog getXmlCatalog() {
    	return catalog;
    }


    public URI getPhysicalURI(IRI ontologyIRI) {
        return CatalogUtilities.getRedirect(ontologyIRI.toURI(), getXmlCatalog());
    }


    public void rebuild() throws IOException {
    	File catalogFile = new File(folder, CATALOG_NAME);
    	catalog = updater.update(catalogFile);
    }


    public OntologyLibraryMemento getMemento() {
        OntologyLibraryMemento memento = new OntologyLibraryMemento(ID);
        memento.putFile(FILE_KEY, folder);
        return memento;
    }

}
