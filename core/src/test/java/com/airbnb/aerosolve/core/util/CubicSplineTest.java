package com.airbnb.aerosolve.core.util;

import com.airbnb.aerosolve.core.Example;
import com.airbnb.aerosolve.core.FeatureVector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Hector Yee
 */
public class CubicSplineTest {
  private static final Logger log = LoggerFactory.getLogger(CubicSplineTest.class);

  @Test
  public void testCubicSplineEvaluate() {
    float[] weights = {2.0f, 1.0f, -1.0f, -2.0f};
    CubicSpline spline = new CubicSpline(0.0f, 1.0f, weights);
    assertEquals(2.0f, spline.evaluate(-1.0f), 0.3f);
    assertEquals(2.0f, spline.evaluate(0.0f), 0.3f);
    assertTrue(spline.evaluate(0.33f) > 0.0f);
    assertTrue(spline.evaluate(0.66f) < 0.0f);
    assertEquals(-2.0f, spline.evaluate(1.0f), 0.3f);
    assertEquals(-2.0f, spline.evaluate(2.0f), 0.3f);
  }

  float func(float x) {
      return 0.1f * (x + 1.0f) * (x - 2.0f);
  }

  @Test
  public void testCubicSplineUpdate() {
    float[] weights = new float[4];
    CubicSpline spline = new CubicSpline(-1.0f, 5.0f, weights);
    Random rnd = new java.util.Random(123);
    for (int i = 0; i < 10000; i++) {
      float x = (float) (rnd.nextDouble() * 6.0 - 1.0);
      float y = func(x);
      float tmp = spline.evaluate(x);
      float delta = 0.1f * (y - tmp);
      spline.update(x, delta);
    }
    // Check we get roots where we expect them to be.
    assertEquals(0.0f, spline.evaluate(-1.0f), 0.2f);
    assertEquals(0.0f, spline.evaluate(2.0f), 0.2f);
    for (int i = 0; i < 20; i++) {
      float x = (float) (6.0 * i / 20.0 - 1.0f);
      float expected = func(x);
      float eval = spline.evaluate(x);
      log.info("x = " + x + " expected = " + expected + " got = " + eval);
      assertEquals(expected, spline.evaluate(x), 0.2f);
    }
  }
}
