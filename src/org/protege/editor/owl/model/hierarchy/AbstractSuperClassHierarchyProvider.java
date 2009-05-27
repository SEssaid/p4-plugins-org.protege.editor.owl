package org.protege.editor.owl.model.hierarchy;

import java.util.Collections;
import java.util.Set;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntologyManager;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 14-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * A "reverse hierarchy" of superclass relationships.
 * Children are superclasses of their parents.
 */
public abstract class AbstractSuperClassHierarchyProvider extends AbstractOWLObjectHierarchyProvider<OWLClass> {

    private OWLClass rootClass;


    public AbstractSuperClassHierarchyProvider(OWLOntologyManager manager) {
        super(manager);
    }


    public void setRoot(OWLClass cls) {
        rootClass = cls;
        fireHierarchyChanged();
    }


    public Set<OWLClass> getSubPropertiesOfRoot() {
        if (rootClass == null) {
            return Collections.emptySet();
        }
        else {
            return Collections.singleton(rootClass);
        }
    }
}
