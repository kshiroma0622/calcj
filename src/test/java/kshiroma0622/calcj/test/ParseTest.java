package kshiroma0622.calcj.test;

import junit.framework.TestCase;
import kshiroma0622.calcj.Util;
import kshiroma0622.calcj.evaluate.OverflowException;
import kshiroma0622.calcj.parse.ParseException;
import kshiroma0622.calcj.parse.Parser;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.TokenUtil;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.operator.binary.DivideOperator;
import kshiroma0622.calcj.syntax.operator.binary.MinusOperator;
import kshiroma0622.calcj.syntax.operator.binary.MultipleOperator;
import kshiroma0622.calcj.syntax.operator.binary.PlusOperator;
import kshiroma0622.calcj.syntax.operator.unary.NegativeOperator;
import kshiroma0622.calcj.syntax.operator.unary.PositiveOperator;

import java.util.List;
import java.util.Stack;


public class ParseTest extends TestCase {

    private static final Token PositiveOp = new PositiveOperator(0, 0, null);
    private static final Token NegativeOp = new NegativeOperator(0, 0, null);
    private static final Token PlusOp = new PlusOperator(0, 0, null);
    private static final Token MinusOp = new MinusOperator(0, 0, null);
    private static final Token MultipleOp = new MultipleOperator(0, 0, null);
    private static final Token DivideOp = new DivideOperator(0, 0, null);

    public void testParseSucessCase() throws Exception {
        {// 1 + 2
            TokenBuilder test = new TokenBuilder();
            TokenBuilder expected = new TokenBuilder();
            test.p(1).p('+').p(2);

            expected.p(PlusOp);
            expected.p(2);
            expected.p(1);
            doParseTest(test.toArray(), expected.toStack(), true);
        }

        {// - 1 + 2 
            TokenBuilder test = new TokenBuilder();
            TokenBuilder expected = new TokenBuilder();
            test.p('-').p(1).p('+').p(2);
            expected.p(PlusOp);
            expected.p(2);
            expected.p(NegativeOp);
            expected.p(1);
            doParseTest(test.toArray(), expected.toStack(), true);
        }

        {// - 1 + 2 * 3 
            TokenBuilder test = new TokenBuilder();
            TokenBuilder expected = new TokenBuilder();
            test.p('-').p(1).p('+').p(2).p('*').p(3);
            expected.p(PlusOp);
            expected.p(MultipleOp);
            expected.p(3);
            expected.p(2);
            expected.p(NegativeOp);
            expected.p(1);
            doParseTest(test.toArray(), expected.toStack(), true);
        }

        {// - 1 + 2 * 3 - 4 / 5 
            TokenBuilder test = new TokenBuilder();
            TokenBuilder expected = new TokenBuilder();
            test.p('-').p(1).p('+').p(2).p('*').p(3).p('-').p(4).p('/').p(5);

            expected.p(MinusOp);
            expected.p(DivideOp);
            expected.p(5);
            expected.p(4);

            expected.p(PlusOp);
            expected.p(MultipleOp);
            expected.p(3);
            expected.p(2);
            expected.p(NegativeOp);
            expected.p(1);
            doParseTest(test.toArray(), expected.toStack(), true);
        }

        {// - 1 + 2 * (3 - 4) / 5 
            TokenBuilder test = new TokenBuilder();
            TokenBuilder expected = new TokenBuilder();
            test.p('-').p(1).p('+').p(2).p('*').p('(').p(3).p('-').p(4).p(')')
                    .p('/').p(5);

            expected.p(PlusOp);
            expected.p(DivideOp);
            expected.p(5);
            expected.p(MultipleOp);
            expected.p(MinusOp);
            expected.p(4);
            expected.p(3);
            expected.p(2);
            expected.p(NegativeOp);
            expected.p(1);

            doParseTest(test.toArray(), expected.toStack(), true);
        }

        {// 2 * (3 + - 4) / 5 
            TokenBuilder test = new TokenBuilder();
            TokenBuilder expected = new TokenBuilder();
            test.p(2).p('*').p('(').p(3).p('+').p('-').p(4).p(')').p('/').p(5);

            expected.p(DivideOp);
            expected.p(5);
            expected.p(MultipleOp);
            expected.p(PlusOp);
            expected.p(NegativeOp);
            expected.p(4);
            expected.p(3);
            expected.p(2);

            doParseTest(test.toArray(), expected.toStack(), true);
        }

    }

