package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

public class CircEffect extends ReversableEffect {

    public CircEffect(boolean reversed, String name) {
        super(reversed, name);
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        return Filters.circle(percentageValue(value, Math.max(M, N)), reversed);
    }
}
