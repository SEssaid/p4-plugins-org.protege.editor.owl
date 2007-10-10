package org.protege.editor.owl.ui.view;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.protege.editor.owl.ui.frame.OWLClassDescriptionFrame;
import org.protege.editor.owl.ui.framelist.OWLFrameList2;
import org.semanticweb.owl.model.OWLClass;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 19-Jan-2007<br><br>
 */
public class OWLDisjointClassesViewComponent extends AbstractOWLClassViewComponent {

    private OWLFrameList2<OWLClass> list;


    public void initialiseClassView() throws Exception {
        list = new OWLFrameList2<OWLClass>(getOWLEditorKit(), new OWLClassDescriptionFrame(getOWLEditorKit()));
        setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(list);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(sp);
    }


    /**
     * This method is called to request that the view is updated with
     * the specified class.
     * @param selectedClass The class that the view should be updated with.  Note
     *                      that this may be <code>null</code>, which indicates that the view should
     *                      be cleared
     * @return The actual class that the view is displaying after it has been updated
     *         (may be <code>null</code>)
     */
    protected OWLClass updateView(OWLClass selectedClass) {
        list.setRootObject(selectedClass);
        return selectedClass;
    }


    public void disposeView() {
        list.dispose();
    }
}
