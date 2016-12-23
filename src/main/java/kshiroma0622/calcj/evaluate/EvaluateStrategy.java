package kshiroma0622.calcj.evaluate;

import kshiroma0622.calcj.ProgramException;
import kshiroma0622.calcj.syntax.Value;
import kshiroma0622.calcj.syntax.Token;

import java.math.BigDecimal;

public interface EvaluateStrategy {

    public Value plus(Value value1, Value value2)
            throws OverflowException;

    public Value minus(Value value1, Value value2)
            throws OverflowException;

    public Value multiple(Value value1, Value value2)
            throws OverflowException;

    public Value divide(Value value1, Value value2)
            throws OverflowException, ZeroDivideException;

    public Value toPositive(Value value1);

    public Value toNegative(Value value1);

    public static EvaluateStrategy DefaultStrategy = new EvaluateStrategy() {

        private Value newValue(int value, Value value1, Value value2)
                throws OverflowException {
            int pos = value1.getPos();
            int len = value2.getPos() + value2.getLength() - pos;
            Token prev = value1.getPrevToken();
            return new Value(value, pos, len, prev);
        }

        private Value newValue(int value, Value value1)
                throws OverflowException {
            Token prev = value1.getPrevToken();
            int pos;
            if (prev == null) {
                pos = -1;
            } else {
                pos = prev.getPos();
            }
            int len = value1.getPos() + value1.getLength() - pos;

            return new Value(value, pos, len, prev);
        }

        public Value plus(Value value1, Value value2)
                throws OverflowException {
            int left = value1.getValue();
            int right = value2.getValue();
            return newValue(left + right, value1, value2);

        }

        public Value minus(Value value1, Value value2)
                throws OverflowException {
            int left = value1.getValue();
            int right = value2.getValue();
            return newValue(left - right, value1, value2);
        }

        public Value multiple(Value value1, Value value2)
                throws OverflowException {
            int left = value1.getValue();
            int right = value2.getValue();
            ///かける前に何らかの方法で検査をする必要がある。
            // TODO 最適化
            BigDecimal b1 = new BigDecimal(Math.abs(left));
            BigDecimal b2 = new BigDecimal(Math.abs(right));
            BigDecimal b3 = b1.multiply(b2);
            if (b3.compareTo(new BigDecimal(Value.MAX_VALUE)) > 1) {
                throw new OverflowException();
            }
            return newValue(left * right, value1, value2);
        }

        public Value divide(Value value1, Value value2)
                throws OverflowException, ZeroDivideException {
            int left = value1.getValue();
            int right = value2.getValue();
            if (right == 0) {
                throw new ZeroDivideException(value2);
            }
            return newValue(left / right, value1, value2);
        }

        public Value toPositive(Value value1) {
            int value = value1.getValue();

            try {
                return newValue(value, value1);
            } catch (OverflowException e) {
                throw new ProgramException();
            }

        }

        public Value toNegative(Value value1) {
            int value = value1.getValue();
            try {
                return newValue(-value, value1);
            } catch (OverflowException e) {
                throw new ProgramException();
            }
        }

    };
}
