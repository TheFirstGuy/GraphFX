package org.graphsfx.graph;

import org.graphsfx.model.GraphNode;

import java.util.ArrayList;

public class TreeGraph extends Graph {

    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {

        System.out.println("Layout out children");
        // Snap top and left
        top = snapPosition(top);
        left = snapPosition(left);
        setWidth(width);
        setHeight(height);

        // Create Layout Grid
        // Each inner arraylist is a layer in the tree. With each index being a
        // cell which a graphnode will be placed. The size an location of each
        // cell will be determined by the size of the graphnode panes and other
        // settings.





    }

    // Private Methods =================================================================================================

    /**
     * The root of the tree to be rendered
      */
    private GraphNode rootNode;


    private class Grid{

        /**
         * Adds a GraphNode to the first available spot in the grid.
         * @param graphNode GraphNode to be added
         * @param layer The index of the layer the graphnode will be added too
         * @return The index the noded was added
         */
        public int addToLayer(GraphNode graphNode, int layer){

            // Increase the size of the grid if needed
            if(layer + 1 > grid.size()){
                while(grid.size() < layer){
                    grid.add(new ArrayList<GraphNode>());
                }
            }

            ArrayList<GraphNode> graphLayer = grid.get(layer);
            GraphNode currentLastNode = null;

            // Get the status of the last position
            if(graphLayer.size() > 0){
                currentLastNode = graphLayer.get(graphLayer.size() - 1);
            }

            if(currentLastNode != null){
                // Add padding cell
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
         * @param index The index where the graphnode will be moved too
         * @return 'true' if move was successful. 'false' otherwise
         */
        public boolean moveTo(GraphNode graphNode, int layer, int index){
            boolean moved = false;

            return moved;
        }

        public ArrayList<ArrayList<GraphNode>> grid = new ArrayList<ArrayList<GraphNode>>();
    }
}
