package Exceptions;

public class TooFewLayersException extends Exception {
    public TooFewLayersException() {
        System.err.println("Too few layers in this network");
    }
}