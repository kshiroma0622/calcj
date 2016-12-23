package kshiroma0622.calcj.parse;


import kshiroma0622.calcj.syntax.SyntaxError;

public class ParseException extends Exception {
    private static final long serialVersionUID = 1L;
    private SyntaxError error;

    public ParseException(SyntaxError error) {
        this.error = error;
    }

    public SyntaxError getSyntaxError() {
        return error;
    }
}
