package org.graphsfx.model;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Created by Urs on 9/2/2017.
 */
public class GraphEdge extends Path{

    /**
     * Defines what Path element will be generated
     */
    public static enum PathType{ STRAIGHT, CUBIC, QUADRATIC, ARC };

    /**
     * Default constructor
     */
    public GraphEdge(){
        this(PathType.STRAIGHT);
    }

    /**
     * Constructor.
     * @param pathType Determines the PathElement that will be initialized.
     */
    public GraphEdge(PathType pathType){
        this.pathType = pathType;

        switch(pathType) {

            case STRAIGHT:
                this.pathElement = new LineTo();
                break;

            case CUBIC:
                this.pathElement = new CubicCurveTo();
                break;

            case QUADRATIC:
                this.pathElement = new QuadCurveTo();
                break;

            case ARC:
                this.pathElement = new ArcTo();
                break;

            default:
                this.pathElement = new LineTo();
                break;
        }

        initializeBinds();
        getElements().add(this.moveTo);
        getElements().add(this.pathElement);
        setStrokeWidth(3);

        // Set default color to gray
        setStroke(Color.valueOf("#c2c3c4"));
        //this.getElements().addAll(this.closePath);
    }




    // Setters & Getters ===============================================================================================

    /**
     * Binds a source coordinate to the start of the GraphEdge
     * @param sourceX source X coordinate
     * @param sourceY source Y coordinate
     */
    public void setSourceBindings(DoubleProperty sourceX, DoubleProperty sourceY){
        this.moveTo.xProperty().unbind();
        this.moveTo.yProperty().unbind();
        this.moveTo.xProperty().bind(DoubleBinding.doubleExpression(sourceX));
        this.moveTo.yProperty().bind(DoubleBinding.doubleExpression(sourceY));
    }

    /**
     * Binds a destination coordinate to the end of the GraphEdge
     * @param destX destination X coordinate
     * @param destY destination Y coordinate
     */
    public void setDestBindings(DoubleProperty destX, DoubleProperty destY){
        this.destX.unbind();
        this.destY.unbind();
        this.destX.bind(DoubleBinding.doubleExpression(destX));
        this.destY.bind(DoubleBinding.doubleExpression(destY));

    }




    public DoubleProperty getDestXProperty(){
        return this.destX;
    }

    public DoubleProperty getDestYProperty(){
        return this.destY;
    }

    public double getDestX(){
        return this.destX.doubleValue();
    }

    public double getDestY(){
        return this.destY.doubleValue();
    }

    // Private methods =================================================================================================

    /**
     * Adds change listeners to the destination X and Y properties to modify the pathElement.
     */
    private void initializeBinds(){

        switch(GraphEdge.this.pathType){

            case STRAIGHT:
                // Bind the end point of the line
                ((LineTo) GraphEdge.this.pathElement).xProperty().setValue(this.destX.getValue());
                ((LineTo) GraphEdge.this.pathElement).yProperty().setValue(this.destY.getValue());
                ((LineTo) GraphEdge.this.pathElement).xProperty().bind(this.destX);
                ((LineTo) GraphEdge.this.pathElement).yProperty().bind(this.destY);
                break;

            case CUBIC:

                // Create bindings for the two pairs of control points
                CubicCurveTo curveTo = (CubicCurveTo) GraphEdge.this.pathElement;
                DoubleBinding oneThirdPointX = new DoubleBinding() {
                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }
                    @Override
                    protected double computeValue() {
                        return GraphEdge.calculateThirdPoint(GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get(), false).getX() - 100;
                    }
                };

                DoubleBinding oneThirdPointY = new DoubleBinding() {
                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }
                    @Override
                    protected double computeValue() {
                        return GraphEdge.calculateThirdPoint(GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get(), false).getY();
                    }
                };

