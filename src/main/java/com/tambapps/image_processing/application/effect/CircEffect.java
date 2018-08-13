package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;

public class CircEffect extends AbstractEffect {
    private final String name;
    private final boolean inverted;

    public CircEffect(boolean inverted, String name) {
        this.name = name;
        this.inverted = inverted;
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        //TODO
        throw new RuntimeException("not implemented yet");
    }

    @Override
    String name() {
        return name;
    }
}
