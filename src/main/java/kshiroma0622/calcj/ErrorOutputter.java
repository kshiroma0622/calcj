package kshiroma0622.calcj;

import kshiroma0622.calcj.syntax.SyntaxError;
import kshiroma0622.calcj.tokenize.TokenizeError;
import kshiroma0622.calcj.evaluate.EvaluateException;
import kshiroma0622.calcj.evaluate.OverflowException;
import kshiroma0622.calcj.evaluate.ZeroDivideException;
import kshiroma0622.calcj.parse.ParseException;
import kshiroma0622.calcj.syntax.Token;
import kshiroma0622.calcj.tokenize.TokenizeException;
import org.apache.commons.lang.StringUtils;

public class ErrorOutputter {

    public static void outputError(TokenizeException exception,
                                   String inputString) {
        if (exception == null) {
            return;
        }
        for (TokenizeError error : exception.getErrorList()) {
            int index = error.getIndex();
            String c = error.getInputString();
            String format = null;
            TokenizeError.Type type = error.getType();
            if (type == TokenizeError.Type.IllegalCharacter) {
                format = " エラー  %d文字目に不正な文字があります。\"%s\"";
            } else if (type == TokenizeError.Type.Overflow) {
                format = " エラー  %d文字目 9桁以上の数値が入力されています。\"%s\"";
            } else if (type == TokenizeError.Type.NumberFormatError) {
                format = " エラー  %d文字目 数値の書式が不正です。\"%s\"";
            }
            if (StringUtils.isNotEmpty(format)) {
                String message = String.format(format, index + 1, c);
                System.out.println(message);
            }
        }
    }

    public static void outputError(ParseException exception, String inputString) {
        if (exception == null) {
            return;
        }
        SyntaxError error = exception.getSyntaxError();
        if (error == null) {
            //Util.throwPGError();
        }
        Token token = error.getErrorToken();

        SyntaxError.SyntaxErrorType type = error.getType();
        if (type == SyntaxError.SyntaxErrorType.Other) {
            String format = " エラー  %d文字目付近に文法エラーがあります。(%s)";
            // int n =10 ;
            int pos = token.getPos();
            // int length = inputString.length(); 
            // int begin = pos-n > 0 ? pos-n : 0;
            /// int end   = pos+n < length ? pos+n:length-1;
            //String neighborString = inputString.substring(begin,end);
            String message = String.format(format, pos + 1, token.dump());
            System.out.println(message);
        } else if (type == SyntaxError.SyntaxErrorType.AsyncBracket) {
            String format = " エラー  開始括弧と終了括弧の数が違います。";
            String message = String.format(format);
            System.out.println(message);
        } else if (type == SyntaxError.SyntaxErrorType.EmptyBlock) {
            String format = " エラー  %d文字目付近に空の括弧があります。";
            int pos = token.getPos();
            String message = String.format(format, pos + 1);
            System.out.println(message);
        }
    }

    public static void outputError(EvaluateException exception, String inputString) {
        if (exception == null) {
            return;
        }
        if (exception instanceof OverflowException) {
            System.out.println(" エラー  計算中に桁あふれが起こりました。");
            return;
        }
        if (exception instanceof ZeroDivideException) {
            System.out.println(" エラー  計算中にゼロ除算が起こりました。");
            return;
        }
    }

}
