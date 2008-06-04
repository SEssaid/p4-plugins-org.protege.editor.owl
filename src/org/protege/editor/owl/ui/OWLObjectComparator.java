package org.protege.editor.owl.ui;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLObjectRenderer;
import org.semanticweb.owl.model.OWLObject;

import java.util.Comparator;


/**
 * Author: Nick Drummond<br>
 * nick.drummond@cs.manchester.ac.uk<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Dec 8, 2006<br><br>
 * <p/>
 * code made available under Mozilla Public License (http://www.mozilla.org/MPL/MPL-1.1.html)<br>
 * copyright 2006, The University of Manchester<br>
 */
public class OWLObjectComparator<E extends OWLObject> implements Comparator<E> {

    private OWLModelManager owlModelManager;

    public OWLObjectComparator(OWLModelManager owlModelManager) {
        this.owlModelManager = owlModelManager;
    }

    public int compare(E o1, E o2) {
        final OWLObjectRenderer objRen = owlModelManager.getOWLObjectRenderer();
        final OWLModelManagerEntityRenderer entityRen = owlModelManager.getOWLEntityRenderer();
        String ren1 = objRen.render(o1, entityRen);
        String ren2 = objRen.render(o2, entityRen);
        return ren1.compareToIgnoreCase(ren2);
    }
}
