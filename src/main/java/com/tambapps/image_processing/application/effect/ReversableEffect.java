package com.tambapps.image_processing.application.effect;

abstract class ReversableEffect extends AbstractEffect {

    final boolean reversed;

    ReversableEffect(boolean reversed) {
        this.reversed = reversed;
    }

    @Override
    double percentageValue(double percentage, double max) {
        return (reversed ? super.percentageValue(100 - percentage, max) : super.percentageValue(percentage, max)) / 2d;
    }
}
