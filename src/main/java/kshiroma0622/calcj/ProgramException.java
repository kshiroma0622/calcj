package kshiroma0622.calcj;


/**
 * プログラムのバグが原因であるエラー<br>
 * プログラムの仕様上通るはずのない実行パスや成立するはずのない条件が成立するときに発生させる
 * 
 * try catchする対象に含めないため、RuntimeExceptionの派生クラスにしている。
 */
public class ProgramException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // private static Log logger = LogFactory.getLog(ProgramException.class);

    public ProgramException() {
        //logger.error("program bug", this);// or fatal?
    }

}
