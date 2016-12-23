package kshiroma0622.calcj.syntax;

public class SentenceBound extends AbstractToken {
    public SentenceBound(){
        this(-1,-1);
    }
    
    public SentenceBound(int pos, int length) {
        super(pos, length,null);
    }

    public String dump() {
        return "$";
    }
}
