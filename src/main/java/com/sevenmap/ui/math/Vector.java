package com.sevenmap.ui.math;

public interface Vector<V extends Vector<V>> {

    /**
     * Add the current vector to another {@link Vector} object.
     * @param vec other vector
     * @return this + vec
     */
    public V add(V vec);

    /**
     * Substract another vector from the current {@link Vector} object
     * @param vec other vector
     * @return this - vec
     */
    public V sub(V vec);

    /**
     * Calculate the dot product by another vector.
     * @param vec other vector
     * @return this . vec
     */
    public float dot(V vec);

    /**
     * Calculate the norm of the vector.
     * @return norm
     */
    public float norm();
    
    /**
     * Multiply the current vector by another {@link Vector}.
     * @param vec other vector
     * @return result
     */
    public V multiply(V vec);

    /**
     * Divide the current vector by another vector.
     * @param vec other vector
     * @return result
     */
    public V divide(V vec);

    /**
     * Normalize and return the vector.
     * @return normalized vector
     */
    public V normalize();
}
