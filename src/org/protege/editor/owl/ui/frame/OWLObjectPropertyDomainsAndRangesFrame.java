package org.protege.editor.owl.ui.frame;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLObjectProperty;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLObjectPropertyDomainsAndRangesFrame extends AbstractOWLFrame<OWLObjectProperty> {

    public OWLObjectPropertyDomainsAndRangesFrame(OWLEditorKit owlEditorKit) {
        super(owlEditorKit.getOWLModelManager().getOWLOntologyManager());
        addSection(new OWLObjectPropertyDomainFrameSection(owlEditorKit, this));
        addSection(new OWLObjectPropertyRangeFrameSection(owlEditorKit, this));
    }
}
