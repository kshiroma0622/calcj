package kshiroma0622.calcj.syntax.operator.binary;


import kshiroma0622.calcj.evaluate.EvaluateStrategy;
import kshiroma0622.calcj.evaluate.EvaluateException;
import kshiroma0622.calcj.syntax.AbstractToken;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.operator.OperatorToken;

public abstract class BinaryOperator extends AbstractToken implements
        OperatorToken {

    public BinaryOperator(int pos, int length, Token prev) {
        super(pos, length, prev);
    }

    public abstract Value evaluate(EvaluateStrategy strategy, Value left,
                                   Value right) throws EvaluateException;

}
