package com.programos.deploymentdiagrameditor;

/**Class for point
 * @param <X> in point
 * @param <Y> in point
 */
public class PointOfConnect<X,Y> {
    public X item1;
    public Y item2;

    public PointOfConnect(X item1, Y item2) {
        this.item1 = item1;
        this.item2 = item2;
    }
}
