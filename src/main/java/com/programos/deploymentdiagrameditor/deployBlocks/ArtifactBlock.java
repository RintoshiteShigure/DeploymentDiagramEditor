package com.programos.deploymentdiagrameditor.deployBlocks;

import com.programos.deploymentdiagrameditor.Dragger;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class for artifacts
 */
public class ArtifactBlock extends DefaultBlock{
    public ArtifactBlock(DeployBlockType type, Point2D point, String text, Dragger onDrag) {
        super(type, point, text, onDrag);
    }
    public ArtifactBlock(DeployBlockType type, Point2D point, String text) {
        super(type, point, text);
    }

    /**
     * @return copy
     */
    @Override
    public DefaultBlock copy(){
        return new ArtifactBlock(this.type, new Point2D(this.pX,this.pY), this.text, this.listeners.get(this.listeners.size()-1));
    }
    @Override
    public DefaultBlock copy(Dragger dragger){
        return new ArtifactBlock(this.type, new Point2D(this.pX,this.pY), this.text, dragger);
    }

    /**
     * draw a shapes
     */
    @Override
    public void draw()
    {
        showText.applyCss();
        showText.layout();
        editableText.layout();
        editableText.applyCss();
        getChildren().clear();
        float offset = 10f;

        var textWidth = showText.prefWidth(-1);
        var textHeight = showText.prefHeight(-1);

        Rectangle body = new Rectangle(
                10,
                0,
                textWidth + offset,
                Math.max(textHeight + offset,30));
        body.setStroke(Color.BLACK);
        body.setFill(Color.WHITE);
        Rectangle part1 = new Rectangle(
                0,
                body.getHeight()*0.15,
                15,
                body.getHeight()*0.27);
        part1.setFill(Color.WHITE);
        part1.setStroke(Color.BLACK);
        Rectangle part2 = new Rectangle(
                0,
                body.getHeight()*0.6,
                15,
                body.getHeight()*0.27);
        point = new Point2D(getTranslateX(), getTranslateY());
        width = body.getWidth()+10;
        height = body.getHeight();
        part2.setStroke(Color.BLACK);
        part2.setFill(Color.WHITE);
        getChildren().add(body);
        getChildren().add(part1);
        getChildren().add(part2);
        getChildren().add(showText);
        getChildren().add(editableText);
        showText.setTranslateX(offset * 1.5f);
        showText.setTranslateY(offset * 0.5f);
    }
}
