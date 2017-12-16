package org.graphsfx.graph;

import javafx.scene.layout.Pane;
import org.graphsfx.model.GraphNode;

import java.util.ArrayList;
import java.util.HashSet;

public class TreeGraph extends Graph {

    private static final double DEFAULT_LAYER_SIZE = 250;
    private static final boolean DEFAULT_DRAGGABLE = false;

    public TreeGraph(){
        this.prefLayerSize = DEFAULT_LAYER_SIZE;
        this.draggable = DEFAULT_DRAGGABLE;
    }
    // Public Methods ==================================================================================================

    public void setRootNode(GraphNode rootNode) throws CircularReferenceException{
        HashSet<GraphNode> explored = new HashSet<GraphNode>();
        try {
            verifyTree(rootNode, explored);
        } catch (CircularReferenceException ex){
            throw ex;
        }

        this.rootNode = rootNode;

    }

    public GraphNode getRootNode(){return this.rootNode;}

    public void setPrefLayerSize(double prefLayerSize){this.prefLayerSize = prefLayerSize;}

    public double getPrefLayerSize(){return this.prefLayerSize;}

    // Protected Methods ===============================================================================================
    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {

        System.out.println("Layout out children");
        // Snap top and left
        top = snapPosition(top);
        left = snapPosition(left);
        setWidth(width);
        setHeight(height);

        // Position nodes using grid
        if(rootNode != null){
            // reset grid

            this.grid.layers.clear();
            layoutNodes(this.rootNode, 0, 0);

            // Loop through the grid and calculate coordinates
            Dimension dimension = new Dimension();
            dimension.width = 0;
            dimension.height = 0;

            findMaxDimensions(this.rootNode, dimension);

            double layerHeight = dimension.height;
            double layerWidth = dimension.width;

            // Take the max of the two
            if(this.prefLayerSize > layerHeight){
                layerHeight = new Double(this.prefLayerSize);
            }

            GraphNode currentNode;
            // Layer loop
            for(int i = 0; i < this.grid.layers.size(); i++) {
                // Cell loop
                ArrayList<GraphNode> currentLayer = this.grid.layers.get(i);
                for (int j = 0; j < currentLayer.size(); j++) {
                    currentNode = currentLayer.get(j);
                    if (currentNode != null) {
                        System.out.println("Found: " + currentNode.getName() + " at " + i + "," + j);
                        currentNode.getPane().setLayoutX(i * layerHeight);
                        currentNode.getPane().setLayoutY(j * layerWidth);
                        layoutLabel(currentNode);
                    }
                }
            }
        }
    }

    // Private Methods =================================================================================================

    /**
     * Recursive function which lays out nodes into a distributed tree formation.
     * @param rootNode the GraphNode to be placed
     * @param layer the layer in the tree/grid to place the node
     * @param startPos The starting position for the node
     * @return the index in the layer the node was placed
     */
    int layoutNodes(GraphNode rootNode, int layer, int startPos) {
        int position = this.grid.addToLayer(rootNode, layer, startPos);

        // The positions of the children. Used to center this node
        int minPos = 0;
        int maxPos = 0;

        GraphNode[] children = new GraphNode[rootNode.getAdjacencies().size()];
        rootNode.getAdjacencies().toArray(children);
        for(int i = 0; i < children.length; i++){
            if(i == 0){
                minPos = layoutNodes(children[i], layer + 1, position);
            }
            else if(i == (children.length - 1)){
                maxPos = layoutNodes(children[i], layer + 1, position);
            }
            else{
                layoutNodes(children[i], layer + 1, position);
            }
        }

        int difference = maxPos - minPos;
        int newPos = minPos + (difference / 2);

        // If the root only has one child, make the newPos the position of the child
        if(rootNode.getAdjacencies().size() <= 1){
            newPos = minPos;
        }


        // adjust the position of the root node based on children
        if( this.grid.moveTo(rootNode, layer, position, newPos) ){
            position = newPos;
        }

        return position;


    }

