package org.protege.editor.owl.model.inference;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.protege.editor.core.ProtegeApplication;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NullReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 19-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLReasonerManagerImpl implements OWLReasonerManager {
    private static Logger logger = Logger.getLogger(OWLReasonerManager.class);
    
    private OWLModelManager owlModelManager;
    
    private ReasonerPreferences preferences;
    
    private Set<ProtegeOWLReasonerFactory> reasonerFactories;

    private ProtegeOWLReasonerFactory currentReasonerFactory;

    private Map<OWLOntology, OWLReasoner> reasonerMap = new HashMap<OWLOntology, OWLReasoner>();
    
    private OWLReasoner runningReasoner;
    private boolean classificationInProgress = false;
    
    private ReasonerProgressMonitor reasonerProgressMonitor;
    private OWLReasonerExceptionHandler exceptionHandler;
    

    public OWLReasonerManagerImpl(OWLModelManager owlModelManager) {
        this.owlModelManager = owlModelManager;
        preferences = new ReasonerPreferences();
        preferences.load();
        reasonerFactories = new HashSet<ProtegeOWLReasonerFactory>();
        reasonerProgressMonitor = new NullReasonerProgressMonitor();
        installFactories();
        exceptionHandler = new DefaultOWLReasonerExceptionHandler();
    }


    public void setReasonerExceptionHandler(OWLReasonerExceptionHandler handler) {
        if(handler != null) {
            exceptionHandler = handler;
        }
        else {
            exceptionHandler = new DefaultOWLReasonerExceptionHandler();
        }
    }


    public void dispose() {
        if (preferences != null) {
            preferences.save();
        }
        clearAndDisposeReasoners();
    }
    
    private void clearAndDisposeReasoners() {
        for (OWLReasoner reasoner : reasonerMap.values()) {
            if (reasoner != null) {
                try {
                    reasoner.dispose();
                }
                catch (Throwable t) {
                    ProtegeApplication.getErrorLog().logError(t);
                }
            }
        }
        reasonerMap.clear();
    }


    public String getCurrentReasonerName() {
        return getCurrentReasoner().getReasonerName();
    }


    public ProtegeOWLReasonerFactory getCurrentReasonerFactory() {
        if (currentReasonerFactory == null) {
            currentReasonerFactory = new NoOpReasonerFactory();
        }
        return currentReasonerFactory;
    }


    public void setReasonerProgressMonitor(ReasonerProgressMonitor progressMonitor) {
        this.reasonerProgressMonitor = progressMonitor;
    }
    
    public ReasonerProgressMonitor getReasonerProgressMonitor() {
		return reasonerProgressMonitor;
	}


    public Set<ProtegeOWLReasonerFactory> getInstalledReasonerFactories() {
        return reasonerFactories;
    }


    private void installFactories() {        
        ProtegeOWLReasonerFactoryPluginLoader loader = new ProtegeOWLReasonerFactoryPluginLoader(owlModelManager);
        addReasonerFactories(loader.getPlugins());
        setCurrentReasonerFactoryId(preferences.getDefaultReasonerId());
    }

    public void addReasonerFactories(Set<ProtegeOWLReasonerFactoryPlugin> plugins) {
        for (ProtegeOWLReasonerFactoryPlugin plugin : plugins) {
            try {
                ProtegeOWLReasonerFactory factory = plugin.newInstance();
                factory.initialise();
                reasonerFactories.add(factory);
            }
            catch (Throwable t) {
                ProtegeApplication.getErrorLog().logError(t);
            }
        }
    }

    public String getCurrentReasonerFactoryId() {
        return getCurrentReasonerFactory().getReasonerId();
    }


    public void setCurrentReasonerFactoryId(String id) {
        if (getCurrentReasonerFactory().getReasonerId().equals(id)) {
            return;
        }
        for (ProtegeOWLReasonerFactory reasonerFactory : reasonerFactories) {
            if (reasonerFactory.getReasonerId().equals(id)) {
                preferences.setDefaultReasonerId(id);
                preferences.save();
                clearAndDisposeReasoners();
                currentReasonerFactory = reasonerFactory;
                owlModelManager.fireEvent(EventType.REASONER_CHANGED);
                return;
            }
        }
        throw new RuntimeException("Unknown reasoner ID");
    }


    public OWLReasoner getCurrentReasoner() {
        OWLReasoner reasoner;
        OWLOntology activeOntology = owlModelManager.getActiveOntology();
        synchronized (reasonerMap)  {
            reasoner = reasonerMap.get(activeOntology);
        }
        if (reasoner == null) {
            reasoner = new NoOpReasoner(activeOntology);
            synchronized (reasonerMap)  {
                reasonerMap.put(activeOntology, reasoner);
            }
        }
        return reasoner;
    }
    
    public boolean isClassificationInProgress() {
        synchronized (reasonerMap) {
            return classificationInProgress;
        }
    }
    
    public boolean isClassified() {
        synchronized (reasonerMap) {
            OWLReasoner reasoner = getCurrentReasoner();
            return !(reasoner instanceof NoOpReasoner) && 
            			(reasoner.getPendingChanges() == null || reasoner.getPendingChanges().isEmpty());
        }
    }

    /**
     * Classifies the current active ontologies.
     */
    public boolean classifyAsynchronously(Set<InferenceType> precompute) {
        if (getCurrentReasonerFactory() instanceof NoOpReasonerFactory) {
            return true;
        }
        final OWLOntology currentOntology = owlModelManager.getActiveOntology();
        synchronized (reasonerMap) {
            if (classificationInProgress) {
                return false;
            }
            runningReasoner = reasonerMap.get(currentOntology);
            reasonerMap.put(currentOntology, new NoOpReasoner(currentOntology));
            classificationInProgress = true;
        }
        owlModelManager.fireEvent(EventType.ABOUT_TO_CLASSIFY);
        Thread currentReasonerThread = new Thread(new ClassificationRunner(currentOntology, precompute), "Classification Thread");
        currentReasonerThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread thread, Throwable throwable) {
                exceptionHandler.handle(throwable);
                ProtegeApplication.getErrorLog().logError(throwable);
            }
        });
        currentReasonerThread.start();
        return true;
    }


    public void killCurrentClassification() {
        synchronized (reasonerMap) {
            if (runningReasoner != null){
                runningReasoner.interrupt();
            }
        }
    }
    
    public ReasonerPreferences getReasonerPreferences() {
        return preferences;
    }


    /**
     * Fires a reclassify event, ensuring that the event
     * is fired in the event dispatch thread.
     */
    private void fireReclassified() {
        Runnable r = new Runnable() {
            public void run() {
                owlModelManager.fireEvent(EventType.ONTOLOGY_CLASSIFIED);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        }
        else {
            SwingUtilities.invokeLater(r);
        }
    }
    
    private class ClassificationRunner implements Runnable {
        private OWLOntology ontology;
        private Set<InferenceType> precompute;
        private ProtegeOWLReasonerFactory currentReasonerFactory;
        
        public ClassificationRunner(OWLOntology ontology, Set<InferenceType> precompute) {
            this.ontology = ontology;
            this.precompute = EnumSet.noneOf(InferenceType.class);
            this.precompute.addAll(precompute);
            currentReasonerFactory = getCurrentReasonerFactory();
        }
        
        public void run() {
            try {
                long start = System.currentTimeMillis();
                if (runningReasoner instanceof NoOpReasoner) {
                    runningReasoner = null;
                }
                if (runningReasoner != null && !runningReasoner.getPendingChanges().isEmpty()) {
                    if (runningReasoner.getBufferingMode() == null 
                            || runningReasoner.getBufferingMode() == BufferingMode.NON_BUFFERING) {
                        runningReasoner.dispose();
                        runningReasoner = null;
                    }
                    else {
                        runningReasoner.flush();
                    }
                }
                if (runningReasoner == null) {
                    runningReasoner = currentReasonerFactory.createReasoner(ontology, reasonerProgressMonitor);
                    owlModelManager.fireEvent(EventType.REASONER_CHANGED);
                }
                Set<InferenceType> dontPrecompute  = EnumSet.noneOf(InferenceType.class);
                for (InferenceType type : precompute) {
                    if (runningReasoner.isPrecomputed(type)) {
                        dontPrecompute.add(type);
                    }
                }
                precompute.removeAll(dontPrecompute);
                if (!precompute.isEmpty()) {
                    runningReasoner.precomputeInferences(precompute.toArray(new InferenceType[precompute.size()]));

                    String s = currentReasonerFactory.getReasonerName() + " classified in " + (System.currentTimeMillis()-start) + "ms";
                    logger.info(s);
                }

            }
            finally{
                synchronized (reasonerMap) {
                    reasonerMap.put(ontology, runningReasoner);
                    runningReasoner = null;
                    classificationInProgress = false;
                }
                fireReclassified();
            }
        }
    }
    
}
