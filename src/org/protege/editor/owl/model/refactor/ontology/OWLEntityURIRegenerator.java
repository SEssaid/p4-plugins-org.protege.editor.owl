package org.protege.editor.owl.model.refactor.ontology;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.entity.CustomOWLEntityFactory;
import org.protege.editor.owl.model.entity.OWLEntityFactory;
import org.semanticweb.owl.model.OWLEntity;

import java.net.URI;
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
 * Date: Aug 21, 2008<br><br>
 *
 * Takes a URI and replaces the fragment/last path element with a generated ID
 * If no fragment exists, adds the ID directly
 *
 */
public class OWLEntityURIRegenerator {

    private OWLEntityFactory fac;


    public OWLEntityURIRegenerator(OWLModelManager mngr) {

        // regardless of how prefs are set up, we always want to generate an auto ID URI
        this.fac = new CustomOWLEntityFactory(mngr){
            protected boolean isFragmentAutoID() {
                return true;
            }
        };
    }


    public URI generateNewURI(OWLEntity entity) {
        URI base = getBaseURI(entity.getURI());

        String id = ""; // this is the "user given name" which will not be used

        OWLEntity newEntity = getEntity(entity, id, base);

        return newEntity.getURI();
    }


    private OWLEntity getEntity(OWLEntity entity, String id, URI base) {
        if (entity.isOWLClass()){
            return fac.createOWLClass(id, base).getOWLEntity();
        }
        else if (entity.isOWLObjectProperty()){
            return fac.createOWLObjectProperty(id, base).getOWLEntity();
        }
        else if (entity.isOWLDataProperty()){
            return fac.createOWLDataProperty(id, base).getOWLEntity();
        }
        else if (entity.isOWLIndividual()){
            return fac.createOWLIndividual(id, base).getOWLEntity();
        }
        return null;
    }


    private URI getBaseURI(URI uri) {
        String fragment = uri.getFragment();
        if (fragment == null) {
            String path = uri.getPath();
            if (path != null) {
                fragment = path.substring(path.lastIndexOf("/") + 1);
            }
        }

        if (fragment != null){
            return URI.create(uri.toString().substring(0, uri.toString().lastIndexOf(fragment)));
        }
        return uri;
    }
}
