package org.graphsfx.graph;

/**
 * Created by TheFirstGuy on 10/9/2017.
 */
public class WebGraph extends Graph {


    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {

        // Snap top and left
        top = snapPosition(top);
        left = snapPosition(left);
    }


}
