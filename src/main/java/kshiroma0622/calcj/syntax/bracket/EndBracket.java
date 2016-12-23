package kshiroma0622.calcj.syntax.bracket;


import kshiroma0622.calcj.syntax.AbstractToken;
import kshiroma0622.calcj.syntax.Token;

public class EndBracket extends AbstractToken {
    public EndBracket(int pos, int length, Token prev) {
        super(pos, length, prev);
    }

    public String dump() {
        return ")";
    }
}
