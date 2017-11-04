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

        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for(StackTraceElement element: ste){
            System.out.println(element);
        }
        // Snap top and left
        top = snapPosition(top);
        left = snapPosition(left);
        setWidth(width);
        setHeight(height);
        System.out.println("Top: " + top + " Left: " + left + " Width: " + width + " Height: " + height);

    }

    // Getters & Setters ===============================================================================================

    public void setEdgeLength(double edgeLength){
        this.edgeLength = edgeLength;
    }

    public double getEdgeLength(){
        return this.edgeLength;
    }

    // Private Fields ==================================================================================================

    private double edgeLength;

}
