package kshiroma0622.calcj;

import kshiroma0622.calcj.evaluate.EvaluateException;
import kshiroma0622.calcj.evaluate.Evaluator;
import kshiroma0622.calcj.evaluate.OverflowException;
import kshiroma0622.calcj.parse.ParseException;
import kshiroma0622.calcj.parse.Parser;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.tokenize.TokenizeException;
import kshiroma0622.calcj.tokenize.Tokenizer;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class ConsoleApp {

    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        try {
            app.start();
        } catch (IOException e) {
        }
    }

    private ConsoleApp() {
    }

    private void start() throws IOException {
        BufferedReader reader = new BufferedReader(//
                new InputStreamReader(System.in, "UTF-8"));

        while (true) {
            outputPrompt();

            String inputLine = reader.readLine();
            if (StringUtils.isBlank(inputLine)) {
                continue;
            }
            if (isExitCommand(inputLine)) {
                break;
            }
            calucurate(inputLine);
        }

    }

    private void outputPrompt() {
        System.out.print("数式を入力してください$ ");
        System.out.flush();
    }

    private boolean isExitCommand(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (StringUtils.equals(str.trim(), "exit")) {
            return true;
        }
        return false;
    }

    private void calucurate(String expression) {
        try {
            Token[] tokens = Tokenizer.tokenize(expression);
            Stack<Token> stack = Parser.parse(tokens);
            int result = Evaluator.evaluate(stack);
            System.out.println("結果:" + result);
        } catch (TokenizeException e) {
            ErrorOutputter.outputError(e, expression);
            return;
        } catch (ParseException e) {
            ErrorOutputter.outputError(e, expression);
        } catch (OverflowException e) {
            ErrorOutputter.outputError(e, expression);
        } catch (EvaluateException e) {
            ErrorOutputter.outputError(e, expression);
        }

    }

}
