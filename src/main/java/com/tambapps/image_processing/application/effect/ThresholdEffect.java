package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThresholdEffect extends AbstractEffect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThresholdEffect.class);

    private double max;

    @Override
    public void setTransform(ImageHolder transform) {
        super.setTransform(transform);

        max = 0;

        Complex2DArray[] channels = transform.getChannels();
        for (Complex2DArray array : channels) {
            for (int i = 0; i < array.getM() * array.getN(); i++) {
                Complex c = array.get(i);
                double abs = c.abs();
                if (max < abs) {
                    max = abs;
                }
                if (abs >= Double.parseDouble("1.0E7")) {
                    System.out.println(abs);
                }
            }
        }
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        double threshold = max  * (100d - value);
        LOGGER.info("Applying {} with max value of {}", this, threshold);
        return Filters.threshold(threshold);
    }

    @Override
    String name() {
        return "threshold";
    }

    @Override
    public String toString() {
        return name() + " effect";
    }
}
