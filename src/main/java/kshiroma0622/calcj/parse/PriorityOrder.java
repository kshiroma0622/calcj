package kshiroma0622.calcj.parse;


import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.TokenUtil;

public enum PriorityOrder {

    /**
     * Less Than
     */
    LT,
    /**
     * EQual
     */
    EQ,
    /**
     * GreaterThan
     */
    GT,
    /**
     * エラー
     */
    ER,
    /**
     * Fin
     */
    FI;

    private static final PriorityOrder[][] matrix;
    static {

        PriorityOrder[][] m = new PriorityOrder[][] {
        //  ///////////    整数 符号 +- *   (    )  $
                /* 整数 */{ ER, ER, GT, GT, ER, GT, GT },
                /* 符号 */{ LT, GT, GT, GT, LT, GT, GT },
                /* +- ' */{ LT, LT, GT, LT, LT, GT, GT },
                /* x/ ' */{ LT, LT, GT, GT, LT, GT, GT },
                /* ( '' */{ LT, LT, LT, LT, LT, EQ, ER },
                /* ) '' */{ ER, ER, GT, GT, ER, GT, GT },
                /* $ '' */{ LT, LT, LT, LT, LT, ER, FI } };
        matrix = m;
    }

    public static PriorityOrder getOrder(Token left, Token right) {
        int rightIndex = getIndex(right);
        int leftIndex = getIndex(left);
        return matrix[leftIndex][rightIndex];
    }

    private static int getIndex(Token token) {
        if (TokenUtil.isValue(token)) {
            return Number;
        }
        if (TokenUtil.isSignOperator(token)) {
            return Sign;
        }
        if (TokenUtil.isPlusOperator(token) || TokenUtil.isMinusOperator(token)) {
            return PlusMInus;
        }
        if (TokenUtil.isMultipleOperator(token)
                || TokenUtil.isDivideOperator(token)) {
            return MultiDivide;
        }
        if (TokenUtil.isBeginBracket(token)) {
            return BeginBracket;
        }
        if (TokenUtil.isEndBracket(token)) {
            return EndBracket;
        }
        if (TokenUtil.isBound(token)) {
            return Bound;
        }
        return 0;
    }

    private static final int Number = 0;
    private static final int Sign = 1;
    private static final int PlusMInus = 2;
    private static final int MultiDivide = 3;
    private static final int BeginBracket = 4;
    private static final int EndBracket = 5;
    private static final int Bound = 6;
}
