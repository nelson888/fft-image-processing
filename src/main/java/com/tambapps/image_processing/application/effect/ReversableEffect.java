package com.tambapps.image_processing.application.effect;

abstract class ReversableEffect extends AbstractEffect {

  private final String name;
  final boolean reversed;

  ReversableEffect(boolean reversed, String name) {
    this.reversed = reversed;
    this.name = name;
  }

  @Override
  double percentageValue(double percentage, double max) {
    return (reversed ? super.percentageValue(100 - percentage, max) : super.percentageValue(percentage, max)) / 2d;
  }

  @Override
  String name() {
    return name;
  }
}
