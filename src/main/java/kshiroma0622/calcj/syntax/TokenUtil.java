package kshiroma0622.calcj.syntax;

import kshiroma0622.calcj.ProgramException;
import kshiroma0622.calcj.syntax.bracket.BeginBracket;
import kshiroma0622.calcj.syntax.bracket.EndBracket;
import kshiroma0622.calcj.syntax.operator.binary.*;
import kshiroma0622.calcj.syntax.operator.unary.NegativeOperator;
import kshiroma0622.calcj.syntax.operator.unary.PositiveOperator;
import kshiroma0622.calcj.syntax.operator.unary.UnaryOperator;
import org.apache.commons.lang.ArrayUtils;

public class TokenUtil {
    private static final char[] DIGIT_CHAR = //
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' //
                    //,'０', '１', '２', '３','４', '５', '６', '７', '８', '９', '１', '２'
            };
    private static final char[] MINUS_CHAR = {'-'};
    private static final char[] PLUS_CHAR = {'+'};
    private static final char[] MULTI_CHAR = {'*'};
    private static final char[] DIVIDEC_HAR = {'/'};
    private static final char[] BEGING_BRACKET = {'('};
    private static final char[] END_BRACKET = {')'};
    private static final char[] SYMBOL_CHAR;

    static {
        char[] temp = new char[0];
        temp = ArrayUtils.addAll(temp, MINUS_CHAR);
        temp = ArrayUtils.addAll(temp, PLUS_CHAR);
        temp = ArrayUtils.addAll(temp, MULTI_CHAR);
        temp = ArrayUtils.addAll(temp, DIVIDEC_HAR);
        temp = ArrayUtils.addAll(temp, BEGING_BRACKET);
        temp = ArrayUtils.addAll(temp, END_BRACKET);

        SYMBOL_CHAR = temp.clone();
    }

    private static final char[] BLANK = {' '};

    public static boolean isDigit(char c) {
        return ArrayUtils.contains(DIGIT_CHAR, c);
    }

    public static boolean isValue(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof Value;
    }

    public static boolean isSymbol(char c) {
        return ArrayUtils.contains(SYMBOL_CHAR, c);
    }

    //    public static boolean isSymbol(Token token ) {
    //        if( token == null ){
    //            return false ;
    //        }
    //        return token instanceof SymbolToken ;
    //    }

    public static boolean isBlank(char c) {
        return ArrayUtils.contains(BLANK, c);
    }

    public static boolean isPlusOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof PlusOperator;
    }

    public static boolean isMinusOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof MinusOperator;

    }

    public static boolean isDivideOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof DivideOperator;

    }

    public static boolean isMultipleOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof MultipleOperator;
    }

    public static boolean isBinaryOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof BinaryOperator;
    }

    public static boolean isUnaryOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof UnaryOperator;
    }

    public static boolean isBracket(Token token) {
        return isBeginBracket(token) || isEndBracket(token);
    }

    public static boolean isBeginBracket(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof BeginBracket;

    }

    public static boolean isEndBracket(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof EndBracket;
    }

    public static boolean isBound(Token token) {
        return token == Token.Bound;
    }

    public static boolean isPositiveOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof PositiveOperator;
    }

    public static boolean isNegativeOperator(Token token) {
        if (token == null) {
            return false;
        }
        return token instanceof NegativeOperator;
    }

    public static boolean isSignOperator(Token token) {
        return isPositiveOperator(token) || isNegativeOperator(token);
    }

    private static boolean isSignCondition(Token prev) {
        if (prev == null) {
            return true;
        }
        if (isBeginBracket(prev)) {
            return true;
        }
        if (isBinaryOperator(prev)) {
            return true;
        }
        return false;
    }

    public static boolean isLeftOperandCandidate(Token token) {
        // Number , ) , 
        if (isValue(token)) {
            return true;
        }
        if (isEndBracket(token)) {
            return true;
        }
        return false;
    }

    public static boolean isRightOperandCandidate(Token token) {
        // ( , Number , 正負符号
        if (isValue(token)) {
            return true;
        }
        if (isBeginBracket(token)) {
            return true;
        }
        if (isSignOperator(token)) {
            return true;
        }
        return false;
    }

    public static Token createSymbolToken(char c, int pos, int len, Token prev) {
        if (ArrayUtils.contains(MINUS_CHAR, c)) {
            if (isSignCondition(prev)) {
                return new NegativeOperator(pos, len, prev);
            } else {
                return new MinusOperator(pos, len, prev);
            }
        }
        if (ArrayUtils.contains(PLUS_CHAR, c)) {

            if (isSignCondition(prev)) {
                return new PositiveOperator(pos, len, prev);
            } else {
                return new PlusOperator(pos, len, prev);
            }
        }
        if (ArrayUtils.contains(MULTI_CHAR, c)) {
            return new MultipleOperator(pos, len, prev);
        }
        if (ArrayUtils.contains(DIVIDEC_HAR, c)) {
            return new DivideOperator(pos, len, prev);
        }
        if (ArrayUtils.contains(BEGING_BRACKET, c)) {
            return new BeginBracket(pos, len, prev);
        }
        if (ArrayUtils.contains(END_BRACKET, c)) {
            return new EndBracket(pos, len, prev);
        }
        //エラー
        throw new ProgramException();
    }

}
