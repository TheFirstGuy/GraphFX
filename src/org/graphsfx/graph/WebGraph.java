package org.graphsfx.graph;

/**
 * Created by TheFirstGuy on 10/9/2017.
 */
public class WebGraph extends Graph {

    private static final double DEFAULT_EDGE_LENGTH = 25;

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

    // Private Fields ==================================================================================================

    private double edgeLength;

}
