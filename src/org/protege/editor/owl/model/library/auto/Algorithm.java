package org.protege.editor.owl.model.library.auto;

import java.io.File;
import java.net.URI;
import java.util.Set;

public interface Algorithm {
    Set<URI> getSuggestions(File f);
}
