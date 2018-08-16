package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircEffect extends ReversableEffect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircEffect.class);

    public CircEffect(boolean reversed, String name) {
        super(reversed, name);
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        int radius = percentageValue(value, Math.max(M, N));
        LOGGER.info("Applying {} of radius {}", this, radius);
        return Filters.circle(radius, reversed);
    }

}
