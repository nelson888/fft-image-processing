package com.tambapps.image_processing.application.effect;

import com.tambapps.image_processing.application.model.ImageHolder;
import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.filtering.Filter;

public abstract class AbstractEffect implements Effect {

    private ImageHolder transform;
    private ImageHolder result;

    @Override
    public void setTransform(ImageHolder transform) {
        this.transform = transform;
        this.result = null;
    }

    @Override
    public final void apply(double value) {
        Filter filter = getFilter(transform.getM(), transform.getN(), value);
        for (Complex2DArray channel : result.getChannels()) {
            filter.apply(channel);
        }
        result.computeImage(transform.getImage().getType());
    }

    @Override
    public String toString() {
        return name() + " effect";
    }

    @Override
    public ImageHolder getResult() {
        return result;
    }

    int percentageValue(double percentage, int max) {
        return (int) (percentage * max / 100);
    }


    double percentageValue(double percentage, double max) {
        return percentage * max / 100d;
    }

    ImageHolder getTransform() {
        return transform;
    }

    abstract Filter getFilter(int M, int N, double value);
    abstract String name();
}
