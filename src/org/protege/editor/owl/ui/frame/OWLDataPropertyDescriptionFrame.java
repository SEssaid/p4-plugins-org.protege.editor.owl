package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLDataProperty;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 16-Feb-2007<br><br>
 */
public class OWLDataPropertyDescriptionFrame extends AbstractOWLFrame<OWLDataProperty> {

    public OWLDataPropertyDescriptionFrame(OWLEditorKit owlEditorKit) {
        super(owlEditorKit.getOWLModelManager().getOWLOntologyManager());
        addSection(new OWLDataPropertyDomainFrameSection(owlEditorKit, this));
        addSection(new OWLDataPropertyRangeFrameSection(owlEditorKit, this));
        addSection(new OWLEquivalentDataPropertiesFrameSection(owlEditorKit, this));
        addSection(new OWLSubDataPropertyAxiomSuperPropertyFrameSection(owlEditorKit, this));
        addSection(new OWLDisjointDataPropertiesFrameSection(owlEditorKit, this));
    }
}
