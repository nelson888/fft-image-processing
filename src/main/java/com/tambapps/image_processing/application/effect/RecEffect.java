package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

public class RecEffect extends ReversableEffect {

    private final String name;

    public RecEffect(boolean reversed, String name) {
        super(reversed);
        this.name = name;
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        return Filters.rectangle(percentageValue(value, M), percentageValue(value, N), reversed);
    }

    @Override
    String name() {
        return name;
    }
}
