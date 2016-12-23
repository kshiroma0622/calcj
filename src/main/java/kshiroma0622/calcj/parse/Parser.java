package kshiroma0622.calcj.parse;

import kshiroma0622.calcj.ProgramException;
import kshiroma0622.calcj.Util;
import kshiroma0622.calcj.syntax.SyntaxError;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.syntax.TokenUtil;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 字句解析が終わったものを逆ポーランド記法に並び替える
 */
public class Parser {

    private List<Token> input;
    private List<Token> result;

    public Parser(Token[] tokens) throws ParseException {

        Stack<Token> stack = new Stack<Token>();
        input = Util.newLinkedList(tokens);

        result = Util.newLinkedList();

        // 括弧の対応を調べる
        throwIfBraketAsync();

        //オペランドが決まらない演算子があるとエラーにする
        throwIfOperatorsHaveEnoughOperands();

        // 最初と最後に終端を入れる
        stack.push(Token.Bound);
        input.add(Token.Bound);
        int count = 0;
        while (true && count < 1000) {
            count++;
            Token stackTop = stack.peek();
            Token inputTop = input.get(0);
            PriorityOrder order = PriorityOrder.getOrder(stackTop, inputTop);
            if (order == PriorityOrder.FI) {
                break;
            }

            if (order == PriorityOrder.LT || order == PriorityOrder.EQ) {
                stack.push(inputTop);
                input.remove(0);
            } else if (order == PriorityOrder.GT) {
                List temp = Util.newLinkedList();
                PriorityOrder order2 = null;
                while (order2 != PriorityOrder.LT) {
                    Token s0 = stack.pop();
                    Token s1 = stack.peek();
                    if (!TokenUtil.isBracket(s0)) {
                        temp.add(0, s0);
                    }
                    order2 = PriorityOrder.getOrder(s1, s0);
                }
                result.addAll(temp);
            } else {
                if (inputTop == null || inputTop == Token.Bound) {
                    throw new ProgramException();
                }
                SyntaxError error = new SyntaxError(SyntaxError.SyntaxErrorType.Other,
                        inputTop);
                throw new ParseException(error);
            }
        }
    }

    public Stack<Token> getReversePolish() {
        Stack<Token> stack = new Stack<Token>();
        stack.addAll(result);
        Collections.reverse(stack);
        return stack;
    }

    /**
     * 括弧の対応がなされていなければ例外を発生させる
     */
    private void throwIfBraketAsync() throws ParseException {
        int beingCount = 0;
        int endCount = 0;
        if (Util.isEmpty(input)) {
            return;
        }
        Token bracket = null;//TODO
        for (Token token : input) {

            if (TokenUtil.isBeginBracket(token)) {
                beingCount++;
            } else if (TokenUtil.isEndBracket(token)) {
                endCount++;
            }
        }
        if (beingCount != endCount) {
            SyntaxError error = new SyntaxError(SyntaxError.SyntaxErrorType.AsyncBracket,
                    bracket);
            throw new ParseException(error);
        }

        ///空のブロックがあればスローする
        for (Token token : input) {
            if (TokenUtil.isEndBracket(token)) {
                Token left = token.getPrevToken();
                if (TokenUtil.isBeginBracket(left)) {
                    SyntaxError error = new SyntaxError(
                            SyntaxError.SyntaxErrorType.EmptyBlock, left);
                    throw new ParseException(error);
                }
            }
        }

    }

    private void throwIfOperatorsHaveEnoughOperands() throws ParseException {

        for (int i = 0; i < input.size(); i++) {
            Token left = Util.get(input, i - 1);
            Token current = Util.get(input, i);
            Token right = Util.get(input, i + 1);
            boolean error = false;
            if (TokenUtil.isUnaryOperator(current)) {
                if (!TokenUtil.isRightOperandCandidate(right)) {
                    error = true;
                }
            }
            if (TokenUtil.isBinaryOperator(current)) {
                if (!TokenUtil.isLeftOperandCandidate(left)) {
                    error = true;
                }
                if (!TokenUtil.isRightOperandCandidate(right)) {
                    error = true;
                }
            }
            if (error) {
                SyntaxError syntaxError = new SyntaxError(
                        SyntaxError.SyntaxErrorType.Other, current);
                throw new ParseException(syntaxError);
            }

        }

    }

    public static Stack<Token> parse(Token[] tokens) throws ParseException {
        Parser parser = new Parser(tokens);
        return parser.getReversePolish();
    }
}
