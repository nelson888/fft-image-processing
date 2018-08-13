package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;

public interface Effect {

    Effect NONE = new Effect() {
        @Override
        public void apply(double value) {

        }

        @Override
        public ImageHolder getResult() {
            return null;
        }

        @Override
        public void setTransform(ImageHolder transform) {

        }

        @Override
        public double getMaxValue() {
            return 0;
        }

        @Override
        public double getMinValue() {
            return 0;
        }

        @Override
        public String toString() {
            return "no effect";
        }
    };

    void apply(double value);

    ImageHolder getResult();

    void setTransform(ImageHolder transform);

    double getMaxValue();
    double getMinValue();
}
