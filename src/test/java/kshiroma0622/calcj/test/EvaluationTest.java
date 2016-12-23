package kshiroma0622.calcj.test;

import junit.framework.TestCase;
import kshiroma0622.calcj.evaluate.*;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.operator.binary.DivideOperator;
import kshiroma0622.calcj.syntax.operator.binary.MinusOperator;
import kshiroma0622.calcj.syntax.operator.binary.MultipleOperator;
import kshiroma0622.calcj.syntax.operator.binary.PlusOperator;
import kshiroma0622.calcj.syntax.operator.unary.NegativeOperator;
import kshiroma0622.calcj.syntax.operator.unary.PositiveOperator;

import java.util.Stack;

public class EvaluationTest extends TestCase {
    private static final Object OF = new OverflowException();
    private static final Object ZERO = new ZeroDivideException(null);

    private enum EvaluateType {
        PLUS, MINUS, MULTIPLE, DIVIDE, POSITIVE, NEGATIVE
    }

    public void testEvaluatorPlusWithOutStack() throws Exception {
        doEvaluateWithOutStack(3, EvaluateType.PLUS, 2, 1, getStrategy());
        doEvaluateWithOutStack(0, EvaluateType.PLUS, -1, 1, getStrategy());
        doEvaluateWithOutStack(0, EvaluateType.PLUS, 1, -1, getStrategy());
        doEvaluateWithOutStack(OF, EvaluateType.PLUS, Value.MAX_VALUE - 100,
                +101, getStrategy());
        doEvaluateWithOutStack(OF, EvaluateType.PLUS, Value.MIN_VALUE + 100,
                -101, getStrategy());
    }

    public void testEvaluatorMinusWithOutStack() throws Exception {
        doEvaluateWithOutStack(1, EvaluateType.MINUS, 2, 1, getStrategy());
        doEvaluateWithOutStack(-2, EvaluateType.MINUS, -1, 1, getStrategy());
        doEvaluateWithOutStack(2, EvaluateType.MINUS, 1, -1, getStrategy());
        doEvaluateWithOutStack(1, EvaluateType.MINUS, -1, -2, getStrategy());
        doEvaluateWithOutStack(OF, EvaluateType.MINUS,
                Value.MAX_VALUE - 100, -101, getStrategy());
        doEvaluateWithOutStack(OF, EvaluateType.MINUS,
                Value.MIN_VALUE + 100, +101, getStrategy());
    }

    public void testEvaluatorMultipleWithOutStack() throws Exception {
        doEvaluateWithOutStack(10, EvaluateType.MULTIPLE, 2, 5, getStrategy());
        doEvaluateWithOutStack(-18, EvaluateType.MULTIPLE, -3, 6, getStrategy());
        doEvaluateWithOutStack(-28, EvaluateType.MULTIPLE, 4, -7, getStrategy());
        doEvaluateWithOutStack(72, EvaluateType.MULTIPLE, -8, -9, getStrategy());
        doEvaluateWithOutStack(OF, EvaluateType.MULTIPLE,
                Value.MAX_VALUE / 2, 3, getStrategy());
        //doEvaluateWithOutStack(OF, EvaluateType.MULTIPLE, Value.MAX_VALUE / 3, 3, getStrategy());
        doEvaluateWithOutStack(OF, EvaluateType.MULTIPLE,
                Value.MIN_VALUE / 2, -3, getStrategy());
    }

    public void testEvaluatorDivideWithOutStack() throws Exception {
        doEvaluateWithOutStack(0, EvaluateType.DIVIDE, 2, 5, getStrategy());
        doEvaluateWithOutStack(2, EvaluateType.DIVIDE, 5, 2, getStrategy());
        doEvaluateWithOutStack(0, EvaluateType.DIVIDE, -3, 6, getStrategy());
        doEvaluateWithOutStack(-2, EvaluateType.DIVIDE, 6, -3, getStrategy());
        doEvaluateWithOutStack(20, EvaluateType.DIVIDE, 100, 5, getStrategy());
        doEvaluateWithOutStack(0, EvaluateType.DIVIDE, 0, 5, getStrategy());
        doEvaluateWithOutStack(ZERO, EvaluateType.DIVIDE, -8, 0, getStrategy());
        doEvaluateWithOutStack(ZERO, EvaluateType.DIVIDE, 8, 0, getStrategy());
        //doEvaluateWithOutStack(OF, EvaluateType.DIVIDE, Value.MAX_VALUE + 1, 2, getStrategy());
    }

