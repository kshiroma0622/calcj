package kshiroma0622.calcj.syntax;


import kshiroma0622.calcj.evaluate.OverflowException;

public class Value extends AbstractToken {

    public static final int MAX_VALUE = 99999999;
    public static final int MIN_VALUE = -MAX_VALUE;

    private final int value;

    public int getValue() {
        return this.value;
    }

    public Value(int value, int pos, int length, Token prev)
            throws OverflowException {
        super(pos, length, prev);
        if (!withInRange(value)) {
            throw new OverflowException();
        }
        this.value = value;
    }

    public String getStringExpression() {
        return String.valueOf(value);
    }

    public String dump() {
        return String.valueOf(value);
    }

    public boolean withInRange(int value) {
        return value <= MAX_VALUE && value >= MIN_VALUE;
    }
}
