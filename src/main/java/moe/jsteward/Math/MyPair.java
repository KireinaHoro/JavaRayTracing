package moe.jsteward.Math;

import org.apache.commons.lang3.tuple.Pair;

public class MyPair<L, R> extends Pair<L, R> {
    public
    L first;
    R second;

    public L getLeft() {
        return first;
    }

    public R getRight() {
        return second;
    }

    public R setValue(R v) {
        second = v;
        return second;
    }
}
