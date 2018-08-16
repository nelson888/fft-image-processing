package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecEffect extends ReversableEffect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecEffect.class);

    public RecEffect(boolean reversed, String name) {
        super(reversed, name);
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        int m = percentageValue(value, M);
        int n = percentageValue(value, N);
        LOGGER.info("Applying {} of width {} and height {}", this, m, n);
        return Filters.rectangle(m, n, reversed);
    }

    @Override
    public String toString() {
        return reversed ? "Reversed Rectangle effect" : "Rectangle effect";
    }
}
