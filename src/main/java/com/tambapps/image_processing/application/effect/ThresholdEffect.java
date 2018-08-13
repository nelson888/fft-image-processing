package com.tambapps.image_processing.application.effect;

import com.tambapps.math.fourier.filtering.Filter;
import com.tambapps.math.fourier.filtering.Filters;

public class ThresholdEffect extends AbstractEffect {

    private double max;

    //TODO TROUVER UN MOYEN DE CALCULER LE MAX
    @Override
    Filter getFilter(int M, int N, double value) {
        return Filters.threshold(percentageValue(value, max));
    }

    @Override
    String name() {
        return "threshold";
    }

}
