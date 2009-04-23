package org.protege.editor.owl.ui.selector;

import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.view.ViewComponentPlugin;
import org.protege.editor.core.ui.view.ViewComponentPluginAdapter;
import org.protege.editor.core.ui.workspace.Workspace;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.renderer.OWLSystemColors;
import org.protege.editor.owl.ui.view.AbstractOWLPropertyHierarchyViewComponent;
import org.protege.editor.owl.ui.view.dataproperty.OWLDataPropertyHierarchyViewComponent;
import org.semanticweb.owl.model.OWLDataProperty;

import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 27-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLDataPropertySelectorPanel extends AbstractHierarchySelectorPanel<OWLDataProperty> {

    private AbstractOWLPropertyHierarchyViewComponent<OWLDataProperty> view;


    public OWLDataPropertySelectorPanel(OWLEditorKit eKit) {
        this(eKit, true);
    }

    public OWLDataPropertySelectorPanel(OWLEditorKit eKit, boolean editable) {
        this(eKit, editable, eKit.getModelManager().getOWLHierarchyManager().getOWLDataPropertyHierarchyProvider());
    }

    public OWLDataPropertySelectorPanel(OWLEditorKit eKit, boolean editable, OWLObjectHierarchyProvider<OWLDataProperty> hp) {
        super(eKit, editable, hp);
    }


    protected ViewComponentPlugin getViewComponentPlugin() {
        return new ViewComponentPluginAdapter() {
            public String getLabel() {
                return "Data properties";
            }


            public Workspace getWorkspace() {
                return getOWLEditorKit().getWorkspace();
            }


            public ViewComponent newInstance() throws ClassNotFoundException, IllegalAccessException,
                    InstantiationException {
                view = new OWLDataPropertyHierarchyViewComponent(){
                    protected void performExtraInitialisation() throws Exception {
                        if (isEditable()){
                            super.performExtraInitialisation();
                        }
                    }

                    protected OWLObjectHierarchyProvider<OWLDataProperty> getHierarchyProvider() {
                        return OWLDataPropertySelectorPanel.this.getHierarchyProvider();
                    }
                };
                view.setup(this);
                return view;
            }


            public Color getBackgroundColor() {
                return OWLSystemColors.getOWLDataPropertyColor();
            }
        };
    }


    public void setSelection(OWLDataProperty property) {
        view.setSelectedEntity(property);
    }


    public void setSelection(Set<OWLDataProperty> properties) {
        view.setSelectedEntities(properties);
    }


    public OWLDataProperty getSelectedObject() {
        return view.getSelectedEntity();
    }

    public Set<OWLDataProperty> getSelectedObjects() {
        return view.getSelectedEntities();
    }


    public void addSelectionListener(ChangeListener listener) {
        view.addChangeListener(listener);
    }

    public void removeSelectionListener(ChangeListener listener) {
        view.removeChangeListener(listener);
    }

    public void dispose() {
        view.dispose();
    }
}
