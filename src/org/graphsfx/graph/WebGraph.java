package org.graphsfx.graph;

import org.graphsfx.model.GraphNode;

import java.util.Iterator;

/**
 * Created by TheFirstGuy on 10/9/2017.
 */
public class WebGraph extends Graph {

    private static final double DEFAULT_EDGE_LENGTH = 50;

    public WebGraph(){
        this.edgeLength = DEFAULT_EDGE_LENGTH;
    }

    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {

        System.out.println("Layout out children");
        // Snap top and left
        top = snapPosition(top);
        left = snapPosition(left);
        setWidth(width);
        setHeight(height);

        // Calculate the center of the graph
        double centerX = width / 2;
        double centerY = height / 2;

        // Loop through all graphnodes and lay them out if they havent been layed out yet
        GraphNode rootNode;
        Iterator<GraphNode> itr = this.graphNodes.iterator();
        while(itr.hasNext()) {
            // Center first GraphNode

            rootNode = itr.next();
            if(!placed.contains(rootNode)) {

                rootNode.getPane().setLayoutX(centerX);
                rootNode.getPane().setLayoutY(centerY);

                // Use Depth First Search to traverse Graph
                this.placed.add(rootNode);
                layoutLabel(rootNode);
                layoutChildren(rootNode);
            }

        }

        centerGraph();
        autoSizeGraph();
    }

    // Getters & Setters ===============================================================================================

    /**
     * Sets the length of the edges between GraphNodes
     * @param edgeLength length in pixels between GraphNodes
     */
    public void setEdgeLength(double edgeLength){
        this.edgeLength = edgeLength;
    }

    /**
     * Returns current edge length
     * @return the current edge length
     */
    public double getEdgeLength(){
        return this.edgeLength;
    }

    // Private Methods =================================================================================================

    /**
     * Recursively laysout the given graphnode's children
     * @param graphNode The parent graphnode which has already been placed.
     */
    private void layoutChildren(GraphNode graphNode){
        double radians;
        Iterator<GraphNode> itr = graphNode.getAdjacencies().iterator();
        int counter = 0;
        double x,y;
        GraphNode currentNode;
        double edgeLength;

        layedOut.add(graphNode);
        while(itr.hasNext()){
            currentNode = itr.next();

            // If the childNode has children of its own, give it more space
            if(currentNode.getAdjacencies().size() >= 1){
                edgeLength = this.edgeLength * 2;
            }
            else{
                edgeLength = this.edgeLength;
            }

            // Place the node
            if(!this.placed.contains(currentNode)){

                do{
                    // Calculate the angle from the originating node
                    radians = (1.0 / graphNode.getAdjacencies().size()) * counter * (2 * Math.PI);
                    x = graphNode.getPane().getLayoutX() + (edgeLength * Math.cos(radians));
                    y = graphNode.getPane().getLayoutY() + (edgeLength * Math.sin(radians));

                    // Set the coordinates
                    currentNode.getPane().setLayoutX(x);
                    currentNode.getPane().setLayoutY(y);

                    // place the nodes
                    this.placed.add(currentNode);

                    counter++;
                }while(checkCollision(currentNode));
                layoutLabel(currentNode);
            }


        }

        //  Recursively layout the children of the current node
        itr = graphNode.getAdjacencies().iterator();
        while(itr.hasNext()){
            currentNode = itr.next();
            if(!this.layedOut.contains(currentNode)){
                layoutChildren(currentNode);
            }
        }
    }


    // Private Fields ==================================================================================================

    private double edgeLength;

}