    public void testEvaluatorPositiveWithOutStack() throws Exception {
        doEvaluate(2, EvaluateType.POSITIVE, 2);
        doEvaluate(-3, EvaluateType.POSITIVE, -3);
        doEvaluate(0, EvaluateType.POSITIVE, -0);
        doEvaluate(0, EvaluateType.POSITIVE, 0);
    }

    public void testEvaluatorNegativeWithOutStack() throws Exception {
        doEvaluateWithOutStack(-2, EvaluateType.NEGATIVE, 2, getStrategy());
        doEvaluateWithOutStack(3, EvaluateType.NEGATIVE, -3, getStrategy());
        doEvaluateWithOutStack(0, EvaluateType.NEGATIVE, -0, getStrategy());
        doEvaluateWithOutStack(0, EvaluateType.NEGATIVE, 0, getStrategy());
    }

    public void testEvaluatorPlus() throws Exception {
        doEvaluate(3, EvaluateType.PLUS, 2, 1);
        doEvaluate(0, EvaluateType.PLUS, -1, 1);
        doEvaluate(0, EvaluateType.PLUS, 1, -1);
        doEvaluate(OF, EvaluateType.PLUS, Value.MAX_VALUE - 100, +101);
        doEvaluate(OF, EvaluateType.PLUS, Value.MIN_VALUE + 100, -101);
    }

    public void testEvaluatorMinus() throws Exception {
        doEvaluate(1, EvaluateType.MINUS, 2, 1);
        doEvaluate(-2, EvaluateType.MINUS, -1, 1);
        doEvaluate(2, EvaluateType.MINUS, 1, -1);
        doEvaluate(1, EvaluateType.MINUS, -1, -2);
        doEvaluate(OF, EvaluateType.MINUS, Value.MAX_VALUE - 100, -101);
        doEvaluate(OF, EvaluateType.MINUS, Value.MIN_VALUE + 100, +101);
    }

    public void testEvaluatorMultiple() throws Exception {
        doEvaluate(10, EvaluateType.MULTIPLE, 2, 5);
        doEvaluate(-18, EvaluateType.MULTIPLE, -3, 6);
        doEvaluate(-28, EvaluateType.MULTIPLE, 4, -7);
        doEvaluate(72, EvaluateType.MULTIPLE, -8, -9);
        doEvaluate(OF, EvaluateType.MULTIPLE, Value.MAX_VALUE / 2, 3);
        //doEvaluate(OF, EvaluateType.MULTIPLE, Value.MAX_VALUE / 3, 3);
        doEvaluate(OF, EvaluateType.MULTIPLE, Value.MIN_VALUE / 2, -3);
    }

    public void testEvaluatorDivide() throws Exception {
        doEvaluate(0, EvaluateType.DIVIDE, 2, 5);
        doEvaluate(2, EvaluateType.DIVIDE, 5, 2);
        doEvaluate(0, EvaluateType.DIVIDE, -3, 6);
        doEvaluate(-2, EvaluateType.DIVIDE, 6, -3);
        doEvaluate(20, EvaluateType.DIVIDE, 100, 5);
        doEvaluate(0, EvaluateType.DIVIDE, 0, 5);
        doEvaluate(ZERO, EvaluateType.DIVIDE, -8, 0);
        doEvaluate(ZERO, EvaluateType.DIVIDE, 8, 0);
        //doEvaluate(OF, EvaluateType.DIVIDE, Value.MAX_VALUE + 1, 2);
    }

