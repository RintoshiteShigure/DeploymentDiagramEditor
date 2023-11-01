package com.programos.deploymentdiagrameditor;

import com.programos.deploymentdiagrameditor.deployBlocks.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller
 */
public class MainController implements Initializable {
    public ToggleButton artifactCreator;
    public ToggleButton nodeCreator;
    public Pane desktop;
    ListBlocks blocks;

    DefaultBlock[] connectionBlocks = new DefaultBlock[2];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blocks = new ListBlocks(this::drawTransitions);
        desktop.setOnMouseClicked(e-> {
            if (artifactCreator.isSelected()) {
                drawOnDesktop(blocks.add(DeployBlockType.ARTIFACT, new Point2D(e.getX(), e.getY()), "Артефакт"));
                artifactCreator.setSelected(false);
            } else if (nodeCreator.isSelected()){
                drawOnDesktop(blocks.add(DeployBlockType.HOST, new Point2D(e.getX(), e.getY()), "Узел"));
                nodeCreator.setSelected(false);
            }
        });
    }

    /**
     * @param block for drawing
     */
    private void drawOnDesktop(DefaultBlock block)
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Копировать");
        item1.setOnAction(event -> {
            DefaultBlock copy=blocks.add(block.copy());
            drawOnDesktop(copy);
        });
        MenuItem item2 = new MenuItem("Удалить");
        item2.setOnAction(event -> {
            desktop.getChildren().remove(block);
            blocks.delete(block);
            drawTransitions();
        });
        MenuItem item3 = new MenuItem("Добавить артефакт");
        item3.setOnAction(e-> ((HostBlock)block).addArtifact("артефакт"));
        contextMenu.getItems().addAll(item1, item2);
        if(block.type==DeployBlockType.HOST)
            contextMenu.getItems().add(item3);
        desktop.getChildren().add(block);
        block.setOnMouseClicked(e->{
            if(e.isShiftDown())
                if(connectionBlocks[0]==null)
                    connectionBlocks[0] = block;
                else if (connectionBlocks[1] == null) {
                    connectionBlocks[1] = block;
                    addTransition(connectionBlocks[0],connectionBlocks[1]);
                    connectionBlocks = new DefaultBlock[2];
                }
        });
        block.setOnContextMenuRequested(e-> contextMenu.show(block,e.getScreenX(),e.getScreenY()));
        block.draw();
    }

    /**
     * draw a transitions
     */
    private void drawTransitions() {
        desktop.autosize();
        List<DefaultBlock[]> transitionElements=new ArrayList<>();
        for (Transition transition : blocks.getConnections()) {
            transitionElements.add(new DefaultBlock[]{transition.from, transition.to});
            desktop.getChildren().remove(transition);
        }
        blocks.getConnections().clear();
        for (DefaultBlock[] connections : transitionElements)
            if(blocks.getBlocks().contains(connections[0]) && blocks.getBlocks().contains(connections[1]))
                addTransition(connections[0], connections[1]);
    }

    /**
     * @param parent block
     * @param child block
     */
    private void addTransition(DefaultBlock parent, DefaultBlock child)
    {
        var points = Transition.getPointsOfConnection(parent, child);
        Line line = new Line(points.item1.getX(), points.item1.getY(), points.item2.getX(), points.item2.getY());
        Transition transition = blocks.addConnection(line,parent,child);
        desktop.getChildren().add(transition);
        transition.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.isControlDown()) {
                desktop.getChildren().remove(transition);
                blocks.getConnections().remove(transition);
            }
        });
    }

    /**save as .png
     */
    @FXML
    private void saveAsPicture() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения (*.png)", "*.png"));
        fileChooser.setInitialFileName("Диаграмма развертывания");
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            WritableImage snapShot = desktop.snapshot(new SnapshotParameters(), null);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapShot, null);
            ImageIO.write(renderedImage, "png", file);
        }
    }

    /**
     * save as .fdd
     */
    @FXML
    private  void saveAsFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите директорию для сохранения...");
        fileChooser.setInitialFileName("Диаграмма развертывания");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Диаграмма развертывания", "*.fdd"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) saveAsFile(file);
    }

    /**
     * @param file for saving
     */
    public void saveAsFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream outStream = new ObjectOutputStream(fos);
        outStream.writeObject(blocks);
        outStream.flush();
        outStream.close();
        System.out.println("Сохранено!");
    }

    /**
     * open as .fdd
     */
    @FXML
    private void openAsFile() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть файл диаграммы развертывания");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Диаграмма развертывания", "*.fdd");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if (file!=null) openAsFile(file);
    }

    /**
     * @param file for opening
     */
    private void openAsFile(File file) throws IOException, ClassNotFoundException {
        reloadDesktop();
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream inputStream = new ObjectInputStream(fis);
        ListBlocks temp=(ListBlocks) inputStream.readObject();
        for (DefaultBlock element:temp.getBlocks()) {
            drawOnDesktop(blocks.add(element.copy(this::drawTransitions)));
        }
        for (Transition transition:temp.getConnections()) {
            DefaultBlock[] connected = new DefaultBlock[2];
            for (DefaultBlock element:blocks.getBlocks()) {
                if (element.Equals(transition.from))
                    connected[0] = element;
                if (element.Equals(transition.to))
                    connected[1] = element;
            }
            addTransition(connected[0], connected[1]);
        }
        inputStream.close();
    }

    /**
     * delete all objects on scene
     */
    @FXML
    private void reloadDesktop()
    {
        desktop.getChildren().removeAll(blocks.getBlocks());
        desktop.getChildren().removeAll(blocks.getConnections());
        blocks.clear();
        connectionBlocks = new DefaultBlock[2];
    }

    /**
     * show information
     */
    @FXML
    private void help()
    {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, """
                Left Panel:elements of diagram (LBM on workspace to place)
                Top Menu: export,import,functions,info
                Hot keys: Ctrl+LBM on element - delete
                                Shift+LBM on 2 blocks - connect blocks
                                RBM on block - show context menu
                """, ButtonType.CLOSE);
        infoAlert.setHeaderText("Developed by Kozinov");
        infoAlert.setTitle("Inforamtion");
        infoAlert.show();
    }
}