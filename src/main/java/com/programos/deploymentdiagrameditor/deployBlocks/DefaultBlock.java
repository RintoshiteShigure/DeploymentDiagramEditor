package com.programos.deploymentdiagrameditor.deployBlocks;

import com.programos.deploymentdiagrameditor.Dragger;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class for Parent block
 */
public class DefaultBlock extends Group implements Serializable {
    transient public ArrayList<Dragger> listeners = new ArrayList<>();
    transient protected TextField editableText;
    public String text;
    protected double width;
    transient protected Label showText;
    protected double height;
    transient protected Point2D point;
    public double pX;
    public double pY;
    public DeployBlockType type;
    private double mouseAnchorX;
    private double mouseAnchorY;

    public DefaultBlock(DeployBlockType type, Point2D point, String text, Dragger onDrag)
    {
        this.text=text;
        pX=point.getX();
        pY=point.getY();
        this.type=type;
        setTranslateX(point.getX());
        setTranslateY(point.getY());

        showText = new Label(text);
        editableText= new TextField(text);
        showText.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                this.text = showText.getText();
                editableText.setText(this.text);
                showText.setVisible(false);
                editableText.setVisible(true);
                editableText.toFront();
                editableText.requestFocus();
            }
        });
        editableText.setOnKeyTyped(e -> this.text=editableText.getText());
        editableText.focusedProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue) {
                showText.setVisible(true);
                editableText.setVisible(false);
                showText.setText(this.text);
                showText.toFront();
                draw();
            }
        });
        editableText.setOnKeyReleased(e -> {
            this.text=editableText.getText();
            if(e.getCode() == KeyCode.ENTER)
            {
                showText.setVisible(true);
                editableText.setVisible(false);
                showText.setText(this.text);
                showText.toFront();
                draw();
            }
        });
        showText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        editableText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        showText.setAlignment(Pos.BASELINE_CENTER);
        this.point = point;
        pX = this.point.getX();
        pY = this.point.getY();
        this.width = computePrefWidth(-1);
        this.height = computePrefHeight(-1);
        editableText.setVisible(false);
        getChildren().add(showText);
        width=showText.prefWidth(-1);
        getChildren().add(editableText);
        setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
        listeners.add(onDrag);
        setOnMouseDragged(mouseEvent -> {
            double xBound=0,yBound=0;

            setTranslateX(Math.max(getTranslateX() + mouseEvent.getX() - mouseAnchorX, xBound));
            setTranslateY(Math.max(getTranslateY() + mouseEvent.getY() - mouseAnchorY, yBound));

            this.point = new Point2D(getTranslateX(), getTranslateY());
            pX=this.point.getX();
            pY=this.point.getY();
            for (Dragger listener : listeners) {
                listener.onDrag();
            }
        });
    }
    public DefaultBlock(DeployBlockType type, Point2D point, String text)
    {
        this.text=text;
        pX=point.getX();
        pY=point.getY();
        this.type=type;
        setTranslateX(point.getX());
        setTranslateY(point.getY());
        showText = new Label(text);
        editableText= new TextField(text);
        showText.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                this.text = showText.getText();
                editableText.setText(this.text);
                showText.setVisible(false);
                editableText.setVisible(true);
                editableText.toFront();
                editableText.requestFocus();
            }
        });
        editableText.setOnKeyTyped(e -> this.text=editableText.getText());
        editableText.focusedProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue) {
                showText.setVisible(true);
                editableText.setVisible(false);
                showText.setText(this.text);
                showText.toFront();
                ((HostBlock)getParent()).draw();
            }
        });
        editableText.setOnKeyReleased(e -> {
            this.text=editableText.getText();
            if(e.getCode() == KeyCode.ENTER)
            {
                showText.setVisible(true);
                editableText.setVisible(false);
                showText.setText(this.text);
                showText.toFront();
                ((HostBlock)getParent()).draw();
            }
        });
        showText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        editableText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        showText.setAlignment(Pos.BASELINE_CENTER);
        this.point = point;
        pX = this.point.getX();
        pY = this.point.getY();
        this.width = computePrefWidth(-1);
        this.height = computePrefHeight(-1);
        editableText.setVisible(false);
        getChildren().add(showText);
        getChildren().add(editableText);
    }

    /**
     * draw a shapes
     */
    public void draw() {}

    /**
     * @return copy
     */
    public DefaultBlock copy(){
        return new DefaultBlock(this.type, new Point2D(this.pX,this.pY), this.text, this.listeners.get(this.listeners.size()-1));
    }
    public DefaultBlock copy(Dragger dragger){
        return new DefaultBlock(this.type, new Point2D(this.pX,this.pY), this.text, dragger);
    }
    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public Point2D getPosition() {
        return point;
    }
    public double minY() {
        return getPosition().getY();
    }

    public double minX() {
        return getPosition().getX();
    }

    public double maxY() {
        return getPosition().getY() + getHeight();
    }

    public double maxX() {
        return getPosition().getX() + getWidth();
    }

    /**
     * @return array of points
     */
    public ArrayList<Point2D> getArrayOfMinMaxPoints() {
        ArrayList<Point2D> fromPoints = new ArrayList<>();
        fromPoints.add(new Point2D(maxX(), maxY() - getHeight() * 0.5));
        fromPoints.add(new Point2D(maxX() - getWidth() * 0.5, minY()));
        fromPoints.add(new Point2D(maxX() - getWidth() * 0.5, maxY()));
        fromPoints.add(new Point2D(minX(), maxY() - getHeight() * 0.5));

        return fromPoints;
    }

    /**
     * @param element equable
     * @return result of equals
     */
    public boolean Equals(DefaultBlock element)
    {
        return this.text.equals(element.text) && this.pX == element.pX && this.pY == element.pY && this.type == element.type;
    }
}

