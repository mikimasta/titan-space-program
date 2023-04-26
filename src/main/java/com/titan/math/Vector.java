package com.titan.math;

import java.security.InvalidParameterException;

public class Vector {

    /**
     * values of a vector
     */
    private double[] values;

    /**
     * length of a vector
     */
    private double length;

    /**
     * the dimension of a vector
     */
    private int size;

    /**
     * constructs a vector and calculates its length using the appropriate formula
     * @param values
     */
    public Vector(double[] values) {
        this.values = values;
        this.size = values.length;

        double sum = 0;
        for (double d : values) sum += d*d;
        length = Math.sqrt(sum);
    }

    /**
     * returns the length of a vector
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * returns the dimension of a vector
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @return values a vector holds
     */
    public double[] getValues() {
        return values;
    }

    /**
     * adds two vectors together
     * @param v requires a non-zero equal-dimension vector
     * @return new Vector as a sum of two
     */
    public Vector add(Vector v) {
        checkSize(v);

        double[] newValues = new double[size];
        for (int i = 0; i < size; i++) {
            newValues[i] = values[i] + v.getValues()[i];
        }
        return new Vector(newValues);
    }

    private void checkSize(Vector v) throws InvalidParameterException {
        if (v.getSize() != size) {
            throw new InvalidParameterException("The sizes of the vectors are not matching! Sizes: this vector="
                    + size + "; next vector=" + v.getSize());
        }
    }

    /**
     * subtracts a vector from another
     * @param v vector to subtract
     * @return new Vector after subtraction
     */
    public Vector subtract(Vector v) {
        checkSize(v);

        double[] newValues = new double[size];
        for (int i = 0; i < size; i++) {
            newValues[i] = values[i] - v.getValues()[i];
        }
        return new Vector(newValues);
    }

    /**
     * multiplies a given vector by a scalar
     * @param c scalar to multuplly by
     * @return new Vector after multiplication
     */
    public Vector multiplyByScalar(double c) {
        double[] newValues = new double[size];
        for (int i = 0; i < size; i++) {
            newValues[i] = values[i] * c;
        }
        return new Vector(newValues);
    }

    public static Vector add(Vector v, Vector c) {
        return v.add(c);
    }

    public static Vector subtract(Vector v, Vector c) {
        return v.subtract(c);
    }

    @Override
    public String toString() {
        String valuesString = "";
        for (int i = 0; i < size; i++) {
            valuesString += values[i] + " ";
        }
        return "[ " + valuesString + "]";
    }

    /**
     * clones a vector
     * @return cloned vector
     */
    public Vector clone() {
        return new Vector(values);
    }

    @Override
    public boolean equals(Object anotherVector) {

        checkSize((Vector) anotherVector);

        for (int i = 0; i < this.getSize(); i++) {
            if (!(((Vector) anotherVector).getValues()[i] == getValues()[i])) return false;
        }

        return true;
    }


//    public static void main(String[] args) {
//        Vector v = new Vector(new double[]{5, 7, 9});
//        Vector x = new Vector(new double[]{5, 7, 9});
//
//        System.out.println(v.equals(x));
//        System.out.println(0.1d+0.2d);
//
//        System.out.println(-2+3.4d+0.12);
//    }
}
