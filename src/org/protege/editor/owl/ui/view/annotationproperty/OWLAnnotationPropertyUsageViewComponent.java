package org.protege.editor.owl.ui.view.annotationproperty;

import org.protege.editor.owl.ui.usage.UsagePanel;
import org.semanticweb.owl.model.OWLAnnotationProperty;

import java.awt.*;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 22-Feb-2007<br><br>
 */
public class OWLAnnotationPropertyUsageViewComponent extends AbstractOWLAnnotationPropertyViewComponent {

    private UsagePanel usagePanel;


    protected OWLAnnotationProperty updateView() {
        OWLAnnotationProperty property = getOWLWorkspace().getOWLSelectionModel().getLastSelectedAnnotationProperty();
        usagePanel.setOWLEntity(property);
        return property;
    }


    public void initialiseView() throws Exception {
        setLayout(new BorderLayout());
        usagePanel = new UsagePanel(getOWLEditorKit());
        add(usagePanel, BorderLayout.CENTER);
    }


    public void disposeView() {
    }
}