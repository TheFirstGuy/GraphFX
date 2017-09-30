package org.graphsfx.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.chart.Chart;
import org.graphsfx.model.GraphEdge;
import org.graphsfx.model.GraphNode;
import org.graphsfx.util.TwoKeyMap;

/**
 * Created by Urs on 9/23/2017.
 */
public abstract class Graph extends Chart{

    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {

    }

    public void nodeNameChanged(){
        requestChartLayout();

        // TODO: implement
        //updateLegend();
    }



    // Getters & Setters ===============================================================================================

    /**
     * Creates a graphEdge and triggers a new rendering of the graph
     * @param graphNode Source node
     * @param elementAdded Node connected to source
     */
    public void createGraphEdge(GraphNode graphNode, GraphNode elementAdded) {

        // Verify that edge hasnt already been created
        if(!this.edgeHashMap.containsKeyPair(graphNode, elementAdded)){

            GraphEdge edge = new GraphEdge(this.pathType);
            edge.setSourceBindings(graphNode.getCenterXProperty(), graphNode.getCenterYProperty());
            edge.setDestBindings(elementAdded.getCenterXProperty(), elementAdded.getCenterYProperty());
            edgeHashMap.putUnidirectional(graphNode, elementAdded, edge);

            // TODO: Add edge to edge layer
            // TODO: trigger refresh
        }
    }

    public void removeGraphEdge(GraphNode graphNode, GraphNode elementRemoved) {

        // Verify that edge exists

    }


    // Private Fields ==================================================================================================

    private ObservableSet<GraphNode> graphNodes = FXCollections.observableSet();

    private TwoKeyMap<GraphNode, GraphEdge> edgeHashMap = new TwoKeyMap<>();

    private GraphEdge.PathType pathType = GraphEdge.PathType.STRAIGHT;
}
