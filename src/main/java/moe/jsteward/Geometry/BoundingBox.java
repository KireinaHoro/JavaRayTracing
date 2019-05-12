package moe.jsteward.Geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class BoundingBox {
    //  The bounds (min / max vectors).
    protected Vector3D m_bounds[] = new Vector3D[2];

    /*
     *  Initializes the bounding box from the provided geometry.
     *  \param1	geometry :=  The geometry.
     */
    public BoundingBox(Geometry geometry) {
        set(geometry);
    }


}
