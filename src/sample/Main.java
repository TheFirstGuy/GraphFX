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
        graphPane.prefWidthProperty().bind(graphPane.widthProperty());
        graphPane.prefHeightProperty().bind(graphPane.heightProperty());
        graphPane.setPickOnBounds(false);

        webGraph.addGraphNode(test4);

        graphPane.getChildren().add(webGraph);


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
