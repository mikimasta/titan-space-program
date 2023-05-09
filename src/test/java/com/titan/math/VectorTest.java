package com.titan.math;

/*
 * Testing strategy
 *
 * partitioning the input space as follows:
 * addition:
 *      Vector.size() = 1; > 1
 *
 * multiplication:
 *      c (scalar) = 0 ; 1; < 0 ; > 0
 *
 *
 * cover each part
 */


import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;

public class VectorTest {


    /*
     * covers v.size() != x.size(), should throw exception since dimensions
     * do not agree
     */
    @Test(expected = InvalidParameterException.class)
    public void testAdditionNotPossible() {

        Vector v = new Vector(new double[2]);
        Vector x = new Vector(new double[3]);

        v.add(x);
    }

    /*
     * covers addition v.sz = 1, x.sz = 1
     */
    @Test
    public void testAddTwoVectorsDimensionOne() {
        // given
        Vector v = new Vector(new double[]{3});
        Vector x = new Vector(new double[]{4});

        // then
        assertEquals(7, v.add(x).getValues()[0], 0.0001);
    }

    /*
     * covers v.size(), v.size() > 1
     */
    @Test
    public void testAddTwoVectorsDimBiggerThanOne() {
        // given
        Vector v = new Vector(new double[]{4, 3, -2, 1.5});
        Vector x = new Vector(new double[]{11.1, 5, 3.4, 0});

        // then
        assertEquals(new Vector(new double[]{15.1, 8, 1.4, 1.5}), x.add(v));
    }

    /*
     * covers c = 0, v.size() > 1
     */
    @Test
    public void testMultiplicationByScalar0() {
        // given
        Vector x = new Vector(new double[]{11.1, 5, 3.4, 0});

        // then
        assertEquals(new Vector(new double[]{0d, 0d, 0d, 0d}), x.multiplyByScalar(0));
    }

    /*
     * covers c < 0, v.size() > 1
     */
    @Test
    public void testMultiplyByNegativeScalar() {
        // given
        Vector x = new Vector(new double[]{-2.4, 0, 11.7, -4});

        // then
        assertEquals(new Vector(new double[]{2.4, 0, -11.7, 4}), x.multiplyByScalar(-1));
    }

    /*
     * covers c > 0, v.size() > 1
     */
    @Test
    public void testMultiplyByBiggerScalar() {
        // given
        Vector x = new Vector(new double[]{5, 5.734, 3.43, 0});
        Vector v = new Vector(new double[]{16.3000, 18.6928,11.1818 ,0});

        // then
        for (int i = 0; i < x.getSize(); i++) {
            assertEquals(v.getValues()[i], x.getValues()[i] * 3.26, 0.00005);
        }
    }

}
