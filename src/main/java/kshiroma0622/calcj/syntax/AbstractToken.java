package kshiroma0622.calcj.syntax;

public abstract class AbstractToken implements Token {
    //protected static final Log log = LogFactory.getLog(Token.class);
    private int pos;
    private int length;
    private Token prevToken;

    //    private String source ;

    //    private AbstractToken() {
    //        pos = -1;
    //        length = -1;
    //        //        source = null;
    //    }

    protected AbstractToken(int pos, int length, Token prevToken) {
        this.pos = pos;
        this.length = length;
        this.prevToken = prevToken;
        //        this.source = source;
    }

    public final int getLength() {
        return length;
    }

    public final int getPos() {
        return pos;
    }

    //    public String getSource() {
    //        return source ;
    //    }

    public final String toString() {
        return dump();
    }

    public final Token getPrevToken() {
        return prevToken;
    }
}