    /**
     * Recursively traverses the tree from the root node and checks for circular dependencies
     * @param rootNode The root GraphNode of the tree
     * @param explored The set of nodes already explored.
     * @throws CircularReferenceException
     */
    private void verifyTree(GraphNode rootNode, HashSet<GraphNode> explored)throws CircularReferenceException{
        if(explored.contains(rootNode)){
            throw new CircularReferenceException("The node with label \"" + rootNode.getName() + "\" as a circular" +
                    " reference");
        }
        else{
            explored.add(rootNode);
            for(GraphNode child: rootNode.getAdjacencies()){
                verifyTree(child, explored);
            }
        }
    }

    /**
     * Recursively traverses the tree from the root and returns the maximum width and height
     * @param rootNode The root GraphNode of the tree
     * @param dimension The dimensions of the
     */
    private void findMaxDimensions(GraphNode rootNode, Dimension dimension){
        Pane pane = rootNode.getPane();
        if(dimension.width < pane.getPrefWidth()){
            dimension.width = pane.getPrefWidth();
        }
        if(dimension.height < pane.getPrefHeight()){
            dimension.height = pane.getPrefHeight();
        }

        for(GraphNode child: rootNode.getAdjacencies()){
            findMaxDimensions(child, dimension);
        }
    }

    // Private Fields ==================================================================================================
    /**
     * The root of the tree to be rendered
      */
    private GraphNode rootNode;

    /**
     * The grid representation of the tree
     */
    private Grid grid = new Grid();

    /**
     * The size of each layer (height of the cell)
     */
    private double prefLayerSize;

    // Private Helper Classes ==========================================================================================

    // Create Layout Grid
    // Each inner arraylist is a layer in the tree. With each index being a
    // cell which a graphnode will be placed. The size an location of each
    // cell will be determined by the size of the graphnode panes and other
    // settings.
    private class Grid{

        /**
         * Adds a GraphNode to the first available spot in the grid.
         * @param graphNode GraphNode to be added
         * @param layer The index of the layer the graphnode will be added too
         * @param minIndex The minimum index which the graphnode must be placed
         * @return The index the noded was added
         */
        public int addToLayer(GraphNode graphNode, int layer, int minIndex){

            // Increase the size of the grid if needed
            while(layer > layers.size() - 1){
                layers.add(new ArrayList<GraphNode>());
            }


            ArrayList<GraphNode> graphLayer = layers.get(layer);
            GraphNode currentLastNode = null;


            // Get the status of the last position
            if(graphLayer.size() > 0){
                currentLastNode = graphLayer.get(graphLayer.size() - 1);
            }


            if(currentLastNode != null){
                // Add padding cell
                graphLayer.add(null);
            }

            // Check if the last position is at least the min Index
            while(minIndex > graphLayer.size()){
                graphLayer.add(null);
            }

            graphLayer.add(graphNode);

            return (graphLayer.size() - 1);
        }

        /**
         * Adds a GraphNode to the specific position and adds padding nulls between
         * previous position and the new position. Assumes that no other graphnodes are
         * between the new and old position.
         * @param graphNode The graphnode to be moved
         * @param layer The layer where the graphnode will be moved in (the graphnode is assumed to be in this layer
         * @param currentIndex The index where the graphnode is currently located
         * @param newIndex The index where the graphnode will be moved too. Must be larger than the current index
         * @return boolean 'true' if moved successfully, 'false' otherwise
         */
        public boolean moveTo(GraphNode graphNode, int layer, int currentIndex, int newIndex){

            boolean moved = false;
            if(layers.size() > layer && newIndex > currentIndex){
                ArrayList<GraphNode> currentLayer = layers.get(layer);

                if(graphNode == currentLayer.get(currentIndex)){
                    // nullify current position
                    for(int i = currentIndex; i <= newIndex; i++){
                        if(i < currentLayer.size()){
                            currentLayer.set(i, null);
                        }
                        else{
                            currentLayer.add(null);
                        }
                    }

                    // we can add the graphnode now since we nullified the spaces
                    currentLayer.set(newIndex, graphNode);
                    moved = true;
                }
            }

            return moved;
        }

        public ArrayList<ArrayList<GraphNode>> layers = new ArrayList<ArrayList<GraphNode>>();
    }

    /**
     * Helper class used to allow passing dimensions by reference when recursively searching
     */
    private class Dimension{
        public double width;
        public double height;
    }
}
