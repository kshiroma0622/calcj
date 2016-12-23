package kshiroma0622.calcj.tokenize;

import kshiroma0622.calcj.Util;
import kshiroma0622.calcj.evaluate.OverflowException;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.TokenUtil;
import kshiroma0622.calcj.syntax.Value;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.List;

public class Tokenizer {

    private final String str;
    private boolean sequenceDigit = false;
    List<TokenizeError> errorList = Util.newArryaList();

    public Tokenizer(String str) {
        this.str = str;
    }

    List<Token> list = Util.newArryaList();
    int tokenizedIndex;

    public Token[] tokenize() throws TokenizeException {
        IntegerBuilder sb = null;
        tokenizedIndex = -1;

        for (int index = 0; index < str.length(); index++) {
            char c = str.charAt(index);
            if (TokenUtil.isDigit(c)) {
                if (!sequenceDigit) {
                    sequenceDigit = true;
                    sb = new IntegerBuilder();
                }
                sb.append(c);
            } else {
                if (sequenceDigit) {
                    createMyNumber(sb, index);
                }

                if (TokenUtil.isSymbol(c)) {
                    Token prev = Util.getLast(list);
                    // +-で
                    //前のトークンがオペランドとして扱えるものでなければ正負符号とみなす。

                    list.add(TokenUtil.createSymbolToken(c, index, 1, prev));
                    tokenizedIndex = index;
                } else if (TokenUtil.isBlank(c)) {
                } else {
                    TokenizeError error = new TokenizeError(//
                            TokenizeError.Type.IllegalCharacter, index, String.valueOf(c));
                    errorList.add(error);
                }
            }
        }
        if (sequenceDigit) {
            createMyNumber(sb, str.length());
        }

        if (!Util.isEmpty(errorList)) {
            throw new TokenizeException(errorList);
        }
        return list.toArray(new Token[]{});
    }

    private void createMyNumber(IntegerBuilder sb, int index) {
        if (sequenceDigit) {
            sequenceDigit = false;
        }
        int pos = -1;
        int len = -1;
        try {
            len = sb.length();
            pos = index - len;
            int i = sb.toInteger();//throws NumberFormatException

            if (len > 8) {
                TokenizeError error = new TokenizeError(//
                        TokenizeError.Type.Overflow,//
                        pos,// 
                        sb.toString());
                errorList.add(error);
            } else {
                Token prev = Util.getLast(list);
                list.add(new Value(i, index, len, prev));
            }

        } catch (NumberFormatException e) {
            TokenizeError error = new TokenizeError(//
                    TokenizeError.Type.NumberFormatError,//
                    pos,// 
                    sb.toString());
            errorList.add(error);
        } catch (OverflowException e) {
            //この中に制御が移ることはない。
            TokenizeError error = new TokenizeError(//
                    TokenizeError.Type.Overflow,//
                    pos,// 
                    sb.toString());
            errorList.add(error);
        }
        tokenizedIndex = index;
    }

    private static class IntegerBuilder {

        private final StringBuilder sb;

        public IntegerBuilder() {
            sb = new StringBuilder();
        }

        public void append(Character c) {
            sb.append(c);
        }

        public int length() {
            return sb.length();
        }

        public Integer toInteger() throws NumberFormatException {
            String str = sb.toString();
            if (StringUtils.isEmpty(str)) {
                throw new NumberFormatException();
            }
            if (!NumberUtils.isDigits(str)) {
                throw new NumberFormatException();
            }
            if (str.startsWith("0") && str.length() > 1) {
                throw new NumberFormatException();
            }
            if (str.startsWith("-")) {
                Util.throwPGError();
            }
            return new Integer(str);
        }

        public String toString() {
            return sb.toString();
        }
    }

    public static Token[] tokenize(String inputString) throws TokenizeException {
        Tokenizer tokenizer = new Tokenizer(inputString);
        return tokenizer.tokenize();
    }

}