    public void testEvaluatorPositive() throws Exception {
        doEvaluate(2, EvaluateType.POSITIVE, 2);
        doEvaluate(-3, EvaluateType.POSITIVE, -3);
        doEvaluate(0, EvaluateType.POSITIVE, -0);
        doEvaluate(0, EvaluateType.POSITIVE, 0);
    }

    public void testEvaluatorNegative() throws Exception {
        doEvaluate(-2, EvaluateType.NEGATIVE, 2);
        doEvaluate(3, EvaluateType.NEGATIVE, -3);
        doEvaluate(0, EvaluateType.NEGATIVE, -0);
        doEvaluate(0, EvaluateType.NEGATIVE, 0);
    }

    /**
     * スタックの観察のみ
     *
     * @throws EvaluateException
     */
    public void testMonitorStack() throws EvaluateException {

        class TestEvaluator extends Evaluator {
            public TestEvaluator(Stack<Token> stack) {
                super(stack);
            }

            @Override
            protected void evaluateStep(Stack<Token> stack2)
                    throws EvaluateException {
                System.out.println("InputStack :" + getInputStack());
                System.out.println("WokingStack :" + stack2);
                super.evaluateStep(stack2);
                System.out.println("Stack Move");
                System.out.println("InputStack :" + getInputStack());
                System.out.println("WokingStack :" + stack2);

                System.out.println();
            }
        }
        ;
        /// 1 + 3 + + 4 * 5 / -2 - 6
        // 4 +(正)  5 *  2 -(負) / 1 3 + + 6 -
        Stack<Token> stack = new Stack<Token>();

        stack.push(new MinusOperator(0, 0, null));
        stack.push(new Value(6, 0, 0, null));
        stack.push(new PlusOperator(0, 0, null));
        stack.push(new PlusOperator(0, 0, null));
        stack.push(new Value(3, 0, 0, null));
        stack.push(new Value(1, 0, 0, null));
        stack.push(new DivideOperator(0, 0, null));
        stack.push(new NegativeOperator(0, 0, null));
        stack.push(new Value(2, 0, 0, null));
        stack.push(new MultipleOperator(0, 0, null));
        stack.push(new Value(5, 0, 0, null));
        stack.push(new PositiveOperator(0, 0, null));
        stack.push(new Value(4, 0, 0, null));
        Evaluator testEvaluator = new TestEvaluator(stack);
        testEvaluator.evaluate();

    }

    private void doEvaluate(Object expected, EvaluateType op, int value) {
        try {
            Integer res = null;
            if (op == EvaluateType.NEGATIVE) {
                res = evluateNegative(value);
            } else if (op == EvaluateType.POSITIVE) {
                res = evluatePositive(value);
            } else {
                throw new IllegalArgumentException();
            }
            if (!res.equals(expected)) {
                fail();
            }
        } catch (Exception e) {
            if (!e.getClass().equals(expected.getClass())) {
                fail();
            }
        }
    }

    private void doEvaluateWithOutStack(Object expected, EvaluateType op, int value, EvaluateStrategy strategy) {
        Integer res = null;

        try {
            Value value1 = new Value(value, 0, 0, null);
            if (op == EvaluateType.NEGATIVE) {
                Value n = strategy.toNegative(value1);
                res = n.getValue();
            } else if (op == EvaluateType.POSITIVE) {
                Value n = strategy.toPositive(value1);
                res = n.getValue();
            } else {
                throw new IllegalArgumentException();
            }
            if (!res.equals(expected)) {
                fail();
            }
        } catch (Exception e) {
            if (!e.getClass().equals(expected.getClass())) {
                fail();
            }
        }
    }

