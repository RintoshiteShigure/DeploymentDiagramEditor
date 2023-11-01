package com.programos.deploymentdiagrameditor.deployBlocks;

import com.programos.deploymentdiagrameditor.Dragger;
import javafx.geometry.Point2D;

/**
 * Factory for blocks
 */
public class BlocksFactory {
    /**
     * @param type of block
     * @param point2D on pane
     * @param text of block
     * @param onDrag listener
     * @return new block
     */
    public static DefaultBlock createBlock(DeployBlockType type, Point2D point2D, String text, Dragger onDrag) {
        return switch (type) {
            case HOST -> new HostBlock(DeployBlockType.HOST,point2D, text, onDrag);
            case ARTIFACT -> new ArtifactBlock(DeployBlockType.ARTIFACT,point2D, text, onDrag);
        };
    }
}
