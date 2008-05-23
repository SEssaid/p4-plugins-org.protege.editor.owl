package org.protege.editor.owl.ui.view;

import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLProperty;

/**
 * User: nickdrummond
 * Date: May 23, 2008
 */
public abstract class AbstractOWLPropertyViewComponent<O extends OWLProperty> extends AbstractOWLSelectionViewComponent {

    public void disposeView() {
    }


    final protected OWLEntity updateView() {
        OWLProperty selProp = null;
        if (isOWLDataPropertyView()){
            selProp = updateView((O)getOWLWorkspace().getOWLSelectionModel().getLastSelectedDataProperty());
        }
        else if (isOWLObjectPropertyView()){
            selProp = updateView((O)getOWLWorkspace().getOWLSelectionModel().getLastSelectedObjectProperty());
        }
        if (selProp != null) {
            updateRegisteredActions();
        }
        else {
            disableRegisteredActions();
        }
        return selProp;
    }


    protected abstract O updateView(O property);
}
