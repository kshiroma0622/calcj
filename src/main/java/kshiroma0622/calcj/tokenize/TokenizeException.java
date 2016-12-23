package kshiroma0622.calcj.tokenize;

import kshiroma0622.calcj.Util;

import java.util.List;

/**
 * 字句解析のエラー
 */
public class TokenizeException extends Throwable {
    private static final long serialVersionUID = 1L;
    private final List<TokenizeError> list;

    public TokenizeException(List<TokenizeError> list) {
        this.list = Util.newArryaList();
        this.list.addAll(list);
    }

    public List<TokenizeError> getErrorList() {
        return list;
    }
}
