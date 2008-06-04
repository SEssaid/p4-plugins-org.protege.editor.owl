package org.protege.editor.owl.ui.view;

import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.OWLObjectComparator;
import org.protege.editor.owl.ui.action.OWLObjectHierarchyDeleter;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.semanticweb.owl.model.OWLProperty;
import org.semanticweb.owl.util.OWLEntitySetProvider;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: nickdrummond
 * Date: May 23, 2008
 */
public abstract class AbstractOWLPropertyHierarchyViewComponent<O extends OWLProperty> extends AbstractOWLPropertyViewComponent<O>
        implements Findable<O>, Deleteable {

    private OWLModelManagerTree<O> tree;

    public void initialiseView() throws Exception {
        setLayout(new BorderLayout());

        tree = new OWLModelManagerTree<O>(getOWLEditorKit(), getHierarchyProvider());
        tree.setOWLObjectComparator(new OWLObjectComparator<O>(getOWLModelManager()));
        add(new JScrollPane(tree), BorderLayout.CENTER);

        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                transmitSelection();
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                transmitSelection();
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                transmitSelection();
            }
        });

        performExtraInitialisation();
    }

    private void transmitSelection() {
        O prop = tree.getSelectedOWLObject();
        setSelectedEntity(prop);
        mediator.fireStateChanged(this);
    }

    public O getSelectedProperty() {
        return tree.getSelectedOWLObject();
    }

    public Set<O> getSelectedProperties() {
        return new HashSet<O>(tree.getSelectedOWLObjects());
    }

    protected O updateView(O property) {
        tree.setSelectedOWLObject(property);
        return property;
    }


    public void disposeView() {
        tree.dispose();
    }

    protected OWLModelManagerTree<O> getTree() {
        return tree;
    }

    protected abstract void performExtraInitialisation() throws Exception;

    protected abstract OWLObjectHierarchyProvider<O> getHierarchyProvider();


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Findable
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public java.util.List<O> find(String match) {
        if (isOWLDataPropertyView()){
            return new ArrayList<O>((Set<O>)getOWLModelManager().getEntityFinder().getMatchingOWLDataProperties(match));
        }
        else if (isOWLObjectPropertyView()){
            return new ArrayList<O>((Set<O>)getOWLModelManager().getEntityFinder().getMatchingOWLObjectProperties(match));
        }
        return Collections.emptyList();
    }


    public void show(O property) {
        tree.setSelectedOWLObject(property);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Deletable
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    private ChangeListenerMediator mediator = new ChangeListenerMediator();


    public boolean canDelete() {
        return !getSelectedProperties().isEmpty();
    }


    public void handleDelete() {
        OWLObjectHierarchyDeleter<O> deleter = new OWLObjectHierarchyDeleter<O>(
                getOWLEditorKit(),
                getHierarchyProvider(),
                new OWLEntitySetProvider<O>() {
                    public Set<O> getEntities() {
                        return getSelectedProperties();
                    }
                },
                "properties");

        deleter.performDeletion();
    }


    public void addChangeListener(ChangeListener listener) {
        mediator.addChangeListener(listener);
    }


    public void removeChangeListener(ChangeListener listener) {
        mediator.removeChangeListener(listener);
    }
}
