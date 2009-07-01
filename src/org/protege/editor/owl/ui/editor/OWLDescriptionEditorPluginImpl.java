package org.protege.editor.owl.ui.editor;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.ExtensionInstantiator;
import org.protege.editor.core.plugin.JPFUtil;
import org.protege.editor.core.plugin.PluginUtilities;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.AxiomType;
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
 * Date: Feb 26, 2009<br><br>
 */
public class OWLDescriptionEditorPluginImpl implements OWLDescriptionEditorPlugin {

    private IExtension extension;

    private OWLEditorKit editorKit;

    public OWLDescriptionEditorPluginImpl(OWLEditorKit editorKit, IExtension extension) {
        this.extension = extension;
        this.editorKit = editorKit;
    }


    public String getId() {
        return extension.getUniqueIdentifier();
    }


    public String getDocumentation() {
        return JPFUtil.getDocumentation(extension);
    }


    public boolean isSuitableFor(AxiomType type) {
        String axiomTypes = PluginUtilities.getAttribute(extension, "axiomTypes");
        if (axiomTypes == null){
            return true;
        }

        if (type != null){
            for(String axiomType : axiomTypes.split(",")){
                if (type.toString().equals(axiomType.trim())){
                    return true;
                }
            }
        }
        return false;
    }


    public String getIndex() {
        String index = PluginUtilities.getAttribute(extension, "index");
        return index != null ? index : "ZZZ";
    }


    public OWLDescriptionEditor newInstance() throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        ExtensionInstantiator<OWLDescriptionEditor> instantiator = new ExtensionInstantiator<OWLDescriptionEditor>(extension);
        OWLDescriptionEditor editor =  instantiator.instantiate();
        editor.setup(extension.getUniqueIdentifier(), PluginUtilities.getAttribute(extension, "label"), editorKit);
        return editor;
    }
}