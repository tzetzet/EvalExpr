package tzetzet.tool.expr;

/**
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

    static Token getToken(String s) {
        for (Token opeToken : CTRL_TOKENS) {
            if (opeToken.mStr.equals(s)) {
                return opeToken;
            }
        }
        return new NumToken(s);
    }

    @Override
    public String toString() {
        return mStr;
    }

    public int getPrecedence() {
        if (this == PLUS || this == MINUS) {
            return 1;
        }
        if (this == TIMES || this == DIVIDE) {
            return 2;
        }
        return 0;
    }

    public int parseAsNum() {
        return Integer.parseInt(mStr);
    }

    protected final String mStr;

    private Token(String s) {
        if (s == null) {
            s = "";
        } else {
            s = s.trim();
        }
        mStr = s;
    }

    public abstract static class CtrlToken extends Token {
        private CtrlToken(String s) {
            super(s);
        }
    }

    public static class BinOpeToken extends CtrlToken {
        private BinOpeToken(String s) {
            super(s);
        }
    }

    public static class ParenToken extends CtrlToken {
        private ParenToken(String s) {
            super(s);
        }
    }

    public static class NumToken extends Token {
        private NumToken(String s) {
            super(s);
        }
    }
}
