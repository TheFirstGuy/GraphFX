package org.graphsfx.model;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.graphsfx.graph.Graph;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Urs on 9/2/2017.
 */
public class GraphNode {

    /**
     * Full Constructor
     * @param pane Pane to be used to render the node
     * @param label String to sit next to rendered pane
     */
    public GraphNode(Pane pane, String label){
        this.pane = pane;
        this.label.setText(label);

        initialize();
    }

    /**
     * Simple Constructor
     * @param label String to sit next to rendered pane
     */
    public GraphNode(String label){
        this.label.setText(label);

        // Set up default pane
        this.pane = new Pane();
        pane.setPrefSize(DEFAULT_PANE_WIDTH, DEFAULT_PANE_HEIGHT);

        // Set up Style for default pane
        StringBuilder style = new StringBuilder();
        style.append("-fx-border-style: solid; -fx-border-radius: ");
        style.append(DEFAULT_PANE_HEIGHT);
        style.append("; -fx-background-radius: ");
        style.append(DEFAULT_PANE_HEIGHT);
        style.append("; -fx-border-width: ");
        style.append(DEFAULT_BORDER_WIDTH);
        style.append("; -fx-border-color: #1565C0");
        style.append("; -fx-background-color: #E2E2E2");
        pane.setStyle(style.toString());

        initialize();
    }

    // Public Methods ==================================================================================================

    /**
     * Adds node to adjacency list
     * @param graphNode node to be added
     */
    public void addAdjacency(GraphNode graphNode){
        this.adjacencies.add(graphNode);
    }

    /**
     * Adds a bidirectional adjacency to the GraphNode
     * @param graphNode node to add the bi directional adjacency
     */
    public void addBidirectionalAdjacency(GraphNode graphNode){
        this.adjacencies.add(graphNode);
        graphNode.addAdjacency(this);
    }

    public boolean removeAdjacency(GraphNode graphNode){
        return this.adjacencies.remove(graphNode);
    }


    // Getters & Setters ===============================================================================================

    /**
     * @return unmodifiable set of adjacencies
     */
    public Set<GraphNode> getAdjacencies(){
        return Collections.unmodifiableSet(this.adjacencies);
    }

    public double getCenterX(){
        return this.centerX.doubleValue();
    }

    public double getCenterY(){
        return this.centerY.doubleValue();
    }

    public DoubleProperty getCenterXProperty(){
        return this.centerX;
    }

    public DoubleProperty getCenterYProperty(){
        return this.centerY;
    }

    public Graph getGraph(){
        return this.graph;
    }

    public String getLabelText(){
        return this.label.getText();
    }

    public String getName(){
        return this.name.getValue();
    }

    public Pane getPane(){
        return this.pane;
    }

    public void setName(String name){
        this.name.set(name);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setPane(Pane pane){
        this.pane = pane;
    }

    public Label getLabel() {
        return this.label;
    }

    public void setLabel(Label label){
        this.label = label;
    }

    // Private Methods =================================================================================================

    private void setLabelText( String text ){
        this.label.setText(text);
    }
    /**
     * Initializes all nodes
     */
    private void initialize(){
        setCenterBindings();
        initAdjacencyListener();
        setDragPane();
    }

    /**
     * Initializes the adjacency listener
     */
    private void initAdjacencyListener(){
        // Add listener for adjacency
        this.adjacencies.addListener(new SetChangeListener<GraphNode>() {
            @Override
            public void onChanged(Change<? extends GraphNode> change) {
                if(GraphNode.this.getGraph() != null){
                    if(change.wasAdded()){
                        GraphNode.this.getGraph().createGraphEdgeUnidirectional(GraphNode.this, change.getElementAdded());
                    }
                    else if(change.wasRemoved()){
                        GraphNode.this.getGraph().removeGraphEdgeUnidirectional(GraphNode.this, change.getElementRemoved());
                    }
                }
            }
        });
    }


    /**
     * Adds a OnDrag event handler
     */
    private void setDragPane(){

        this.pane.setOnMouseDragged(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Pane thisPane = GraphNode.this.pane;
                // Calculate coordinates relative to the graph Node.
                double deltaX = event.getScreenX() - thisPane.localToScreen(thisPane.getBoundsInLocal()).getMinX();
                double deltaY = event.getScreenY() - thisPane.localToScreen(thisPane.getBoundsInLocal()).getMinY();
                double newX = thisPane.getLayoutX() + deltaX;
                double newY = thisPane.getLayoutY() + deltaY;

                GraphNode.this.pane.setLayoutX(newX);
                GraphNode.this.pane.setLayoutY(newY);


            }
        });

    }

    /**
     * Binds the center coordinates of the GraphNode
     */
    private void setCenterBindings(){

        System.out.println("Width: " + pane.getPrefWidth() /2 );
        System.out.println("Height: " + (pane.getPrefHeight() / 2));
        // Bind the layout x property to be offset by half the width
        DoubleBinding xBinding = new DoubleBinding() {
            {
                super.bind(GraphNode.this.pane.layoutXProperty());
            }
            @Override
            protected double computeValue() {
                double x = GraphNode.this.pane.layoutXProperty().get();

                // Work around to correctly place edges before Graphnode is rendered
                if(GraphNode.this.pane.getWidth() == 0 ){
                    x += GraphNode.this.pane.getPrefWidth() / 2;
                }
                else{
                    x += GraphNode.this.pane.getWidth() / 2;
                }
                return x;
            }
        };

        // Bind the layout y property to be offset by half the height
        DoubleBinding yBinding = new DoubleBinding() {
            {
                super.bind(GraphNode.this.pane.layoutYProperty());
            }
            @Override
            protected double computeValue() {
                double y = GraphNode.this.pane.layoutYProperty().get();

                // Work around to correctly place edges before Graphnode is rendered
                if(GraphNode.this.pane.getHeight() == 0){
                    y += GraphNode.this.pane.getPrefHeight() / 2;
                }
                else{
                    y += GraphNode.this.pane.getWidth() / 2;
                }
                return y;
            }
        };

        this.centerX.bind(xBinding);
        this.centerY.bind(yBinding);

    }


    // Private Fields ==================================================================================================


    /**
     * JavaFX Pane to be rendered for the node
     */
    private Pane pane;

    /**
     * The label for the node
     */
    private Label label = new Label();

    /**
     * Reference to parent graph
     */
    private Graph graph = null;

    /**
     * The name property for the label
     */

    private StringProperty name = new StringPropertyBase() {
        @Override
        protected void invalidated(){
            if(getGraph() != null){
                setLabelText(getName());
                getGraph().nodeNameChanged();
            }
        }

        @Override
        public Object getBean() {
            return GraphNode.this;
        }

        @Override
        public String getName() {
            return "name";
        }
    };

    /**
     * Adjacent nodes in the graph
     */
    private ObservableSet<GraphNode> adjacencies = FXCollections.observableSet();

    /**
     * The center X and Y coordinates for the
     */
    private DoubleProperty centerX = new SimpleDoubleProperty();
    private DoubleProperty centerY = new SimpleDoubleProperty();

    /**
     * Private default sizes
     */
    private static final double DEFAULT_PANE_HEIGHT = 20.0;
    private static final double DEFAULT_PANE_WIDTH = 20.0;
    private static final double DEFAULT_BORDER_WIDTH = 3.0;
}
