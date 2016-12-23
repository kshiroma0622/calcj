package kshiroma0622.calcj.test;

import junit.framework.TestCase;
import kshiroma0622.calcj.ErrorOutputter;
import kshiroma0622.calcj.evaluate.Evaluator;
import kshiroma0622.calcj.evaluate.EvaluateException;
import kshiroma0622.calcj.parse.ParseException;
import kshiroma0622.calcj.tokenize.TokenizeException;
import kshiroma0622.calcj.parse.Parser;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.tokenize.Tokenizer;

import java.util.Stack;

public class IntegrationTest extends TestCase {

    public void testNormalCalculation() {
        doCalculation("1+34+444", 479, true);
        doCalculation("322+34+44*(4/3)-44", 356, true);
        doCalculation("1+334 * (334-332) + (( 3434+ 2456 ) / 32 )", 853, true);
        // and so on
    }

    private void doCalculation(String expr, int expected, boolean success) {
        try {
            Token[] tokens = Tokenizer.tokenize(expr);
            Stack<Token> stack = Parser.parse(tokens);
            int result = Evaluator.evaluate(stack);
            if (!success) {
                fail();
            }
            if (success && result != expected) {
                fail();
            }
            System.out.println(expr + " = " + result);
        } catch (TokenizeException e) {
            ErrorOutputter.outputError(e, expr);
        } catch (ParseException e) {
            ErrorOutputter.outputError(e, expr);
        } catch (EvaluateException e) {
            ErrorOutputter.outputError(e, expr);
        }
    }

}
