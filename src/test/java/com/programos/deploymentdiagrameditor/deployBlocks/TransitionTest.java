package com.programos.deploymentdiagrameditor.deployBlocks;

import de.saxsys.javafx.test.JfxRunner;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(JfxRunner.class)
class TransitionTest {

    @Test
    void getPointsOfConnection() {
        JFXPanel fxPanel = new JFXPanel();
        DefaultBlock block1=BlocksFactory.createBlock(DeployBlockType.ARTIFACT,new Point2D(0,0),"sd",()->{});
        DefaultBlock block2=BlocksFactory.createBlock(DeployBlockType.ARTIFACT,new Point2D(2,100),"sd",()->{});
        var points=Transition.getPointsOfConnection(block1,block2);
        Assert.assertEquals(new Point2D(points.item1.getX(),points.item1.getX()),new Point2D(block1.width,block1.height/2));
        Assert.assertEquals(new Point2D(points.item2.getX(),points.item2.getY()),new Point2D(block2.pX+ block2.width/2,block2.pY));
    }
}