package kshiroma0622.calcj.syntax.operator.binary;

import kshiroma0622.calcj.evaluate.EvaluateStrategy;
import kshiroma0622.calcj.evaluate.EvaluateException;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.Token;

public class DivideOperator extends BinaryOperator {

    public DivideOperator(int pos, int length, Token prev) {
        super(pos, length, prev);
    }

    public String dump() {
        return "/";
    }

    public Value evaluate(EvaluateStrategy strategy, Value left,
                          Value right) throws EvaluateException {
        return strategy.divide(left, right);
    }
}
