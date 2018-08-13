package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

public class RecEffect extends AbstractEffect {

    private final String name;
    private final boolean inverted;

    public RecEffect(boolean inverted, String name) {
        this.name = name;
        this.inverted = inverted;
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        return Filters.rectangle(percentageValue(value, M), percentageValue(value, N), inverted);
    }

    @Override
    String name() {
        return name;
    }
}
