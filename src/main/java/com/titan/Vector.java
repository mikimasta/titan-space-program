package com.titan;

import java.math.BigDecimal;

public class Vector {

    /**
     * values of a 3d vector
     */
    private double x1, x2, x3;

    /**
     * length of a vector
     */
    private double length;


    /**
     * constructs a vector and calculates its length using the appropriate formula
     * @param x1
     * @param x2
     * @param x3
     */
    public Vector(double x1, double x2, double x3) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;

        length = Math.sqrt(x1*x1 + x2*x2 + x3*x3);
    }

    /**
     * returns the length of a vector
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * adds two vectors together
     * @param v vector to add
     * @return new Vector as a product of two
     */
    public Vector add(Vector v) {
        return new Vector(x1 + v.getX1(), x2 + v.getX2(), x3 + v.getX3());
    }

    /**
     * subtracts a vector from another
     * @param v vector to subtract
     * @return new Vector after subtraction
     */
    public Vector subtract(Vector v) {
        return new Vector(x1 - v.getX1(), x2 - v.getX2(), x3 - v.getX3());
    }

    /**
     * multiplies a given vector by a scalar
     * @param c scalar to multuplly by
     * @return new Vector after multiplication
     */
    public Vector multiplyByScalar(double c) {
        return new Vector((x1 * c), (x2 * c),  x3 * c);
    }

    public Vector multiplyByScalar(BigDecimal c) {
        return new Vector(
                (c.multiply(BigDecimal.valueOf(x1))).doubleValue(),
                (c.multiply(BigDecimal.valueOf(x2))).doubleValue(),
                (c.multiply(BigDecimal.valueOf(x3))).doubleValue());
    }

    public static Vector add(Vector v, Vector c) {
        return v.add(c);
    }

    public static Vector subtract(Vector v, Vector c) {
        return v.subtract(c);
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    @Override
    public String toString() {
        return "[ " + x1 + " " + x2 + " " + x3 + " ]";
    }

    /**
     * clones a vector
     * @return cloned vector
     */
    public Vector clone() {
        return new Vector(x1, x2, x3);
    }


}
