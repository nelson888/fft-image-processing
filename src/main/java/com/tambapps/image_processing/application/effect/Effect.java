package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.fourier.filtering.Filter;

public interface Effect {

    Effect NONE = new AbstractEffect() {


        @Override
        public String toString() {
            return name();
        }


        @Override
        Filter getFilter(int M, int N, double value) {
            return null;
        }

        @Override
        String name() {
            return "none";
        }
    };

    void apply(double value);

    ImageHolder getResult();

    void setTransform(ImageHolder transform);
}
