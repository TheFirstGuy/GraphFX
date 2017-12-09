package org.graphsfx.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import org.graphsfx.model.GraphEdge;
import org.graphsfx.model.GraphNode;
import org.graphsfx.util.MouseDragData;
import org.graphsfx.util.TwoKeyMap;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Urs on 9/23/2017.
 */
public abstract class Graph extends Chart{


    private static double DEFAULT_SCALING = 0.01;
    private static double MAX_SCALING = 3.0;

    // Public Methods ==================================================================================================

    /**
     * Adds a GraphNode to be rendered
     * @param graphNode GraphNode to be added
     */
    public void addGraphNode(GraphNode graphNode){
        this.graphNodes.add(graphNode);
    }

    /**
     * Removes a GraphNode to be rendered
     * @param graphNode The node to be removed
     */
    public void removeGraphNode(GraphNode graphNode){
        if(this.graphNodes.contains(graphNode)){
            this.graphNodes.remove(graphNode);
        }
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

            // Add the new direction for edge
            this.edgeHashMap.putUnidirectional(graphNode, elementAdded, edge);

            // If the edge is not already in the edgeLayer
            if(!this.edgeLayer.getChildren().contains(edge)){
                // Add edge to edge layer for rendering
                this.edgeLayer.getChildren().add(edge);
                System.out.println("Edge added");
            } else{
                System.out.println("Edge already exists");
            }




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
        if(this.edgeHashMap.containsKeyPairBidirectional(graphNode, elementRemoved)){ System.out.println("Contains Pair!");}
        GraphEdge edge = this.edgeHashMap.removeUnidirectional(graphNode, elementRemoved);
        if(this.edgeHashMap.containsKeyPairBidirectional(graphNode, elementRemoved)){ System.out.println("Still Contains Pair!");}
        else{ System.out.println("Removed pair");}
        // Check if the edge needs to be removed from rendering
        if(edge != null && !this.edgeHashMap.containsKeyPairBidirectional(graphNode, elementRemoved)){
            this.edgeLayer.getChildren().remove(edge);
        }
    }

    public int getNumNodes(){return this.graphNodes.size();}

    public boolean isDraggable(){return this.draggable;}

    public void setDraggable(boolean draggable){this.draggable = draggable;}

    public boolean isPannable(){return this.pannable;}

    public void setPannable(boolean pannable){this.pannable = pannable;}

    public boolean isZoomable(){return  this.zoomable;}

    public void setZoomable(boolean zoomable){this.zoomable = zoomable;}

    public double getPaneWidth(){
        return this.graphNodeLayer.getPrefWidth();
    }

    public double getPaneHeight(){
        return this.graphNodeLayer.getPrefHeight();
    }

    // Protected Methods ===============================================================================================
    protected Graph(){

        // Add children layers
        this.getChildren().add(this.edgeLayer);
        this.getChildren().add(this.graphNodeLayer);
        this.getChildren().add(this.labelLayer);

        // Bind the child layers to the size of the graph
        this.edgeLayer.prefHeightProperty().bind(this.prefHeightProperty());
        this.edgeLayer.prefWidthProperty().bind(this.prefWidthProperty());
        this.graphNodeLayer.prefHeightProperty().bind(this.prefHeightProperty());
        this.graphNodeLayer.prefWidthProperty().bind(this.prefWidthProperty());
        this.labelLayer.prefHeightProperty().bind(this.prefHeightProperty());
        this.labelLayer.prefWidthProperty().bind(this.prefWidthProperty());

        // allow labellayer to be clicked through
        this.labelLayer.setPickOnBounds(false);

        this.labelLayer.setStyle("-fx-border-style: solid; " +
                "-fx-background-color: #000000;");
        initializeListeners();

    }
    // Private Methods =================================================================================================

