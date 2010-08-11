package org.protege.editor.owl.ui.prefix;

import org.apache.log4j.Logger;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.UIHelper;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 22-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GeneratePrefixFromOntologyAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(GeneratePrefixFromOntologyAction.class);


    private OWLEditorKit owlEditorKit;

    private PrefixMappingPanel panel;


    public GeneratePrefixFromOntologyAction(OWLEditorKit owlEditorKit, PrefixMappingPanel panel) {
        super("Generate from ontology URI", OWLIcons.getIcon("prefix.generate.png"));
        putValue(AbstractAction.SHORT_DESCRIPTION, "Generate prefix mappings from ontology URIs...");
        this.owlEditorKit = owlEditorKit;
        this.panel = panel;
    }


    public void actionPerformed(ActionEvent e) {
        UIHelper uiHelper = new UIHelper(owlEditorKit);
        Set<OWLOntology> ontologies = uiHelper.pickOWLOntologies();
        for (OWLOntology ont : ontologies) {
            // @@TODO what about anonymous ontologies?
            String uriString = ont.getOntologyID().getDefaultDocumentIRI().toString();
            String prefix;
            if (uriString.endsWith("/")) {
                String sub = uriString.substring(0, uriString.length() - 1);
                prefix = sub.substring(sub.lastIndexOf("/") + 1, sub.length());
            }
            else {
                prefix = uriString.substring(uriString.lastIndexOf('/') + 1, uriString.length());
            }
            if (prefix.endsWith(".owl")) {
                prefix = prefix.substring(0, prefix.length() - 4);
            }
            prefix = prefix.toLowerCase();
            if (!uriString.endsWith("#") && !uriString.endsWith("/")) {
                uriString = uriString + "#";
            }
            PrefixMapperTable table = panel.getCurrentPrefixMapperTable();
            int index = table.getPrefixMapperTableModel().addMapping(prefix, uriString);
            table.getSelectionModel().setSelectionInterval(index, index);
        }
    }
}
