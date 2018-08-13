package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

public class ThresholdEffect extends AbstractEffect {

    private double max;
    private double min;

    @Override
    public void setTransform(ImageHolder transform) {
        super.setTransform(transform);
        max = Double.MAX_VALUE;
        min = Double.MAX_VALUE;

        Complex2DArray[] channels = transform.getChannels();
        for (Complex2DArray array : channels) {
            for (int i = 0; i < array.getM() * array.getN(); i++) {
                Complex c = array.get(i);
                double abs = c.abs();
                if (max < abs) {
                    max = abs;
                }
                if (min > abs) {
                    min = abs;
                }
            }
        }
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        return Filters.threshold(min + (max - min) * percentageValue(value, max));
    }

    @Override
    String name() {
        return "threshold";
    }

    @Override
    public double getMinValue() {
        return min;
    }

    @Override
    public double getMaxValue() {
        return max;
    }
}
