package org.graphsfx.graph;

import org.graphsfx.model.GraphNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

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

        // Snap top and left
        top = snapPosition(top);
        left = snapPosition(left);
        setWidth(width);
        setHeight(height);

        // Calculate the center of the graph
        double centerX = width / 2;
        double centerY = height / 2;

        // Use the first Node in the graph as a starting point. Assumes that the graph is fully connected.
        System.out.println("Number of GraphNodes: " + this.graphNodes.size());
        if(!this.graphNodes.isEmpty()){
            GraphNode rootNode;
            Iterator<GraphNode> itr = this.graphNodes.iterator();
            // Center first GraphNode

            rootNode = itr.next();
            rootNode.getPane().setLayoutX(centerX);
            rootNode.getPane().setLayoutY(centerY);

            HashSet<GraphNode> placed = new HashSet<>();
            HashSet<GraphNode> layedOut = new HashSet<>();

            // Use Depth First Search to traverse Graph
            placed.add(rootNode);

            layoutChildren(rootNode, placed, layedOut);
        }


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

    private void layoutChildren(GraphNode graphNode, HashSet<GraphNode> placed, HashSet<GraphNode> layedOut){
        double radians;
        Iterator<GraphNode> itr = graphNode.getAdjacencies().iterator();
        int counter = 0;
        double x,y;
        GraphNode currentNode;
        double edgeLength;
        double minX = 0;
        double minY = 0;
        double maxX = 0;
        double maxY = 0;


        System.out.println(graphNode.getLabelText());
        System.out.println(graphNode.getAdjacencies().size());
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

            if(!placed.contains(currentNode)){
                radians = (1.0 / graphNode.getAdjacencies().size()) * counter * (2 * Math.PI);
                System.out.println("Radians: " + radians);
                System.out.println("DeltaX: " + Math.cos(radians) + " DeltaY: " + Math.sin(radians) );
                x = graphNode.getPane().getLayoutX() + (edgeLength * Math.cos(radians));
                y = graphNode.getPane().getLayoutY() + (edgeLength * Math.sin(radians));
                System.out.println("X: " + x + " Y: " + y );
                currentNode.getPane().setLayoutX(x);
                currentNode.getPane().setLayoutY(y);
                placed.add(currentNode);
            }

            counter++;
        }

        itr = graphNode.getAdjacencies().iterator();
        while(itr.hasNext()){
            currentNode = itr.next();
            if(!layedOut.contains(currentNode)){
                layoutChildren(currentNode, placed, layedOut);
            }
        }

    }
    // Private Fields ==================================================================================================

    private double edgeLength;

}
