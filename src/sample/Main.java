package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.graphsfx.model.GraphEdge;
import org.graphsfx.model.GraphNode;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = (Pane) FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        GraphNode test1 = new GraphNode("TestNode");


        GraphNode test2 = new GraphNode("TestNode2");

        GraphEdge edge = new GraphEdge(GraphEdge.PathType.CUBIC);
        edge.setSourceBindings(test1.getCenterXProperty(), test1.getCenterYProperty());
        edge.setDestBindings(test2.getCenterXProperty(), test2.getCenterYProperty());


        root.getChildren().add(edge);
        root.getChildren().add(test1.getPane());
        root.getChildren().add(test2.getPane());

        primaryStage.show();
        test1.getPane().setLayoutX(15);
        test1.getPane().setLayoutY(50);

        test2.getPane().setLayoutX(75);
        test2.getPane().setLayoutY(50);

        test1.addAdjacency(test2);
        test1.addAdjacency(test2);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
