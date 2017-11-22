package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import org.graphsfx.graph.WebGraph;
import org.graphsfx.model.GraphNode;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = (VBox) FXMLLoader.load(getClass().getResource("sample.fxml"));
        AnchorPane anchor = (AnchorPane) root.lookup("#anchorPane");

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 500, 500));
        GraphNode test1 = new GraphNode("TestNode1");
        GraphNode test2 = new GraphNode("TestNode2");
        GraphNode test3 = new GraphNode("TestNode3");
        GraphNode test4 = new GraphNode("TestNode4");
        GraphNode test5 = new GraphNode("TestNode5");
        GraphNode test6 = new GraphNode("TestNode6");
        GraphNode test7 = new GraphNode("TestNode7");
        GraphNode test8 = new GraphNode("TestNode8");


        test4.addBidirectionalAdjacency(test1);
        test4.addBidirectionalAdjacency(test2);
        test4.addBidirectionalAdjacency(test3);
        test1.addBidirectionalAdjacency(test4);
        test2.addBidirectionalAdjacency(test4);
        test3.addBidirectionalAdjacency(test4);
        test3.addBidirectionalAdjacency(test5);
        test5.addBidirectionalAdjacency(test3);
        test5.addBidirectionalAdjacency(test6);
        test5.addBidirectionalAdjacency(test7);
        test5.addBidirectionalAdjacency(test8);
        test8.addBidirectionalAdjacency(test1);

        WebGraph webGraph = new WebGraph();
        webGraph.prefHeightProperty().bind(anchor.heightProperty());
        webGraph.prefWidthProperty().bind(anchor.widthProperty());
        webGraph.setStyle("-fx-border-style: solid");

        webGraph.addGraphNode(test4);

        anchor.getChildren().add(webGraph);

//        test1.getPane().setLayoutX(15);
//        test1.getPane().setLayoutY(50);
//
//        test2.getPane().setLayoutX(75);
//        test2.getPane().setLayoutY(50);
//
//        test3.getPane().setLayoutX(100);
//        test3.getPane().setLayoutY(100);
//
//        test4.getPane().setLayoutX(25);
//        test4.getPane().setLayoutX(75);

          primaryStage.show();

//        GraphEdge edge = new GraphEdge(GraphEdge.PathType.CUBIC);
//        edge.setSourceBindings(test1.getCenterXProperty(), test1.getCenterYProperty());
//        edge.setDestBindings(test2.getCenterXProperty(), test2.getCenterYProperty());
//
//
//        Pane nodeLayer = new Pane();
//
//        root.getChildren().add(edge);
//        root.getChildren().add(nodeLayer);
//
//        nodeLayer.getChildren().add(test1.getPane());
//        nodeLayer.getChildren().add(test2.getPane());
//
//        primaryStage.show();
//        test1.getPane().setLayoutX(15);
//        test1.getPane().setLayoutY(50);
//
//        test2.getPane().setLayoutX(75);
//        test2.getPane().setLayoutY(50);
//
//        test1.addAdjacency(test2);
//        test1.addAdjacency(test2);



    }


    public static void main(String[] args) {
        launch(args);
    }
}
