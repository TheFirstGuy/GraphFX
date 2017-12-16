package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.graphsfx.graph.WebGraph;
import org.graphsfx.model.GraphEdge;
import org.graphsfx.model.GraphNode;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = (VBox) FXMLLoader.load(getClass().getResource("sample.fxml"));
        AnchorPane graphPane = (AnchorPane) root.lookup("#GraphParent");

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


//        test4.addBidirectionalAdjacency(test1);
//        test4.addBidirectionalAdjacency(test2);
//        test4.addBidirectionalAdjacency(test3);
//        test1.addBidirectionalAdjacency(test4);
//        test2.addBidirectionalAdjacency(test4);
//        test3.addBidirectionalAdjacency(test4);
//        test3.addBidirectionalAdjacency(test5);
//        test5.addBidirectionalAdjacency(test3);
//        test5.addBidirectionalAdjacency(test6);
//        test5.addBidirectionalAdjacency(test7);
//        test5.addBidirectionalAdjacency(test8);
//        test8.addBidirectionalAdjacency(test1);

        graphPane.prefHeightProperty().bind(root.heightProperty());
        graphPane.prefWidthProperty().bind(root.widthProperty());
        WebGraph webGraph = new WebGraph();
        webGraph.prefWidthProperty().bind(graphPane.widthProperty());
        webGraph.prefHeightProperty().bind(graphPane.heightProperty());
        graphPane.setPickOnBounds(false);

        webGraph.addGraphNode(test4);

        //graphPane.getChildren().add(webGraph);

//        GraphNode urs = new GraphNode("Urs Evora");
//        GraphNode mom = new GraphNode("Sunday Nelson");
//        GraphNode dad = new GraphNode("Antonio Evora");
//        GraphNode pete = new GraphNode("Peter Nelson");
//        GraphNode helen = new GraphNode("Helen Nelson");
//
//
//        urs.addAdjacency(dad);
//        urs.addAdjacency(mom);
//        mom.addAdjacency(pete);
//        mom.addAdjacency(helen);
//        dad.addAdjacency(test1);
//        dad.addAdjacency(test2);
//        dad.addAdjacency(test3);
//        dad.addAdjacency(test4);
//        test4.addAdjacency(test5);
//        pete.addAdjacency(test6);
//        pete.addAdjacency(test7);
//        pete.addAdjacency(test8);
//
//
//        TreeGraph treeGraph = new TreeGraph();
//        treeGraph.prefWidthProperty().bind(graphPane.widthProperty());
//        treeGraph.prefHeightProperty().bind(graphPane.heightProperty());
//
//        treeGraph.addGraphNode(urs);
//        treeGraph.setRootNode(urs);
//        graphPane.getChildren().add(treeGraph);

        test1.getPane().setLayoutX(50);
        test1.getPane().setLayoutY(50);
        test2.getPane().setLayoutX(100);
        test2.getPane().setLayoutY(50);


        GraphEdge edge = new GraphEdge(GraphEdge.PathType.CUBIC);
        edge.setSourceBindings(test1.getCenterXProperty(), test1.getCenterYProperty());
        edge.setDestBindings(test2.getCenterXProperty(), test2.getCenterYProperty());

        graphPane.getChildren().add(edge);
        graphPane.getChildren().add(test1.getPane());
        graphPane.getChildren().add(test2.getPane());

//        Button removeBtn = new Button("Remove Node");
//        removeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                webGraph.removeGraphNode(test4);
//                System.out.println("Width: " + webGraph.getPaneWidth() + " Height: " + webGraph.getPaneHeight());
//                System.out.println("Size: " + webGraph.getNumNodes());
//            }
//        });
//
//        Button addBtn = new Button("Add Node");
//        addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                GraphNode node = new GraphNode("TestNode 9");
//                node.addAdjacency(test1);
//                node.addAdjacency(test7);
//                webGraph.addGraphNode(test4);
//                System.out.println("Width: " + webGraph.getPaneWidth() + " Height: " + webGraph.getPaneHeight());
//                System.out.println("Size: " + webGraph.getNumNodes());
//            }
//        });
//        root.getChildren().add(removeBtn);
//        root.getChildren().add(addBtn);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
