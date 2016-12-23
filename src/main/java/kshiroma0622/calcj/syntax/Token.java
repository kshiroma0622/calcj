package kshiroma0622.calcj.syntax;

public interface Token {
    public static final Token Bound = new SentenceBound();

    public int getPos();

    public int getLength();

    //    public String getSource() ;
    public String dump();

    // ソースの
//    public Token next() ;
    public Token getPrevToken();
}