package kshiroma0622.calcj.tokenize;

public class TokenizeError {
    public enum Type {
        IllegalCharacter, NumberFormatError, Overflow;
    }

    private final int index;
    private final String value;
    private final Type type;

    public TokenizeError(Type type, int index, String value) {
        this.type = type;
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getInputString() {
        return value;
    }

    public Type getType() {
        return type;
    }

}
