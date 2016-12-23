package kshiroma0622.calcj.evaluate;

import kshiroma0622.calcj.syntax.Token;

public class ZeroDivideException extends EvaluateException {
    private static final long serialVersionUID = 1L;

    private Token token;

    public Token getErrorToken() {
        return token;
    }

    public ZeroDivideException(Token token) {
        this.token = token;
    }

}
