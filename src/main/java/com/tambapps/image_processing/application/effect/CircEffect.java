package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;

public class CircEffect extends ReversableEffect {
    private final String name;

    public CircEffect(boolean reversed, String name) {
        super(reversed);
        this.name = name;
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
