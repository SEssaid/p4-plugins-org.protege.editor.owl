package org.protege.editor.owl.model.library;

import org.java.plugin.registry.Extension;
import org.protege.editor.core.plugin.ExtensionInstantiator;
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
 * Date: 20-Jul-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OntologyLibraryFactoryPluginJPFImpl implements OntologyLibraryFactoryPlugin {

    private Extension extension;


    public OntologyLibraryFactoryPluginJPFImpl(Extension extension) {
        this.extension = extension;
    }


    /**
     * Gets the plugin Id.
     * @return A <code>String</code> that represents the Id of
     *         the plugin
     */
    public String getId() {
        return extension.getId();
    }


    public String getDocumentation() {
        return extension.getDocumentation().getText();
    }


    /**
     * Creates an instance of the plugin.  It is expected that
     * this instance will be "setup", but the instance's
     * initialise method will not have been called in the instantiation
     * process.
     */
    public OntologyLibraryFactory newInstance() throws ClassNotFoundException, IllegalAccessException,
                                                       InstantiationException {
        ExtensionInstantiator<OntologyLibraryFactory> instantiator = new ExtensionInstantiator<OntologyLibraryFactory>(
                extension);
        return instantiator.instantiate();
    }
}
