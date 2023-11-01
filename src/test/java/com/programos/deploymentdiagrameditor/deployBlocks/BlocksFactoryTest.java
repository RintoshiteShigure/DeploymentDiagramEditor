package com.programos.deploymentdiagrameditor.deployBlocks;

import de.saxsys.javafx.test.JfxRunner;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(JfxRunner.class)
class BlocksFactoryTest {

    @Test
    void createBlock() {
        JFXPanel fxPanel = new JFXPanel();
        DefaultBlock block1=BlocksFactory.createBlock(DeployBlockType.ARTIFACT,new Point2D(0,0),"sd",()->{});
        Assert.assertEquals(block1.text,"sd");
        Assert.assertEquals(block1.point,new Point2D(0,0));
        Assert.assertEquals(block1.type,DeployBlockType.ARTIFACT);
    }
}