package kshiroma0622.calcj.test;


import junit.framework.TestCase;
import kshiroma0622.calcj.Util;
import kshiroma0622.calcj.tokenize.TokenizeError;
import kshiroma0622.calcj.tokenize.TokenizeException;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.bracket.BeginBracket;
import kshiroma0622.calcj.syntax.bracket.EndBracket;
import kshiroma0622.calcj.syntax.operator.binary.DivideOperator;
import kshiroma0622.calcj.syntax.operator.binary.MinusOperator;
import kshiroma0622.calcj.syntax.operator.binary.MultipleOperator;
import kshiroma0622.calcj.syntax.operator.binary.PlusOperator;
import kshiroma0622.calcj.syntax.operator.unary.NegativeOperator;
import kshiroma0622.calcj.syntax.operator.unary.PositiveOperator;
import kshiroma0622.calcj.tokenize.Tokenizer;

import java.util.List;

public class TokenizeTest extends TestCase {

    public void testTokenize() {
        doTokenize("1+34+445+343   +++ 3", true);
        doTokenize("0001", false);
        doTokenize("-0001", false);
        doTokenize("123456789", false);
        doTokenize("-123456789", false);
        doTokenize("-12345678", true);
        doTokenize("12345678", true);
        doTokenize("12++3--45678", true);
        doTokenize("123+-/*)(", true);
        doTokenize("12'3+-/*)(", false);
        doTokenize("23x45", false);
        doTokenize("23 45\t", false);
        doTokenize("0+0", true);
        doTokenize("1+", true);
        //and so on.
    }

    /**
     * 不正な文字がある場合の処理
     */
    public void testIllegalCharacterDetect() {
        String expression = "1 + d + 1 x 5 ＋ 3 + 4 + \t 5 ";
        List<TokenizeError> expectedList = Util.newLinkedList();
        expectedList.add(new TokenizeError(TokenizeError.Type.IllegalCharacter, 4, "d"));
        expectedList.add(new TokenizeError(TokenizeError.Type.IllegalCharacter, 10, "x"));
        expectedList.add(new TokenizeError(TokenizeError.Type.IllegalCharacter, 14, "＋"));
        expectedList.add(new TokenizeError(TokenizeError.Type.IllegalCharacter, 24, "\t"));
        // expectedList.add(new TokenizeError(Type.IllegalCharacter, 24, "\t"));

        try {
            Tokenizer.tokenize(expression);
        } catch (TokenizeException e) {
            equals(expectedList, e);
        }
    }

    /**
     * 数値が８桁を超える場合と書式がおかしい場合のエラー
     */
    public void testOverflowAndNumberFormat() {

        //String expression = "001+12+123*1234+-2345++23456+1234567+12345678+(((123456789)))";
        String expression = "999999999999999+1";
        List<TokenizeError> expectedList = Util.newLinkedList();
        // expectedList.add(new TokenizeError(TokenizeError.Type.Overflow, 1, "999999999999999"));
        expectedList.add(new TokenizeError(TokenizeError.Type.NumberFormatError, 0, "999999999999999"));
        try {
            Token[] tokens = Tokenizer.tokenize(expression);
            for (Token token : tokens) {
                System.out.print(token);
            }
            System.out.println();
            fail();
        } catch (TokenizeException e) {
            equals(expectedList, e);
        }

    }

    /**
     * 文字列から期待したトークンが生成されるかをテストする
     */
    public void testCreatToken() {
        // 文字列からトークンを生成するテスト
        doCreateToken("01", 0, Value.class, false);
        doCreateToken("1", 0, Value.class, true);
        doCreateToken("(", 0, BeginBracket.class, true);
        doCreateToken(")", 0, EndBracket.class, true);
        doCreateToken("+", 0, PositiveOperator.class, true);
        doCreateToken("-", 0, NegativeOperator.class, true);
        doCreateToken("+1", 0, PositiveOperator.class, true);
        doCreateToken("-1", 0, NegativeOperator.class, true);

        doCreateToken("1-", 1, MinusOperator.class, true);
        doCreateToken("1+", 1, PlusOperator.class, true);
        doCreateToken("*", 0, MultipleOperator.class, true);
        doCreateToken("/", 0, DivideOperator.class, true);
    }

    public void testConvertBinaryUnaryOperator() {
        //前に何もないか、演算子がある。括弧の始まりがある場合に
        // 加算と減算が正符号と負符号に変換される。
        String expression = "-1+-3-(-3)*-(+3)+(3";
        doCreateToken(expression, 0, NegativeOperator.class, true);
        doCreateToken(expression, 2, PlusOperator.class, true);
        doCreateToken(expression, 3, NegativeOperator.class, true);
        doCreateToken(expression, 5, MinusOperator.class, true);
        doCreateToken(expression, 7, NegativeOperator.class, true);
        doCreateToken(expression, 10, MultipleOperator.class, true);
        doCreateToken(expression, 11, NegativeOperator.class, true);
        doCreateToken(expression, 13, PositiveOperator.class, true);
        doCreateToken(expression, 16, PlusOperator.class, true);
    }

    private void equals(List<TokenizeError> expectedList, TokenizeException e) {
        for (int i = 0; i < e.getErrorList().size(); i++) {
            TokenizeError error = e.getErrorList().get(i);
            int actualIndex = error.getIndex();
            String actualStr = error.getInputString();
            TokenizeError expectedError = expectedList.get(0);

            int expectedIndex = expectedError.getIndex();
            String expectedStr = expectedError.getInputString();
            if (actualIndex != expectedIndex || !expectedStr.equals(actualStr)) {
                System.out.println("Actual:" + actualIndex + ":" + actualStr);
                System.out.println("Expected:" + expectedIndex + ":"
                        + expectedStr);

                fail();
            }
            expectedList.remove(0);
        }
        if (!expectedList.isEmpty()) {
            fail();
        }
    }

    private void doTokenize(String expression, boolean expectsSuccess) {
        try {
            Tokenizer.tokenize(expression);
            if (!expectsSuccess) {
                fail();
            }
        } catch (TokenizeException e) {
            if (expectsSuccess) {
                fail();
            }
        }
    }

    /**
     * strには一トークン分だけをいれること。
     */
    private void doCreateToken(String str, int index, Class expectedClass,
                               boolean success) {

        try {
            Token[] token = Tokenizer.tokenize(str);
            if (success) {
                if (token == null || token[index] == null) {
                    fail();
                }
                if (!token[index].getClass().equals(expectedClass)) {
                    fail();
                }
            } else {
                if (token[index].getClass().equals(expectedClass)) {
                    fail();
                }
            }
        } catch (TokenizeException e1) {
            if (success) {
                fail();
            }
        }
    }
}
