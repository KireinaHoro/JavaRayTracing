package moe.jsteward.Math;

public class sobol {
    /*
     *  What is the initial value?
     */

    static final int num_dimensions = 0;
    static final int size = 0;
    static final long matrices[] = {0};

    /*
     *  calculate the sobol sequence
     *
     *  Cloud: I thought it should be a static method... TODO
     */
    public static double sample(long index, int dimension, long scramble) {

        long result = scramble & ~-(1 << size);
        for (int i = dimension * size; index > 0; index >>= 1, ++i) {
            if ((index & 1) > 0)
                result ^= matrices[i];
        }

        return result * (1.0 / (1 << size));
    }

    /*
     *  calculate the sobol sequence
     */
    public double sample(long index, int dimension) {
        return sample(index, dimension, 0);
    }
}
