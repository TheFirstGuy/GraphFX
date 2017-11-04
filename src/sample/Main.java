package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.graphsfx.graph.WebGraph;
import org.graphsfx.model.GraphNode;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = (VBox) FXMLLoader.load(getClass().getResource("sample.fxml"));
        AnchorPane anchor = (AnchorPane) root.lookup("#anchorPane");

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        GraphNode test1 = new GraphNode("TestNode1");
        GraphNode test2 = new GraphNode("TestNode2");
        GraphNode test3 = new GraphNode("TestNode3");
        GraphNode test4 = new GraphNode("TestNode4");

        test1.addAdjacency(test2);
        test1.addAdjacency(test3);
        test2.addAdjacency(test3);
        test2.addAdjacency(test4);


        WebGraph webGraph = new WebGraph();
        webGraph.prefHeightProperty().bind(anchor.heightProperty());
        webGraph.prefWidthProperty().bind(anchor.widthProperty());
        webGraph.setStyle("-fx-border-style: solid");

        webGraph.addGraphNode(test1);
        webGraph.addGraphNode(test3);

        anchor.getChildren().add(webGraph);

        test1.getPane().setLayoutX(15);
        test1.getPane().setLayoutY(50);

        test2.getPane().setLayoutX(75);
        test2.getPane().setLayoutY(50);

        test3.getPane().setLayoutX(100);
        test3.getPane().setLayoutY(100);

        test4.getPane().setLayoutX(25);
        test4.getPane().setLayoutX(75);

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
