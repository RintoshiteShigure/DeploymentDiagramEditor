package com.programos.deploymentdiagrameditor.deployBlocks;

import de.saxsys.javafx.test.JfxRunner;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;


@RunWith(JfxRunner.class)
class ArtifactBlockTest {

    @Test
    void copy() {
        JFXPanel fxPanel = new JFXPanel();
        DefaultBlock block=BlocksFactory.createBlock(DeployBlockType.ARTIFACT,new Point2D(0,0),"sd",()->{});
        DefaultBlock copy=block.copy();
        Assert.assertTrue(copy.Equals(block));
        Assert.assertNotEquals(block,copy);
    }
}