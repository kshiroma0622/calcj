package kshiroma0622.calcj.evaluate;

import kshiroma0622.calcj.Util;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.TokenUtil;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.operator.binary.BinaryOperator;
import kshiroma0622.calcj.syntax.operator.unary.UnaryOperator;

import java.util.Stack;

public class Evaluator {

    private final EvaluateStrategy strategy;
    private final Stack<Token> input;

    public Evaluator(final Stack<Token> stack) {
        this(stack, EvaluateStrategy.DefaultStrategy);
    }

    public Evaluator(final Stack<Token> stack, EvaluateStrategy strategy) {
        if (strategy == null) {
            throw new NullPointerException(EvaluateStrategy.class.getName());
        }
        this.strategy = strategy;
        this.input = (Stack<Token>) stack.clone();
    }

    public int evaluate() throws EvaluateException {
        Stack<Token> workStack = new Stack<>();
        while (!input.isEmpty()) {
            evaluateStep(workStack);
        }
        Token value = workStack.pop();
        if (TokenUtil.isValue(value)) {
            Value z = (Value) value;
            return z.getValue();
        } else {
            //エラー
            System.out.println(value.dump());
            return Integer.MAX_VALUE;
        }
    }

    protected Stack<Token> getInputStack() {
        return input;
    }

    protected void evaluateStep(Stack<Token> workStack)
            throws EvaluateException {
        Token t = input.pop();
        if (TokenUtil.isValue(t)) {
            workStack.push(t);
        } else if (TokenUtil.isBracket(t)) {
            //DO NOTHING
        } else if (TokenUtil.isBinaryOperator(t)) {
            BinaryOperator op = (BinaryOperator) t;
            Token right = workStack.pop();
            Token left = workStack.pop();
            boolean error = false;
            if (!TokenUtil.isValue(right)) {
                error = true;
            }
            if (!TokenUtil.isValue(left)) {
                error = true;
            }
            if (error) {
                Util.throwPGError();
            } else {
                Token derived = op.evaluate(strategy, (Value) left,
                        (Value) right);
                workStack.push(derived);
            }
        } else if (TokenUtil.isUnaryOperator(t)) {
            UnaryOperator op = (UnaryOperator) t;
            Token value = workStack.pop();
            boolean error = false;
            if (!TokenUtil.isValue(value)) {
                error = true;
            }
            if (error) {
                Util.throwPGError();
            } else {
                Token derived = op.evaluate(strategy, (Value) value);
                workStack.push(derived);
            }
        }
    }

    public static int evaluate(Stack<Token> token) throws EvaluateException {
        Evaluator evaluator = new Evaluator(token);
        return evaluator.evaluate();
    }

}
