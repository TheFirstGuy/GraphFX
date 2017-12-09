package org.graphsfx.graph;

public class CircularReferenceException  extends Exception{

    public CircularReferenceException(String message){
        super(message);
    }
}
