package org.graphsfx.util;

/**
 * Data structure tracking data needed to pan the graph on mouse drag
 */
public class MouseDragData{


    /**
     * Calculates the delta X and sets the lastX to the current X
     * @param currentX the current X coordinate of the mouse
     * @return the delta x
     */
    public double deltaX(double currentX){
        double deltaX = this.lastX - currentX;
        this.lastX = currentX;
        return deltaX;
    }

    /**
     * Calculates the delta Y and sets the lastY to the current Y
     * @param currentY the current Y coordinate of the mouse
     * @return the delta y
     */
    public double deltaY(double currentY){
        double deltaY = this.lastY - currentY;
        this.lastY = currentY;
        return deltaY;
    }

    /**
     * Flag determining if the data is valid (if for the current mouse drag)
     */
    public boolean valid = false;

    /**
     * The last known position of the mouse
     */
    public double lastX = Double.NaN;
    public double lastY = Double.NaN;

}