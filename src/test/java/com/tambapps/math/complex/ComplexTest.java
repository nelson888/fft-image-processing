package com.tambapps.math.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComplexTest {

  @Test
  public void addTest() {
    Complex complex1 = Complex.of(8);
    Complex complex2 = Complex.of(4, 1);

    assertEquals("Should be equal", Complex.of(12, 1), complex1.add(complex2));
  }

  @Test
  public void pureTest() {
    Complex pr = Complex.of(3423);
    Complex pi = Complex.of(0, 234);

    assertTrue("Should be only pure real", pr.isPureReal() && !pr.isPureImaginary());
    assertTrue("Should be only pure imaginary", pi.isPureImaginary() && !pi.isPureReal());
  }

  @Test
  public void mulTest() {
    Complex c1 = Complex.ONE;
    Complex c2 = Complex.of(0, 2);

    assertEquals("Should be equal", Complex.ZERO, c1.mul(Complex.ZERO));
    assertEquals("Should be equal", Complex.ZERO, Complex.ZERO.mul(c1));

    assertEquals("Should be equal", Complex.I.scl(2), c1.mul(c2));
    assertEquals("Should be equal", Complex.I.scl(2), c2.mul(c1));
  }

}
