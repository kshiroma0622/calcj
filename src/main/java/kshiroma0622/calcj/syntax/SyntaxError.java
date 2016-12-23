package kshiroma0622.calcj.syntax;

public class SyntaxError {

    public enum SyntaxErrorType {
        AsyncBracket, EmptyBlock, Other;
    }

    private Token errorToekn;
    private SyntaxErrorType type;

    public SyntaxError(SyntaxErrorType type, Token errorToekn) {
        this.type = type;
        this.errorToekn = errorToekn;
    }

    public Token getErrorToken() {
        return errorToekn;
    }

    public SyntaxErrorType getType() {
        return type;
    }

}
