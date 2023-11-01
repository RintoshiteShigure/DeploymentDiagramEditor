package com.programos.deploymentdiagrameditor.deployBlocks;

import com.programos.deploymentdiagrameditor.Dragger;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * HostBlock class
 */
public class HostBlock extends DefaultBlock{
    ArrayList<ArtifactBlock> artifactBlocks;
    public HostBlock(DeployBlockType type, Point2D point, String text, Dragger onDrag) {
        super(type, point, text, onDrag);
        artifactBlocks=new ArrayList<>();
    }
    public ArrayList<ArtifactBlock> getArtifactBlocks()
    {
        return artifactBlocks;
    }

    /**
     * @return copy
     */
    @Override
    public HostBlock copy() {
        HostBlock block = new HostBlock(this.type, new Point2D(this.pX,this.pY), this.text, this.listeners.get(this.listeners.size()-1));
        for(ArtifactBlock artifactBlock:artifactBlocks)
            block.addArtifact(artifactBlock.text);
        return block;
    }
    @Override
    public HostBlock copy(Dragger dragger) {
        HostBlock block = new HostBlock(this.type, new Point2D(this.pX,this.pY), this.text, dragger);
        for(ArtifactBlock artifactBlock:artifactBlocks)
            block.addArtifact(artifactBlock.text);
        return block;
    }

    /** add artifact into host
     * @param text on artifact
     */
    public void addArtifact(String text)
    {
        ArtifactBlock block = new ArtifactBlock(DeployBlockType.ARTIFACT,this.point,text);
        artifactBlocks.add(block);
        block.setOnMouseClicked(e->{
            if(e.isControlDown())
            {
                artifactBlocks.remove(block);
                draw();
            }
        });
        draw();
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
        double artifactHeight = textHeight + offset;
        double artifactWidth = textWidth + offset+15;

        for (ArtifactBlock artifact: artifactBlocks) {
            getChildren().add(artifact);
            artifact.draw();
            artifact.setTranslateX(10f * 0.5f);
            artifact.setTranslateY(((artifactHeight+5f)*(artifactBlocks.indexOf(artifact)+1)));
        }
        for (ArtifactBlock artifact: artifactBlocks)
            if (artifact.getWidth() > artifactWidth) artifactWidth = artifact.getWidth();
        Rectangle title = new Rectangle(
                0,
                5,
                Math.max(textWidth + offset,artifactWidth+7),
                textHeight);
        Rectangle body = new Rectangle(
                0,
                title.getHeight()+5,
                title.getWidth(),
                artifactBlocks.size()*(artifactHeight+5f)+7f);
        Rectangle background = new Rectangle(
                5,
                0,
                body.getWidth(),
                title.getHeight()+body.getHeight());
        Line line1 = new Line(0,5,5,0);
        Line line2 = new Line(body.getWidth(),5,body.getWidth()+5,0);
        Line line3 = new Line(body.getWidth(),title.getHeight()+body.getHeight()+5,body.getWidth()+5,title.getHeight()+body.getHeight());
        Line bgLine1 = new Line(5,0,body.getWidth()+5,0);
        bgLine1.setFill(Color.BLACK);
        Line bgLine2 = new Line(body.getWidth()+5,0,body.getWidth()+5,title.getHeight()+body.getHeight());
        bgLine2.setFill(Color.BLACK);
        title.setFill(Color.WHITE);
        title.setStroke(Color.BLACK);
        body.setFill(Color.WHITE);
        body.setStroke(Color.BLACK);
        background.setFill(Color.WHITE);
        line1.setFill(Color.BLACK);
        line2.setFill(Color.BLACK);
        line3.setFill(Color.BLACK);
        point = new Point2D(getTranslateX(), getTranslateY());
        width = title.prefWidth(-1);
        height = title.prefHeight(-1)+body.prefHeight(-1)+3;
        getChildren().addAll(background,bgLine1,bgLine2,line1,line2,line3,body,title,showText,editableText);
        showText.setTranslateX(offset * 0.5f);
        showText.setTranslateY(offset * 0.5f);
        for (ArtifactBlock artifact: artifactBlocks) artifact.toFront();
    }
}
