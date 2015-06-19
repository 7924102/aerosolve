package com.airbnb.aerosolve.core.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

// A cubic spline implemented using Bernstein polynomial basis.
public class CubicSpline implements Serializable {
  private static final long serialVersionUID = 5166347177557768301L;

  @Getter @Setter
  private float[] weights;

  @Getter
  private float minVal;
  @Getter
  private float maxVal;
  private float scale;

  public CubicSpline(float minVal, float maxVal, float[] weights) {
    this.weights = weights;
    this.minVal = minVal;
    this.maxVal = maxVal;
    float diff = Math.max(maxVal - minVal, 1e-10f);
    this.scale = 1.0f / diff;
  }

  public float evaluate(float x) {
    float t = getT(x);
    float[] coeff = getCoefficients(t);
    return weights[0] * coeff[0] +
        weights[1] * coeff[1] +
        weights[2] * coeff[2] +
        weights[3] * coeff[3];
  }

  public void update(float x, float delta) {
    float t = getT(x);
    float[] coeff = getCoefficients(t);
    for (int i = 0; i < 4; i++) {
      weights[i] += coeff[i] * delta;
    }
  }

  // Returns the t from the x
  public float getT(float x) {
    float t = (x  - minVal) * scale;
    t = Math.min(1.0f, Math.max(0.0f, t));
    return t;
  }
  
  // Returns the Bernstein cubic basis coefficients from t.
  private float[] getCoefficients(float t) {
    float t2 = t * t;
    float t3 = t * t2;
    float tinv = 1.0f - t;
    float tinv2 = tinv * tinv;
    float tinv3 = tinv * tinv2;
    float[] coeff = new float[4];
    coeff[0] = tinv3;
    coeff[1] = 3.0f * tinv2 * t;
    coeff[2] = 3.0f * tinv3 * t2;
    coeff[3] = t3;
    return coeff;
  }

}
