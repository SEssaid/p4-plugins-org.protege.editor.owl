package org.protege.editor.owl.ui.view;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.OWLIcons;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.util.OWLEntityRemover;
import org.semanticweb.owl.util.OWLEntitySetProvider;

import java.awt.event.ActionEvent;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 20-Apr-2007<br><br>
 */
public class DeleteIndividualAction extends OWLSelectionViewAction {

    private OWLEditorKit owlEditorKit;

    private OWLEntitySetProvider<OWLIndividual> indSetProvider;


    public DeleteIndividualAction(OWLEditorKit owlEditorKit, OWLEntitySetProvider<OWLIndividual> indSetProvider) {
        super("Delete individual(s)", OWLIcons.getIcon("individual.delete.png"));
        this.owlEditorKit = owlEditorKit;
        this.indSetProvider = indSetProvider;
    }


    public void updateState() {
        setEnabled(!indSetProvider.getEntities().isEmpty());
    }


    public void dispose() {
    }


    public void actionPerformed(ActionEvent e) {
        OWLEntityRemover remover = new OWLEntityRemover(owlEditorKit.getModelManager().getOWLOntologyManager(),
                                                        owlEditorKit.getModelManager().getOntologies());
        for (OWLIndividual ind : indSetProvider.getEntities()) {
            ind.accept(remover);
        }
        owlEditorKit.getModelManager().applyChanges(remover.getChanges());
    }
}
