package org.protege.editor.owl.ui.prefix;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.protege.editor.owl.ui.OWLIcons;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 22-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AddPrefixMappingAction extends AbstractAction {

    private PrefixMapperTable table;


    public AddPrefixMappingAction(PrefixMapperTable table) {
        super("Add prefix", OWLIcons.getIcon("prefix.add.png"));
        putValue(AbstractAction.SHORT_DESCRIPTION, "Add prefix mapping");
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        table.createAndEditRow();
    }
}
