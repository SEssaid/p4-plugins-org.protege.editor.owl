package org.protege.editor.owl.model.library;

import org.apache.log4j.Logger;

import java.net.URI;
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
 * Date: 23-Aug-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OntologyLibraryManager {

    private static final Logger logger = Logger.getLogger(OntologyLibraryManager.class);

    private List<OntologyLibrary> libraries;


    public OntologyLibraryManager() {
        libraries = new ArrayList<OntologyLibrary>();
    }


    public void addLibrary(OntologyLibrary library) {
        libraries.add(library);
    }


    public List<OntologyLibrary> getLibraries() {
        return new ArrayList<OntologyLibrary>(libraries);
    }


    public void removeLibraray(OntologyLibrary library) {
        libraries.remove(library);
    }


    public OntologyLibrary getLibrary(URI ontologyURI) {
        for (OntologyLibrary library : libraries) {
            if (library.contains(ontologyURI)) {
                return library;
            }
        }
        return null;
    }
}
