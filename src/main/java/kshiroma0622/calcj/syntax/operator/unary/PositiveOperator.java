package kshiroma0622.calcj.syntax.operator.unary;

import kshiroma0622.calcj.evaluate.EvaluateStrategy;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.Token;

public class PositiveOperator extends UnaryOperator implements SignOperator {
    public PositiveOperator(int pos, int length, Token prev) {
        super(pos, length, prev);
    }

    public String dump() {
        return "+";
    }

    public Value evaluate(EvaluateStrategy strategy, Value value) {
        return strategy.toPositive(value);
    }
}