    /**
     * Initializes listeners for Graph Fields
     */
    private void initializeListeners(){

        // Handles the addition and removal of graphnodes
        this.graphNodes.addListener(new SetChangeListener<GraphNode>() {
            @Override
            public void onChanged(Change<? extends GraphNode> change) {
                if(change.wasAdded()){
                    System.out.println("Added!");
                    GraphNode graphNode = change.getElementAdded();
                    graphNode.setGraph(Graph.this);

                    // Handle adjacencies
                    for(GraphNode other : graphNode.getAdjacencies()){
                        System.out.println("Added: " + other.getLabelText());

                        // Add edges
                        Graph.this.createGraphEdgeUnidirectional(graphNode, other);

                        // Add node to set, this should not loop since it is a set
                        Graph.this.graphNodes.add(other);

                    }

                    // Add to rendering layers
                    Graph.this.graphNodeLayer.getChildren().add(graphNode.getPane());
                    Graph.this.layoutLabel(graphNode);


                }
                else if(change.wasRemoved()){
                    GraphNode graphNode = change.getElementRemoved();

                    // Handle adjacencies
                    for(GraphNode other : graphNode.getAdjacencies()){

                        // Remove edges bidirectionally
                        Graph.this.removeGraphEdgeBidirectional( other, graphNode);
                    }

                    // Remove from rendering
                    Graph.this.graphNodeLayer.getChildren().remove(graphNode.getPane());
                    Graph.this.labelLayer.getChildren().remove(graphNode.getLabel());
                    graphNode.setGraph(null);
                }
            }
        });

        // Handles graph panning
        setOnMouseDragged(event -> {
            if(this.dragData.valid && this.pannable){
                double deltaX = this.dragData.deltaX(event.getScreenX());
                double deltaY = this.dragData.deltaY(event.getScreenY());

                // Account for scaling
                deltaX /= getScaleX();
                deltaY /= getScaleY();

                // Move all graphNodes by the deltas
                Iterator<GraphNode> itr = this.graphNodes.iterator();
                GraphNode currentNode;
                double layoutX, layoutY;

                while(itr.hasNext()){
                    currentNode = itr.next();
                    layoutX = currentNode.getPane().getLayoutX() - deltaX;
                    layoutY = currentNode.getPane().getLayoutY() - deltaY;
                    currentNode.getPane().setLayoutX(layoutX);
                    currentNode.getPane().setLayoutY(layoutY);
                }
            }
            else{
                if(event.getButton() == MouseButton.SECONDARY) {
                    this.dragData.valid = true;
                    this.dragData.lastX = event.getScreenX();
                    this.dragData.lastY = event.getScreenY();
                }
            }
        });

        setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                this.dragData.valid = false;
            }
        });

        // Handles centering the graph once placed
        setOnMouseClicked(event -> {
            // If double right click
            if(event.getButton() == MouseButton.SECONDARY &&
                    event.getClickCount() == 2){
                centerGraph();
            }
        });

        // Handles graph zooming
        setOnScroll(event -> {
            if(this.zoomable) {
                double scaleX = getScaleX() + (event.getDeltaY() * DEFAULT_SCALING);
                double scaleY = getScaleY() + (event.getDeltaY() * DEFAULT_SCALING);

                if (scaleX > 0 &&
                        scaleY > 0 &&
                        scaleX < MAX_SCALING &&
                        scaleY < MAX_SCALING) {
                    setScaleX(scaleX);
                    setScaleY(scaleY);
                }
            }
        });
    }

    /**
     * Shifts all graph nodes to be "on screen" by finding the extreme minimum node coordinates and translating
     * all nodes so that those coordinates are not negative. No shift will occur if all node coordinates are positive.
     */
    protected void centerGraph(){
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double layoutX = 0;
        double layoutY = 0;
        Iterator<GraphNode> itr = this.graphNodes.iterator();
        GraphNode currentNode;

        // Find the minimum coordinates
        while(itr.hasNext()){
            currentNode = itr.next();
            layoutX = currentNode.getPane().getLayoutX();
            layoutY = currentNode.getPane().getLayoutY();

            minX = (layoutX < minX) ? layoutX : minX;
            minY = (layoutY < minY) ? layoutY : minY;
        }

        // Adjust the min values so they can be added
        minX *= -1;
        minY *= -1;
        // Translate all nodes by at least the minX and minY
        itr = this.graphNodes.iterator();
        while(itr.hasNext()){
            currentNode = itr.next();
            layoutX = currentNode.getPane().getLayoutX() + minX;
            layoutY = currentNode.getPane().getLayoutY() + minY;
            currentNode.getPane().setLayoutX(layoutX);
            currentNode.getPane().setLayoutY(layoutY);
        }
    }

    /**
     * Checks if the given node collides with a node that is already placed. This is a brute force method and should
     * be reworked with a spactial hashmap
     * @param graphNode The node to verify collision against
     * @return 'true' if the graphnode collides with an existing node. 'false' otherwise.
     **/
    protected boolean checkCollision(GraphNode graphNode){
        boolean collides = false;
        Iterator<GraphNode> itr = this.graphNodes.iterator();
        Bounds newBounds = new BoundingBox(graphNode.getPane().getLayoutX(),
                                graphNode.getPane().getLayoutY(),
                                graphNode.getPane().getPrefWidth(),
                                graphNode.getPane().getPrefHeight());
        Bounds currentBounds;
        GraphNode currentNode;

        // Loop through and check against all other nodes
        while(itr.hasNext() && !collides){
            currentNode = itr.next();
            // Do not check against self
            if(currentNode != graphNode){
                currentBounds = new BoundingBox(currentNode.getPane().getLayoutX(),
                                    currentNode.getPane().getLayoutY(),
                                    currentNode.getPane().getPrefWidth(),
                                    currentNode.getPane().getPrefHeight());
                if(newBounds.intersects(currentBounds)){
                    System.out.println("CurrentBounds: " + currentBounds + " NewBounds: " + newBounds);
                    collides = true;
                }
            }
        }
        return collides;
    }

    /**
     * Laysout the graphnode's label
     * @param graphNode The source graphnode to label
     */
    protected void layoutLabel(GraphNode graphNode){
        Label label = graphNode.getLabel();

        // Check that the label hasnt been layedout already
        if(!this.labelLayer.getChildren().contains(label)){
            // Bind to nodes layout property
            label.layoutXProperty().bind(graphNode.getPane().layoutXProperty());
            label.layoutYProperty().bind(graphNode.getCenterYProperty());

            // Set translation (offset)
            label.setTranslateX(graphNode.getPane().getPrefWidth() * 1.1);

            // Add to label pane
            this.labelLayer.getChildren().add(label);
            //this.labels.add(label);
        }
    }

    /**
     * Sets the preferred width and height of the graph based on the coordates of the rightmost node.
     */
    protected void autoSizeGraph(){
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double layoutX = 0;
        double layoutY = 0;
        Iterator<GraphNode> itr = this.graphNodes.iterator();
        GraphNode currentNode;

        // Find the minimum coordinates
        while(itr.hasNext()){
            currentNode = itr.next();
            layoutX = currentNode.getPane().getLayoutX();
            layoutY = currentNode.getPane().getLayoutY();

            maxX = (layoutX > maxX) ? layoutX : maxX;
            maxY = (layoutY > maxY) ? layoutY : maxY;
        }

        setPrefWidth(maxX);
        setPrefHeight(maxY);
    }

    // Protected Fields ================================================================================================

    protected ObservableSet<GraphNode> graphNodes = FXCollections.observableSet();

    protected TwoKeyMap<GraphNode, GraphEdge> edgeHashMap = new TwoKeyMap<>();

    protected Pane edgeLayer = new Pane();

    protected Pane graphNodeLayer = new Pane();

    protected Pane labelLayer = new Pane();

    protected HashSet<GraphNode> placed = new HashSet<>();

    protected HashSet<GraphNode> layedOut = new HashSet<>();

    protected GraphEdge.PathType pathType = GraphEdge.PathType.STRAIGHT;

    protected MouseDragData dragData= new MouseDragData();

    protected  boolean draggable = true;

    protected  boolean pannable = true;

    protected boolean zoomable = true;

    // Inner Classes ===================================================================================================


}