    private void doEvaluate(Object expected, EvaluateType op, int left, int right) {
        Integer res = null;
        try {
            if (op == EvaluateType.PLUS) {
                res = evaluatePlus(left, right);
            } else if (op == EvaluateType.MINUS) {
                res = evluateMinus(left, right);
            } else if (op == EvaluateType.MULTIPLE) {
                res = evluateMultiple(left, right);
            } else if (op == EvaluateType.DIVIDE) {
                res = evluateDivide(left, right);
            } else {
                throw new IllegalArgumentException();
            }
            if (!res.equals(expected)) {
                fail();
            }
        } catch (Exception e) {
            if (!e.getClass().equals(expected.getClass())) {
                fail();
            }
        }
    }

    private void doEvaluateWithOutStack(Object expected, EvaluateType op, int left, int right, EvaluateStrategy strategy) {
        Integer res = null;

        try {
            Value value1 = new Value(left, 0, 0, null);
            Value value2 = new Value(right, 0, 0, null);
            if (op == EvaluateType.PLUS) {
                Value n = strategy.plus(value1, value2);
                res = n.getValue();
            } else if (op == EvaluateType.MINUS) {
                Value n = strategy.minus(value1, value2);
                res = n.getValue();
            } else if (op == EvaluateType.MULTIPLE) {
                Value n = strategy.multiple(value1, value2);
                res = n.getValue();
            } else if (op == EvaluateType.DIVIDE) {
                Value n = strategy.divide(value1, value2);
                res = n.getValue();

            } else {
                throw new IllegalArgumentException();
            }
            if (!res.equals(expected)) {
                fail();
            }
        } catch (Exception e) {
            if (!e.getClass().equals(expected.getClass())) {
                fail();
            }
        }
    }

    private static int evaluatePlus(int left, int right) throws EvaluateException {
        Stack<Token> stack = new Stack<Token>();

        Token value1 = new Value(left, 0, 0, null);
        Token plus = new PlusOperator(0, 0, value1);
        Token value2 = new Value(right, 0, 0, plus);

        stack.push(plus);
        stack.push(value2);
        stack.push(value1);
        int i = Evaluator.evaluate(stack);
        return i;
    }

    private static int evluateMinus(int left, int right) throws EvaluateException {
        Stack<Token> stack = new Stack<>();

        Token value1 = new Value(left, 0, 0, null);
        Token op = new MinusOperator(0, 0, value1);
        Token value2 = new Value(right, 0, 0, op);

        stack.push(op);
        stack.push(value2);
        stack.push(value1);
        int i = Evaluator.evaluate(stack);

        return i;
    }

    private static int evluateMultiple(int left, int right) throws EvaluateException {
        Stack<Token> stack = new Stack<>();

        Token value1 = new Value(left, 0, 0, null);
        Token op = new MultipleOperator(0, 0, value1);
        Token value2 = new Value(right, 0, 0, op);

        stack.push(op);
        stack.push(value2);
        stack.push(value1);
        int i = Evaluator.evaluate(stack);
        return i;
    }

    private static int evluateDivide(int left, int right) throws EvaluateException {
        Stack<Token> stack = new Stack<>();

        Token value1 = new Value(left, 0, 0, null);
        Token op = new DivideOperator(0, 0, value1);
        Token value2 = new Value(right, 0, 0, op);

        stack.push(op);
        stack.push(value2);
        stack.push(value1);
        int i = Evaluator.evaluate(stack);
        return i;
    }

    private static int evluatePositive(int value) throws EvaluateException {
        Stack<Token> stack = new Stack<>();

        Token op = new PositiveOperator(0, 0, null);
        Token value1 = new Value(value, 0, 0, op);

        stack.push(op);
        stack.push(value1);
        int i = Evaluator.evaluate(stack);
        return i;
    }

    private static int evluateNegative(int value) throws EvaluateException {

        Stack<Token> stack = new Stack<>();

        Token op = new NegativeOperator(0, 0, null);
        Token value1 = new Value(value, 0, 0, op);

        stack.push(op);
        stack.push(value1);
        int i = Evaluator.evaluate(stack);
        return i;
    }

    private EvaluateStrategy getStrategy() {
        return EvaluateStrategy.DefaultStrategy;
    }

}
