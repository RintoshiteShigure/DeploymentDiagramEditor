package com.programos.deploymentdiagrameditor.deployBlocks;

import com.programos.deploymentdiagrameditor.Dragger;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * all block on pane
 */
public class ListBlocks implements Serializable {
    ArrayList<DefaultBlock> blocks = new ArrayList<>();
    ArrayList<Transition> connections=new ArrayList<>();
    transient Dragger dragger;
    public ListBlocks(Dragger dragListener)
    {
        dragger = dragListener;
    }

    /**
     * @param type of block
     * @param point on pane
     * @param text on block
     * @return new block
     */
    public DefaultBlock add(DeployBlockType type, Point2D point, String text)
    {
        DefaultBlock block = BlocksFactory.createBlock(type,point,text,dragger);
        blocks.add(block);
        return block;
    }
    public DefaultBlock add(DefaultBlock block)
    {
        blocks.add(block);
        return block;
    }

    /**
     * @return all blocks
     */
    public ArrayList<DefaultBlock> getBlocks() {
        return blocks;
    }

    /**
     * delete all elements
     */
    public void clear()
    {
        blocks.clear();
        connections.clear();
    }

    /**
     * @param block delete blocks
     */
    public void delete(DefaultBlock block)
    {
        blocks.remove(block);
    }

    /**
     * @param line of arrow
     * @param parent block
     * @param child block
     * @return new connection
     */
    public Transition addConnection(Line line, DefaultBlock parent, DefaultBlock child) {
        Transition transition = new Transition(line,parent,child);
        connections.add(transition);
        return transition;
    }

    /**
     * @return all connections
     */
    public ArrayList<Transition> getConnections()
    {
        return connections;
    }
}
