package org.protege.editor.owl.ui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import org.protege.editor.owl.ui.frame.OWLClassDescriptionFrame;
import org.protege.editor.owl.ui.framelist.OWLFrameList2;
import org.protege.editor.owl.ui.framelist.OWLFrameListPopupMenuAction;
import org.semanticweb.owl.model.OWLClass;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 04-Feb-2007<br><br>
 */
public class OWLClassDescriptionViewComponent extends AbstractOWLClassViewComponent {

    private OWLFrameList2<OWLClass> list;

//    private OWLFrameListComponent<OWLClass> frameListComponent;


    public void initialiseClassView() throws Exception {
//        frameListComponent = new OWLFrameListComponent<OWLClass>(getOWLEditorKit(), new OWLClassDescriptionFrame(getOWLEditorKit()));
        list = new OWLFrameList2<OWLClass>(getOWLEditorKit(), new OWLClassDescriptionFrame(getOWLEditorKit()));
        setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(list);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(sp);
        list.addToPopupMenu(new ConvertSelectionToEquivalentClassAction());
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
//        list.dispose();
        list.dispose();
    }


    private class ConvertSelectionToEquivalentClassAction extends OWLFrameListPopupMenuAction {

        protected void initialise() throws Exception {
        }


        protected void dispose() throws Exception {
        }


        protected String getName() {
            return "Convert selected rows to defined class";
        }


        protected void updateState() {
//            if (list.getSelectedValue() == null) {
//                setEnabled(false);
//            }
//            for (Object selVal : list.getSelectedValues()) {
//                if (!(selVal instanceof OWLSubClassAxiomFrameSectionRow)) {
//                    setEnabled(false);
//                    return;
//                }
//            }
//            setEnabled(true);
        }


        public void actionPerformed(ActionEvent e) {
            convertSelectedRowsToDefinedClass();
        }
    }


    private void convertSelectedRowsToDefinedClass() {
//        Set<OWLDescription> descriptions = new HashSet<OWLDescription>();
//        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
//        for (Object selVal : list.getSelectedValues()) {
//            if (selVal instanceof OWLSubClassAxiomFrameSectionRow) {
//                OWLSubClassAxiomFrameSectionRow row = (OWLSubClassAxiomFrameSectionRow) selVal;
//                changes.add(new RemoveAxiom(row.getOntology(), row.getAxiom()));
//                descriptions.add(row.getAxiom().getSuperClass());
//            }
//        }
//        OWLDescription equivalentClass;
//        if (descriptions.size() == 1) {
//            equivalentClass = descriptions.iterator().next();
//        }
//        else {
//            equivalentClass = getOWLDataFactory().getOWLObjectIntersectionOf(descriptions);
//        }
//        Set<OWLDescription> axiomOperands = CollectionFactory.createSet(list.getRootObject(), equivalentClass);
//        changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
//                                 getOWLDataFactory().getOWLEquivalentClassesAxiom(axiomOperands)));
//        getOWLModelManager().applyChanges(changes);
    }
}