                DoubleBinding twoThirdPointX = new DoubleBinding() {
                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }
                    @Override
                    protected double computeValue() {
                        return GraphEdge.calculateThirdPoint(GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get(), true).getX() * 1.5;
                    }
                };

                DoubleBinding twoThirdPointY = new DoubleBinding() {
                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }
                    @Override
                    protected double computeValue() {
                        return GraphEdge.calculateThirdPoint(GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get(), true).getY();
                    }
                };

                // Bind control points
                curveTo.controlX1Property().bind(GraphEdge.this.moveTo.xProperty());
                curveTo.controlY1Property().bind(GraphEdge.this.moveTo.yProperty());
                curveTo.controlX2Property().bind(twoThirdPointX);
                curveTo.controlY2Property().bind(twoThirdPointY);
                curveTo.xProperty().bind(GraphEdge.this.destX);
                curveTo.yProperty().bind(GraphEdge.this.destY);

                // Debug statement
                curveTo.yProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        System.out.println("x: " + curveTo.getX());
                        System.out.println("y: " + curveTo.getY());
                        System.out.println("x1: " + curveTo.getControlX1());
                        System.out.println("y1: " + curveTo.getControlY1());
                        System.out.println("x2: " + curveTo.getControlX2());
                        System.out.println("y2: " + curveTo.getControlY2());
                    }
                });

                break;

            case QUADRATIC:

                // Create the bindings for the X and Y control points
                QuadCurveTo quadCurveTo = ((QuadCurveTo) GraphEdge.this.pathElement);

                DoubleBinding controlX = new DoubleBinding() {
                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }
                    @Override
                    protected double computeValue() {
                        return calculateMidPoint( GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get()).getX() * QUADRATIC_X_OFFSET;
                    }
                };

                DoubleBinding controlY = new DoubleBinding() {
                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }

                    @Override
                    protected double computeValue() {
                        return calculateMidPoint( GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get()).getY() * QUADRATIC_Y_OFFSET;
                    }
                };

                quadCurveTo.controlXProperty().bind(controlX);
                quadCurveTo.controlYProperty().bind(controlY);
                quadCurveTo.xProperty().bind(GraphEdge.this.destX);
                quadCurveTo.yProperty().bind(GraphEdge.this.destY);

                break;

            case ARC:
                // Create bindings for X and Y radius
                ArcTo arcTo = ((ArcTo) GraphEdge.this.pathElement);

                DoubleBinding radius = new DoubleBinding() {

                    {
                        super.bind(GraphEdge.this.destX,
                                GraphEdge.this.destY,
                                GraphEdge.this.moveTo.xProperty(),
                                GraphEdge.this.moveTo.yProperty());
                    }

                    @Override
                    protected double computeValue() {

                        // Radius is half the distance
                        return calculateDistance(GraphEdge.this.destX.get(),
                                GraphEdge.this.destY.get(),
                                GraphEdge.this.moveTo.xProperty().get(),
                                GraphEdge.this.moveTo.yProperty().get()) / ARC_SCALING;
                    }
                };

                arcTo.radiusXProperty().bind(radius);
                arcTo.radiusYProperty().bind(radius);
                arcTo.xProperty().bind(GraphEdge.this.destX);
                arcTo.yProperty().bind(GraphEdge.this.destY);

                break;

            default:
                break;
        }
    }

    /**
     * Calculates the midpoint between two points
     * @param sourceX source x value
     * @param sourceY source y value
     * @param destX destination x value
     * @param destY destination y value
     * @return midpoint between two points
     */
    private static Point2D calculateMidPoint(double sourceX, double sourceY, double destX, double destY){
        return new Point2D((sourceX + destX) / 2, (sourceY + destY) / 2);
    }

    /**
     * Calculates a point that is either a third or two thirds points
     * @param sourceX source x value
     * @param sourceY source y value
     * @param destX destination x value
     * @param destY destination y value
     * @param secondThird if true then will calculate the midpoint was 2 / 3 away from the source, otherwise 1 / 3
     * @return returns a point either 2/3 or 1/3 away from the source
     */
    private static Point2D calculateThirdPoint(double sourceX, double sourceY, double destX, double destY, boolean secondThird){
        Point2D point;
        if(!secondThird){
            point = new Point2D(((sourceX + destX ) / 2), ((sourceY + destY) / 2));
        }
        else{
            point = new Point2D(((sourceX + destX) / 2), (sourceY + destY) / 2);
        }
        return point;
    }

    /**
     * Calculates distance between two points
     * @param sourceX source x value
     * @param sourceY source y value
     * @param destX destination x value
     * @param destY destination y value
     * @return distance between the two points
     */
    private static double calculateDistance(double sourceX, double sourceY, double destX, double destY){
        double distance = Math.sqrt(Math.pow((sourceX - destX), 2) + Math.pow((sourceY - destY), 2));
        System.out.println(distance);
        return distance;
    }

    // Private Fields ==================================================================================================

    private MoveTo moveTo = new MoveTo();
    private ClosePath closePath = new ClosePath();

    private DoubleProperty destX = new SimpleDoubleProperty();
    private DoubleProperty destY = new SimpleDoubleProperty();

    private PathElement pathElement;

    private PathType pathType;

    private static final double ARC_SCALING = 1.5;
    private static final double QUADRATIC_X_OFFSET = 0.5;
    private static final double QUADRATIC_Y_OFFSET = 0.65;
}
