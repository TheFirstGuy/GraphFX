package org.graphsfx.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.chart.Chart;
import javafx.scene.layout.Pane;
import org.graphsfx.model.GraphEdge;
import org.graphsfx.model.GraphNode;
import org.graphsfx.util.TwoKeyMap;

/**
 * Created by Urs on 9/23/2017.
 */
public abstract class Graph extends Chart{


    public Graph(){

        this.getChildren().add(this.edgeLayer);
        this.getChildren().add(this.graphNodeLayer);

        initializeListeners();

    }
    public void nodeNameChanged(){
        requestChartLayout();

        // TODO: implement
        //updateLegend();
    }



    // Getters & Setters ===============================================================================================

    /**
     * Creates a bidirectional mapping between the graphNode and elementAdded, represented by one graphEdge.
     * @param graphNode Source and destination node
     * @param elementAdded Source and destination node
     */
    public void createGraphEdgeBidirectional(GraphNode graphNode, GraphNode elementAdded){
        createGraphEdgeUnidirectional(graphNode, elementAdded);
        createGraphEdgeUnidirectional(elementAdded, graphNode);
    }

    /**
     * Creates a graphEdge and triggers a new rendering of the graph.
     * @param graphNode Source node
     * @param elementAdded Node connected to source
     */
    public void createGraphEdgeUnidirectional(GraphNode graphNode, GraphNode elementAdded) {

        // Check if instance of edge already exists
        GraphEdge edge = this.edgeHashMap.getBidirectional(graphNode, elementAdded);

        if(!this.edgeHashMap.containsKeyPairUnidirectional(graphNode, elementAdded)){

            // Create new edge if one was not found
            if(edge == null) {
                edge = new GraphEdge(this.pathType);
                edge.setSourceBindings(graphNode.getCenterXProperty(), graphNode.getCenterYProperty());
                edge.setDestBindings(elementAdded.getCenterXProperty(), elementAdded.getCenterYProperty());
            }

            this.edgeHashMap.putUnidirectional(graphNode, elementAdded, edge);

            // Add edge to edge layer for rendering
            this.edgeLayer.getChildren().add(edge);
        }
    }

    /**
     * Removes all edges from the graphNode to the elementRemoved. Edge will be removed from the rendering.
     * @param graphNode Source and destination node
     * @param elementRemoved Source and destination node
     */
    public void removeGraphEdgeBidirectional(GraphNode graphNode, GraphNode elementRemoved){
        removeGraphEdgeUnidirectional(graphNode, elementRemoved);
        removeGraphEdgeUnidirectional(elementRemoved, graphNode);
    }

    /**
     * Removes the edge from graphNode to the elementRemoved. If the last reference to the edge has been removed,
     * then it will be removed from the rendering.
     * @param graphNode source of the edge to be removed
     * @param elementRemoved destination of the edge to be removed
     */
    public void removeGraphEdgeUnidirectional(GraphNode graphNode, GraphNode elementRemoved) {
        GraphEdge edge = this.edgeHashMap.removeUnidirectional(graphNode, elementRemoved);

        // Check if the edge needs to be removed from rendering
        if(edge != null && !this.edgeHashMap.containsKeyPairBidirectional(graphNode, elementRemoved)){
            this.edgeLayer.getChildren().remove(edge);
        }
    }

    // Private Methods =================================================================================================

    private void initializeListeners(){
        this.graphNodes.addListener(new SetChangeListener<GraphNode>() {
            @Override
            public void onChanged(Change<? extends GraphNode> change) {
                if(change.wasAdded()){
                    GraphNode graphNode = change.getElementAdded();
                    graphNode.setGraph(Graph.this);

                    // Handle adjacencies
                    for(GraphNode other : graphNode.getAdjacencies()){

                        // Add edges
                        Graph.this.createGraphEdgeUnidirectional(graphNode, other);

                        // Add node to set, this should not loop since it is a set
                        Graph.this.graphNodes.add(other);

                    }

                    // Add to graph layer
                    Graph.this.graphNodeLayer.getChildren().add(graphNode.getPane());

                }
                else if(change.wasRemoved()){
                    GraphNode graphNode = change.getElementRemoved();

                    // Handle adjacencies
                    for(GraphNode other : graphNode.getAdjacencies()){

                        // Remove edges bidirectionally
                        Graph.this.removeGraphEdgeBidirectional(graphNode, other);
                    }

                    // Remove from rendering
                    Graph.this.graphNodeLayer.getChildren().remove(graphNode.getPane());
                    graphNode.setGraph(null);
                }
            }
        });
    }

    // Protected Fields ================================================================================================

    protected ObservableSet<GraphNode> graphNodes = FXCollections.observableSet();

    protected TwoKeyMap<GraphNode, GraphEdge> edgeHashMap = new TwoKeyMap<>();

    protected Pane edgeLayer = new Pane();

    protected Pane graphNodeLayer = new Pane();

    protected GraphEdge.PathType pathType = GraphEdge.PathType.STRAIGHT;
}
