package com.mateoi.gp.exceptions;

/**
 * Exception thrown by NodeFactory if its methods are called before the
 * constructors are set.
 * 
 * @author mateo
 *
 */
public class NoConstructorsSet extends Exception {

    private static final long serialVersionUID = 712937399769875460L;

    public NoConstructorsSet() {
        // TODO Auto-generated constructor stub
    }

    public NoConstructorsSet(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public NoConstructorsSet(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public NoConstructorsSet(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public NoConstructorsSet(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
