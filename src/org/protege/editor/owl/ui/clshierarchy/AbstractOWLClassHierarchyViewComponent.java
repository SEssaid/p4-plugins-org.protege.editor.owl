package org.protege.editor.owl.ui.clshierarchy;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.core.ui.view.View;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.OWLObjectComparatorAdapter;
import org.protege.editor.owl.ui.action.OWLObjectHierarchyDeleter;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.protege.editor.owl.ui.tree.OWLObjectTree;
import org.protege.editor.owl.ui.view.AbstractOWLClassViewComponent;
import org.protege.editor.owl.ui.view.ChangeListenerMediator;
import org.protege.editor.owl.ui.view.Deleteable;
import org.protege.editor.owl.ui.view.Findable;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.util.OWLEntitySetProvider;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;

import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 19-May-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractOWLClassHierarchyViewComponent extends AbstractOWLClassViewComponent implements Findable, Deleteable {

    private OWLObjectTree<OWLClass> tree;

    private TreeSelectionListener listener;

    private OWLObjectHierarchyDeleter<OWLClass> hierarchyDeleter;


    final public void initialiseClassView() throws Exception {
        setLayout(new BorderLayout(7, 7));
        
        tree = new OWLModelManagerTree<OWLClass>(getOWLEditorKit(), getOWLClassHierarchyProvider());

        // ordering based on default, but putting Nothing at the top
        final Comparator<OWLClass> comp = getOWLModelManager().getOWLObjectComparator();
        tree.setOWLObjectComparator(new OWLObjectComparatorAdapter<OWLClass>(comp) {
            public int compare(OWLClass o1, OWLClass o2) {
                if (isNothing(o1)) {
                    return -1;
                }
                else if (isNothing(o2)) {
                    return 1;
                }
                else {
                    return super.compare(o1, o2);
                }
            }
        });

        // render keywords should be on now for class expressions
        final TreeCellRenderer treeCellRenderer = tree.getCellRenderer();
        if (treeCellRenderer instanceof OWLCellRenderer){
            ((OWLCellRenderer) treeCellRenderer).setHighlightKeywords(true);
        }

        initSelectionManagement();
        add(ComponentFactory.createScrollPane(tree));
        performExtraInitialisation();
        OWLClass cls = getSelectedOWLClass();
        if (cls != null) {
            setSelectedClass(cls);
        }
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                transmitSelection();
            }
        });
        hierarchyDeleter = new OWLObjectHierarchyDeleter<OWLClass>(getOWLEditorKit(),
                                                                   getOWLClassHierarchyProvider(),
                                                                   new OWLEntitySetProvider<OWLClass>() {
                                                                       public Set<OWLClass> getEntities() {
                                                                           return new HashSet<OWLClass>(tree.getSelectedOWLObjects());
                                                                       }
                                                                   },
                                                                   "classes");
    }

    public void setSelectedClass(OWLClass cls) {
        tree.setSelectedOWLObject(cls);
    }


    public void setSelectedClasses(Set<OWLClass> clses) {
        tree.setSelectedOWLObjects(clses);
    }

    
    public OWLClass getSelectedClass() {
        return tree.getSelectedOWLObject();
    }


    public Set<OWLClass> getSelectedClasses() {
        return new HashSet<OWLClass>(tree.getSelectedOWLObjects());
    }


    private boolean isNothing(OWLClass o1) {
        return o1.getURI().equals(OWLRDFVocabulary.OWL_NOTHING.getURI());
    }


    public boolean requestFocusInWindow() {
        return tree.requestFocusInWindow();
    }


    protected OWLObjectTree<OWLClass> getTree() {
        return tree;
    }


    protected abstract void performExtraInitialisation() throws Exception;


    private void initSelectionManagement() {
        // Hook up a selection listener so that we can transmit our
        // selection to the main selection model

        listener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                transmitSelection();
            }
        };
        tree.addTreeSelectionListener(listener);
    }


    protected void transmitSelection() {
        deletableChangeListenerMediator.fireStateChanged(this);

        OWLClass selCls = getSelectedClass();
        if (selCls != null) {
            final View view = getView();
            if (view != null && !view.isPinned()){
                view.setPinned(true); // so that we don't follow the selection
                setSelectedEntity(selCls);
                view.setPinned(false);
            }
            else{
                setSelectedEntity(selCls);
            }
        }

        updateHeader(selCls);
    }


    protected OWLClass updateView(OWLClass selectedClass) {
        if (tree.getSelectedOWLObject() == null) {
            if (selectedClass != null) {
                tree.setSelectedOWLObject(selectedClass);
            }
            else {
                // Don't need to do anything - both null
            }
        }
        else {
            if (!tree.getSelectedOWLObject().equals(selectedClass)) {
                tree.setSelectedOWLObject(selectedClass);
            }
        }

        return selectedClass;
    }


    protected abstract OWLObjectHierarchyProvider<OWLClass> getOWLClassHierarchyProvider();


    public void disposeView() {
        // Dispose of the tree selection listener
        if (tree != null) {
            tree.removeTreeSelectionListener(listener);
            tree.dispose();
        }
    }


    protected OWLObject getObjectToCopy() {
        return tree.getSelectedOWLObject();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //
    // Implementation of Deleteable
    //
    /////////////////////////////////////////////////////////////////////////////////////

    private ChangeListenerMediator deletableChangeListenerMediator = new ChangeListenerMediator();


    public void addChangeListener(ChangeListener listener) {
        deletableChangeListenerMediator.addChangeListener(listener);
    }


    public void removeChangeListener(ChangeListener listener) {
        deletableChangeListenerMediator.removeChangeListener(listener);
    }


    public void handleDelete() {
        hierarchyDeleter.performDeletion();
    }


    public boolean canDelete() {
        return !tree.getSelectedOWLObjects().isEmpty();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //
    // Implementation of Findable
    //
    /////////////////////////////////////////////////////////////////////////////////////


    public List<OWLClass> find(String match) {
        return new ArrayList<OWLClass>(getOWLModelManager().getEntityFinder().getMatchingOWLClasses(match));
    }


    public void show(OWLEntity owlEntity) {
        getTree().setSelectedOWLObject((OWLClass) owlEntity);
    }
}
