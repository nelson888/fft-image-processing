package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.complex.Complex;
import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

//TODO STILL DOESN'T WORK AND LOG EXCEPTIONS ON FFT2D
//todo change version of javaFX
public class ThresholdEffect extends AbstractEffect {

    private double max;
    private double min;

    @Override
    public void setTransform(ImageHolder transform) {
        super.setTransform(transform);
        max = Double.MIN_VALUE;
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
        min -= 1;
    }

    @Override
    Filter getFilter(int M, int N, double value) {
        return Filters.threshold(min + (max - min) * value);
    }

    @Override
    String name() {
        return "threshold";
    }
}
