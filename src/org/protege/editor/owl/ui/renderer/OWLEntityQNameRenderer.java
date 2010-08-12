package org.protege.editor.owl.ui.renderer;

import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.prefix.OntologyPrefixMapperManager;
import org.protege.editor.owl.ui.prefix.PrefixMapper;
import org.protege.editor.owl.ui.prefix.MergedPrefixMapperManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 20-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLEntityQNameRenderer extends AbstractOWLEntityRenderer implements PrefixBasedRenderer {
	private OntologyPrefixMapperManager prefixManager;

    
    public void initialise() {
    	prefixManager = new OntologyPrefixMapperManager(getOWLModelManager().getActiveOntology());
    }
    
    @Override
    public void ontologiesChanged() {
    	prefixManager = new OntologyPrefixMapperManager(getOWLModelManager().getActiveOntology());
    }


    public String render(IRI iri) {
        try {
            PrefixMapper mapper = prefixManager.getMapper();
            String s = mapper.getShortForm(iri.toURI());
            if (s != null) {
                return s;
            }
            else {
                // No mapping
                if (iri.getFragment() != null) {
                    return iri.getFragment();
                }
                else {
                    return iri.toQuotedString();
                }
            }
        }
        catch (Exception e) {
            return "<Error! " + e.getMessage() + ">";
        }
    }


    protected void disposeRenderer() {
        // do nothing
    }
}
