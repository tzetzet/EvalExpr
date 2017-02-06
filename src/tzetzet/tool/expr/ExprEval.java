package tzetzet.tool.expr;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 数式を評価するスタティックメソッドを提供する.
 */
public final class ExprEval {
    /**
     * 逆ポーランド記法のトークン列に変換した数式を評価して値を返す.
     *
     * @param rpnTokens 逆ポーランド記法のトークン列
     * @return 評価した整数値
     * @throws ExprEvalException
     */
    public static int evalRPN(List<Token> rpnTokens) throws ExprEvalException {
        if (rpnTokens == null || rpnTokens.isEmpty()) {
            throw new IllegalArgumentException("rpnTokens: " + rpnTokens);
        }

        ResourceBundle rb = ResourceBundle.getBundle(ExprEval.class.getPackage().getName() + ".messages");

        ArrayDeque<Integer> numStack = new ArrayDeque<>();
        for (Token token : rpnTokens) {
            if (token instanceof Token.BinOpeToken) {
                if (numStack.size() < 1) {
                    throw new ExprEvalException(String.format(rb.getString("syntaxerr_lack_2_operands"), token.toString()));
                } else if (numStack.size() < 2) {
                    throw new ExprEvalException(String.format(rb.getString("syntaxerr_lack_1_operand"), token.toString()));
                }
                int r = numStack.pop();
                int l = numStack.pop();
                long n;
                if (token == Token.PLUS) {
                    n = l + r;
                } else if (token == Token.MINUS) {
                    n = l - r;
                } else if (token == Token.TIMES) {
                    n = l * r;
                } else if (token == Token.DIVIDE) {
                    n = l / r;
                } else {
                    throw new RuntimeException("Unknown Error");
                }
                if (Token.isOutOfNumRange(n)) {
                    throw new ExprEvalException(rb.getString("runtimeerr_calc_interim"));
                }
                numStack.push((int) n);
            } else {
                if (token instanceof Token.NumToken) {
                    numStack.push(token.parseAsNum());
                } else {
                    throw new ExprEvalException(String.format(rb.getString("syntaxerr_at"), token.toString()));
                }
            }
        }

        if (numStack.size() != 1) {
            throw new ExprEvalException(String.format(rb.getString("syntaxerr_extra"), numStack.peek().toString()));
        }

        return numStack.pop();
    }

    private ExprEval() {
    }
}
