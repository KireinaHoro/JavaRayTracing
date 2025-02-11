package moe.jsteward.Math;

class Sobol {
    /*
     *  What is the initial value?
     */

    static final int num_dimensions = 0;
    private static final int size = 0;
    private static final long[] matrices = {0};

    /*
     *  calculate the Sobol sequence
     *
     *  Cloud: I thought it should be a static method... TODO
     */
    static double sample(long index, int dimension, long scramble) {

        long result = scramble & ~-(1 << size);
        for (int i = dimension * size; index > 0; index >>= 1, ++i) {
            if ((index & 1) > 0)
                result ^= matrices[i];
        }

        return result * (1.0 / (1 << size));
    }

    /*
     *  calculate the Sobol sequence
     */
    public double sample(long index, int dimension) {
        return sample(index, dimension, 0);
    }
}
