package org.protege.editor.owl.ui.selector;

import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.view.ViewComponentPlugin;
import org.protege.editor.core.ui.view.ViewComponentPluginAdapter;
import org.protege.editor.core.ui.workspace.Workspace;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.clshierarchy.ToldOWLClassHierarchyViewComponent;
import org.protege.editor.owl.ui.renderer.OWLSystemColors;
import org.semanticweb.owl.model.OWLClass;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: May 2, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLClassSelectorPanel extends AbstractSelectorPanel<OWLClass> {

    private ToldOWLClassHierarchyViewComponent viewComponent;


    public OWLClassSelectorPanel(OWLEditorKit editorKit) {
        super(editorKit);
    }


    protected ViewComponentPlugin getViewComponentPlugin() {

        return new ViewComponentPluginAdapter() {
            public String getLabel() {
                return "OWL Asserted Class Hierarchy";
            }


            public Workspace getWorkspace() {
                return getOWLEditorKit().getOWLWorkspace();
            }


            public ViewComponent newInstance() throws ClassNotFoundException, IllegalAccessException,
                                                      InstantiationException {
                viewComponent = new ToldOWLClassHierarchyViewComponent();
                viewComponent.setup(this);
                return viewComponent;
            }


            public Color getBackgroundColor() {
                return OWLSystemColors.getOWLClassColor();
            }
        };
    }

    public void setSelection(OWLClass cls) {
        viewComponent.setSelectedClass(cls);
    }

    public OWLClass getSelectedObject() {
        return viewComponent.getSelectedClass();
    }

    public Set<OWLClass> getSelectedObjects() {
        return viewComponent.getSelectedClasses();
    }


    public void dispose() {
        viewComponent.dispose();
    }


    public void addSelectionListener(ChangeListener listener) {
        viewComponent.addChangeListener(listener);
    }

    public void removeSelectionListener(ChangeListener listener) {
        viewComponent.removeChangeListener(listener);
    }

    /**
     * @deprecated Use <code>setSelection()</code>
     * @param cls
     */
    public void setSelectedClass(OWLClass cls) {
        setSelection(cls);
    }

    /**
     * @deprecated Use <code>getSelectedObject()</code>
     * @return OWLClass
     */
    public OWLClass getSelectedClass() {
        return getSelectedObject();
    }

    /**
     * @deprecated Use <code>getSelectedObjects</code>
     * @return set of selected classes
     */
    public Set<OWLClass> getSelectedClasses() {
        return viewComponent.getSelectedClasses();
    }
}
