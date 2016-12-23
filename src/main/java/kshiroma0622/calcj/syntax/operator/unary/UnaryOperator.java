package kshiroma0622.calcj.syntax.operator.unary;

import kshiroma0622.calcj.evaluate.EvaluateStrategy;
import kshiroma0622.calcj.syntax.AbstractToken;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.operator.OperatorToken;

public abstract class UnaryOperator extends AbstractToken implements OperatorToken {

    public UnaryOperator(int pos, int length, Token prev) {
        super(pos, length, prev);
    }

    public int getOperandNumber() {
        return 1;
    }

    public abstract Value evaluate(EvaluateStrategy strategy, Value value);
}
