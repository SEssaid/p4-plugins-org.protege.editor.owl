package org.protege.editor.owl.ui.renderer;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.protege.editor.core.ui.error.ErrorLogPanel;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 19-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLRendererPreferences {


    public static final String RENDER_HYPERLINKS = "RENDER_HYPERLINKS";

    public static final String HIGHLIGHT_ACTIVE_ONTOLOGY_STATEMENTS = "HIGHLIGHT_ACTIVE_ONTOLOGY_STATEMENTS";

    public static final String HIGHLIGHT_CHANGED_ENTITIES = "HIGHLIGHT_CHANGED_ENTITIES";

    public static final String HIGHLIGHT_KEY_WORDS = "HIGHLIGHT_KEY_WORDS";

    public static final String RENDERER_CLASS = "RENDERER_CLASS";

    public static final String USE_THAT_KEYWORD = "USE_THAT_KEYWORD";

    public static final String RENDER_DOMAIN_AXIOMS_AS_GCIS = "RENDER_DOMAIN_AXIOMS_AS_GCIS";

    public static final String RENDER_PREFIXES = "RENDER_PREFIXES";

    public static final String FONT_SIZE = "FONT_SIZE";

    public static final String FONT_NAME = "FONT_NAME";

    public static final String ANNOTATIONS = "ANNOTATIONS";

    public static final int DEFAULT_FONT_SIZE = 14;

    public static final String DEFAULT_FONT_NAME = "Dialog";

    public static final String ANY_LANGUAGE = "!";

    private static OWLRendererPreferences instance;

    private boolean renderHyperlinks;

    private boolean highlightActiveOntologyStatements;

    private boolean highlightChangedEntities;

    private boolean highlightKeyWords;

    private boolean useThatKeyword;

    private boolean renderPrefixes;

    private String rendererClass;

    private boolean renderDomainAxiomsAsGCIs;

    private int fontSize;

    private String fontName = DEFAULT_FONT_NAME;

    private Font font;

    private List<IRI> annotationIRIS;

    private Map<IRI, List<String>> annotationLanguages;



    public Font getFont() {
        return font;
    }


    public String getFontName() {
        return fontName;
    }


    public void setFontName(String fontName) {
        this.fontName = fontName;
        getPreferences().putString(FONT_NAME, fontName);
        resetFont();

    }

    private void resetFont() {
        font = new Font(this.fontName, Font.PLAIN, fontSize);
    }



    public int getFontSize() {
        return fontSize;
    }


    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        getPreferences().putInt(FONT_SIZE, fontSize);
        resetFont();
    }


    public void setAnnotations(List<IRI> iris, Map<IRI, List<String>> langMap){
        annotationIRIS = iris;
        annotationLanguages = langMap;
        List<String> values = new ArrayList<String>();

        for (IRI iri : iris){
            StringBuilder str = new StringBuilder(iri.toString());
            final List<String> langs = langMap.get(iri);
            if (langs != null){
                for (String lang : langs) {
                    if (lang == null){
                        lang = ANY_LANGUAGE;
                    }
                    str.append(", ").append(lang);
                }
            }
            values.add(str.toString());
        }
        getPreferences().putStringList(ANNOTATIONS, values);
    }


    public List<IRI> getAnnotationIRIs(){
        return new ArrayList<IRI>(annotationIRIS);
    }


    public List<String> getAnnotationLangs(IRI iri){
        final List<String> langs = annotationLanguages.get(iri);
        if (langs != null){
            return new ArrayList<String>(langs);
        }
        else{
            return Collections.emptyList();
        }
    }

    public Map<IRI, List<String>> getAnnotationLangs(){
        return annotationLanguages;
    }

    private OWLRendererPreferences() {
        rendererClass = OWLEntityRendererImpl.class.getName();
        load();
    }


    public static synchronized OWLRendererPreferences getInstance() {
        if (instance == null) {
            instance = new OWLRendererPreferences();
        }
        return instance;
    }


    private Preferences getPreferences() {
        return PreferencesManager.getInstance().getApplicationPreferences(getClass());
    }


    private void load() {
        Preferences p = getPreferences();
        renderHyperlinks = p.getBoolean(RENDER_HYPERLINKS, true);
        highlightActiveOntologyStatements = p.getBoolean(HIGHLIGHT_ACTIVE_ONTOLOGY_STATEMENTS, true);
        highlightChangedEntities = p.getBoolean(HIGHLIGHT_CHANGED_ENTITIES, false);
        highlightKeyWords = p.getBoolean(HIGHLIGHT_KEY_WORDS, true);
        useThatKeyword = p.getBoolean(USE_THAT_KEYWORD, false);
        rendererClass = p.getString(RENDERER_CLASS, OWLEntityRendererImpl.class.getName());
        renderDomainAxiomsAsGCIs = false; p.putBoolean(RENDER_DOMAIN_AXIOMS_AS_GCIS, false);
        renderPrefixes = p.getBoolean(RENDER_PREFIXES, false);
        fontSize = p.getInt(FONT_SIZE, DEFAULT_FONT_SIZE);
        fontName = p.getString(FONT_NAME, DEFAULT_FONT_NAME);
        loadAnnotations();
        resetFont();
    }


    private void loadAnnotations() {
        annotationIRIS = new ArrayList<IRI>();
        annotationLanguages = new HashMap<IRI, List<String>>();
        final List<String> defaultValues = Collections.emptyList();
        List<String> values = getPreferences().getStringList(ANNOTATIONS, defaultValues);

        if (values.equals(defaultValues)){
            annotationIRIS.add(IRI.create(OWLRDFVocabulary.RDFS_LABEL.getURI()));
            annotationIRIS.add(IRI.create("http://www.w3.org/2004/02/skos/core#prefLabel"));
        }
        else{
            for (String value : values){
                String[] tokens = value.split(",");
                try {
                    IRI iri = IRI.create(new URI(tokens[0].trim()));
                    List<String> langs = new ArrayList<String>();
                    for (int i=1; i<tokens.length; i++){
                        String token = tokens[i].trim();
                        if (token.equals(ANY_LANGUAGE)){
                            token = null; // OWL API treats this as "no language set"
                        }
                        langs.add(token);
                    }
                    annotationIRIS.add(iri);
                    annotationLanguages.put(iri, langs);
                }
                catch (URISyntaxException e) {
                    ErrorLogPanel.showErrorDialog(e);
                }
            }
        }
    }


    public void reset() {
        renderHyperlinks = true;
        highlightActiveOntologyStatements = true;
        highlightChangedEntities = false;
        highlightKeyWords = true;
        useThatKeyword = false;
        fontSize = DEFAULT_FONT_SIZE;
    }


    public boolean isRenderHyperlinks() {
        return renderHyperlinks;
    }


    public String getRendererClass() {
        return rendererClass;
    }


    public void setRendererClass(String rendererClass) {
        this.rendererClass = rendererClass;
        getPreferences().putString(RENDERER_CLASS, rendererClass);
    }


    public boolean isUseThatKeyword() {
        return useThatKeyword;
    }


    public void setUseThatKeyword(boolean useThatKeyword) {
        this.useThatKeyword = useThatKeyword;
        getPreferences().putBoolean(USE_THAT_KEYWORD, useThatKeyword);
    }


    public void setRenderHyperlinks(boolean renderHyperlinks) {
        this.renderHyperlinks = renderHyperlinks;
        getPreferences().putBoolean(RENDER_HYPERLINKS, renderHyperlinks);
    }


    public boolean isHighlightActiveOntologyStatements() {
        return highlightActiveOntologyStatements;
    }


    public void setHighlightActiveOntologyStatements(boolean highlightActiveOntologyStatements) {
        this.highlightActiveOntologyStatements = highlightActiveOntologyStatements;
        getPreferences().putBoolean(HIGHLIGHT_ACTIVE_ONTOLOGY_STATEMENTS, highlightActiveOntologyStatements);
    }


    public boolean isHighlightChangedEntities() {
        return highlightChangedEntities;
    }


    public void setHighlightChangedEntities(boolean highlightChangedEntities) {
        this.highlightChangedEntities = highlightChangedEntities;
        getPreferences().putBoolean(HIGHLIGHT_CHANGED_ENTITIES, highlightChangedEntities);
    }


    public boolean isHighlightKeyWords() {
        return highlightKeyWords;
    }


    public void setHighlightKeyWords(boolean highlightKeyWords) {
        this.highlightKeyWords = highlightKeyWords;
        getPreferences().putBoolean(HIGHLIGHT_KEY_WORDS, highlightKeyWords);
    }

    public void setRenderDomainAxiomsAsGCIs(boolean b) {
        this.renderDomainAxiomsAsGCIs = b;
        getPreferences().putBoolean(RENDER_DOMAIN_AXIOMS_AS_GCIS, b);
    }

    public boolean isRenderDomainAxiomsAsGCIs() {
        return renderDomainAxiomsAsGCIs;
    }

    public boolean isRenderPrefixes() {
        return renderPrefixes;
    }

    public void setRenderPrefixes(boolean b){
        this.renderPrefixes = b;
        getPreferences().putBoolean(RENDER_PREFIXES, b);
    }
}
