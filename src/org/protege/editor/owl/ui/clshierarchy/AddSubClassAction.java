package org.protege.editor.owl.ui.clshierarchy;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLWorkspace;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.tree.OWLObjectTree;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLSubClassAxiom;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 3, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AddSubClassAction extends AbstractOWLClassTreeAction {

    private OWLEditorKit owlEditorKit;

    private OWLObjectTree<OWLClass> tree;


    public AddSubClassAction(OWLEditorKit owlEditorKit, OWLObjectTree<OWLClass> tree) {
        super("Add subclass", OWLIcons.getIcon("class.add.sub.png"), tree.getSelectionModel());
        this.owlEditorKit = owlEditorKit;
        this.tree = tree;
        setEnabled(false);
        putValue(AbstractAction.ACCELERATOR_KEY, OWLWorkspace.getCreateSubKeyStroke());
    }


    protected boolean canPerform(OWLClass cls) {
        return cls != null;
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        OWLClass cls = getSelectedOWLClass();
        if (cls == null) {
            return;
        }
        OWLEntityCreationSet<OWLClass> creationSet = owlEditorKit.getOWLWorkspace().createOWLClass();
        if (creationSet == null) {
            return;
        }
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.addAll(creationSet.getOntologyChanges());
        OWLDataFactory df = owlEditorKit.getOWLModelManager().getOWLDataFactory();
        OWLSubClassAxiom ax = df.getOWLSubClassAxiom(creationSet.getOWLEntity(), cls);
        changes.add(new AddAxiom(owlEditorKit.getOWLModelManager().getActiveOntology(), ax));
        owlEditorKit.getOWLModelManager().applyChanges(changes);
        tree.setSelectedOWLObject(creationSet.getOWLEntity());
    }
}