    public void testParseFailurCase() throws Exception {
        {// 1 + 
            TokenBuilder test = new TokenBuilder();
            test.p(1).p('+');
            doParseTest(test.toArray(), null, false);
        }
        {// *1  
            TokenBuilder test = new TokenBuilder();
            test.p('*').p(1);
            doParseTest(test.toArray(), null, false);
        }
        {// 1 / 
            TokenBuilder test = new TokenBuilder();
            test.p(1).p('/');
            doParseTest(test.toArray(), null, false);
        }

        {// 1 + 2 (3) 
            TokenBuilder test = new TokenBuilder();
            test.p(1).p('+').p(2).p('(').p(')').p(3);
            doParseTest(test.toArray(), null, false);
        }

        {// 1 + 2 + 3 )
            TokenBuilder test = new TokenBuilder();
            test.p(1).p('+').p(2).p('+').p(3).p(')');
            doParseTest(test.toArray(), null, false);
        }
        {// 1 + 2 + ()
            TokenBuilder test = new TokenBuilder();
            test.p(1).p('+').p(2).p('+').p('(').p(')');
            doParseTest(test.toArray(), null, false);
        }
        {// 1 + 2 * * 3
            TokenBuilder test = new TokenBuilder();
            test.p(1).p('+').p(2).p('*').p('*').p(3);
            doParseTest(test.toArray(), null, false);
        }
        {// ( * 1 + 2 )
            TokenBuilder test = new TokenBuilder();
            test.p('(').p('*').p(1).p('+').p(2).p(')');
            doParseTest(test.toArray(), null, false);
        }
        {// ( 1 + 2 ) + 3 / 5 - 3 )
            TokenBuilder test = new TokenBuilder();
            test.p('(').p(1).p('+').p(2).p(')').p('+').p(3).p('/').p(5).p('-')
                    .p(3).p(')');
            doParseTest(test.toArray(), null, false);
        }
    }

    private void doParseTest(Token[] tokens, Stack<Token> expected, boolean success) {
        try {
            Stack<Token> actual = Parser.parse(tokens);
            if (!success) {
                fail();
            }
            if (actual == null) {
                fail();
            }
            int n = actual.size();
            int m = expected.size();
            if (n != m) {
                fail();
            }
            for (int i = 0; i < n; i++) {
                Token expectedToken = expected.get(i);
                Token actualToken = actual.get(i);
                if (!expectedToken.getClass().equals(actualToken.getClass())) {
                    System.out.println("Exptected:" + expectedToken.dump());
                    System.out.println("Actual:" + actualToken.dump());
                    System.out.println();
                    fail();
                }
                if (actualToken instanceof Value) {
                    int actualValue = ((Value) actualToken).getValue();
                    int expectedValue = ((Value) expectedToken).getValue();
                    if (actualValue != expectedValue) {
                        fail();
                    }
                }
            }

        } catch (ParseException e) {
            if (success) {
                fail();
            } else {
                return;
            }
        }
    }

    private static class TokenBuilder {
        List<Token> list;

        TokenBuilder() {
            list = Util.newArryaList();
        }

        TokenBuilder p(char c) {
            Token t = TokenUtil.createSymbolToken(c, 0, 0, Util.getLast(list));
            list.add(t);
            return this;
        }

        TokenBuilder p(int value) throws Exception {
            try {
                Token t = new Value(value, 0, 0, Util.getLast(list));
                list.add(t);
                return this;
            } catch (OverflowException e) {
                throw new Exception();
            }
        }

        TokenBuilder p(Token token) {
            list.add(token);
            return this;
        }

        Token[] toArray() {
            return list.toArray(new Token[]{});
        }

        Stack<Token> toStack() {
            Stack<Token> stack = new Stack<Token>();
            stack.addAll(list);
            return stack;
        }
    }
}
