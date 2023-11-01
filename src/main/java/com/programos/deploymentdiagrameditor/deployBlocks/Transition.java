package com.programos.deploymentdiagrameditor.deployBlocks;

import com.programos.deploymentdiagrameditor.PointOfConnect;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.io.Serializable;

/**
 * Class for connections
 */
public class Transition extends Group implements Serializable {
    public DefaultBlock from;
    public DefaultBlock to;
    public Transition(Line line, DefaultBlock parent, DefaultBlock child) {
        super(line);
        this.from = parent;
        this.to = child;
    }
    /**
     * @param parent block
     * @param child block
     * @return path from parent to child
     */
    public static PointOfConnect<Point2D, Point2D> getPointsOfConnection(DefaultBlock parent, DefaultBlock child) {
        var fromPoints = parent.getArrayOfMinMaxPoints();
        var toPoints = child.getArrayOfMinMaxPoints();
        Point2D pointFromFinal = Point2D.ZERO;
        Point2D pointToFinal = Point2D.ZERO;
        double lowestDistance = Double.POSITIVE_INFINITY;
        for (Point2D fromPoint : fromPoints) {
            for (Point2D toPoint : toPoints) {
                var newDistance = fromPoint.distance(toPoint);
                if (newDistance < lowestDistance) {
                    pointFromFinal = fromPoint;
                    pointToFinal = toPoint;
                    lowestDistance = newDistance;
                }
            }
        }
        return new PointOfConnect<>(pointFromFinal, pointToFinal);
    }
}