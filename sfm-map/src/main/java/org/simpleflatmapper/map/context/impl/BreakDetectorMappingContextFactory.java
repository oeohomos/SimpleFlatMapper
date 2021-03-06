package org.simpleflatmapper.map.context.impl;

import org.simpleflatmapper.map.BreakDetector;
import org.simpleflatmapper.map.MappingContext;
import org.simpleflatmapper.map.context.KeysDefinition;
import org.simpleflatmapper.map.context.MappingContextFactory;
import org.simpleflatmapper.util.Supplier;

import java.util.List;

public class BreakDetectorMappingContextFactory<S, K> implements MappingContextFactory<S> {
    private final KeysDefinition<S, K>[] keyDefinitions;
    private final int rootDetector;
    private final MappingContextFactory<S> delegateFactory;

    public BreakDetectorMappingContextFactory(KeysDefinition<S, K>[] keyDefinitions,
                                              int rootDetector,
                                              MappingContextFactory<S> delegateFactory) {
        this.keyDefinitions = keyDefinitions;
        this.rootDetector = rootDetector;
        this.delegateFactory = delegateFactory;
    }

    @Override
    public MappingContext<S> newContext() {
        return new BreakDetectorMappingContext<S>(newBreakDetectors(keyDefinitions), rootDetector, delegateFactory.newContext());
    }

    @SuppressWarnings("unchecked")
    private BreakDetector<S>[] newBreakDetectors(KeysDefinition<S, K>[] definitions) {
        if (definitions == null) return null;

        BreakDetector<S>[] breakDetectors = new BreakDetector[definitions.length];

        for (int i = 0; i < definitions.length; i++) {
            KeysDefinition<S, K> definition = definitions[i];
            if (definition != null) {
                breakDetectors[i] = newBreakDetector(definition, definition.getParentIndex() != -1 ? breakDetectors[definition.getParentIndex()] : null);
            }
        }


        return breakDetectors;
    }

    private BreakDetector<S> newBreakDetector(KeysDefinition<S, K> definition, BreakDetector<S> parent) {
        return new BreakDetectorImpl<S, K>(definition, parent);
    }
}
