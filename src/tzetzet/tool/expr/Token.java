package tzetzet.tool.expr;

/**
 * 数式を構成するトークンを表現するクラス.
 *
 * トークンの種別をサブクラスによるクラス階層で表現する.
 */
public abstract class Token {
    public static final CtrlToken PLUS = new BinOpeToken("+");
    public static final CtrlToken MINUS = new BinOpeToken("-");
    public static final CtrlToken TIMES = new BinOpeToken("*");
    public static final CtrlToken DIVIDE = new BinOpeToken("/");

    public static final CtrlToken OPEN_PAREN = new ParenToken("(");
    public static final CtrlToken CLOSE_PAREN = new ParenToken(")");

    static final CtrlToken[] CTRL_TOKENS = {
        PLUS, MINUS, TIMES, DIVIDE, OPEN_PAREN, CLOSE_PAREN,
    };

    static boolean isOutOfNumRange(long n) {
        return (n < -99999999 || 99999999 < n);
    }

    /**
     * トークン文字列からトークンオブジェクトを得る.
     *
     * @param tokenStr トークン文字列
     * @return トークンオブジェクト
     */
    public static Token getToken(String tokenStr) {
        for (Token opeToken : CTRL_TOKENS) {
            if (opeToken.mTokenStr.equals(tokenStr)) {
                return opeToken;
            }
        }
        return new NumToken(tokenStr);
    }

    @Override
    public String toString() {
        return mTokenStr;
    }

    /**
     * トークンが二項演算子の場合、演算の優先順位を返す.
     *
     * 優先順位は 1 以上の整数とし、値が大きいほうが優先順位が高いとする.
     * トークンが二項演算子以外の場合は 0 を返す.
     *
     * @return 二項演算子の優先順位
     */
    public int getPrecedence() {
        if (this == PLUS || this == MINUS) {
            return 1;
        }
        if (this == TIMES || this == DIVIDE) {
            return 2;
        }
        return 0;
    }

    /**
     * トークンが符号付き整数値の場合、 int 値を返す.
     *
     * トークンが符号付き整数値でない場合など、int 値として構文解析できない場合は
     * NumberFormatException 例外を送出する.
     *
     * @return 符号付き整数値
     */
    public int parseAsNum() {
        int n = Integer.parseInt(mTokenStr);
        if (isOutOfNumRange(n)) {
            throw new NumberFormatException("n: " + n);
        }
        return n;
    }

    protected final String mTokenStr;

    private Token(String s) {
        if (s == null) {
            s = "";
        } else {
            s = s.trim();
        }
        mTokenStr = s;
    }

    /**
     * 制御系トークン.
     *
     * 二項演算子トークンと丸括弧トークンより成る.
     */
    public abstract static class CtrlToken extends Token {
        private CtrlToken(String s) {
            super(s);
        }
    }

    /**
     * 二項演算子トークン.
     */
    public static class BinOpeToken extends CtrlToken {
        private BinOpeToken(String s) {
            super(s);
        }
    }

    /**
     * 丸括弧トークン.
     */
    public static class ParenToken extends CtrlToken {
        private ParenToken(String s) {
            super(s);
        }
    }

    /**
     * 符号付き整数値トークン.
     */
    public static class NumToken extends Token {
        private NumToken(String s) {
            super(s);
        }
    }
}
