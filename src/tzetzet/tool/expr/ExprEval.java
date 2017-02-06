package tzetzet.tool.expr;

import java.util.ArrayDeque;
import java.util.List;

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

        ArrayDeque<Integer> numStack = new ArrayDeque<>();
        for (Token token : rpnTokens) {
            if (token instanceof Token.BinOpeToken) {
                if (numStack.size() < 1) {
                    throw new ExprEvalException("syntax error: two operands lack for operator " + token.toString());
                } else if (numStack.size() < 2) {
                    throw new ExprEvalException("syntax error: one operand lacks for operator " + token.toString());
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
                if (isOutOfNumRange(n)) {
                    throw new ExprEvalException("number overflow");
                }
                numStack.push((int) n);
            } else {
                numStack.push(token.parseAsNum());
            }
        }

        if (numStack.size() != 1) {
            throw new ExprEvalException("extra inputs");
        }

        return numStack.pop();
    }

    private static boolean isOutOfNumRange(long n) {
        return (n < -99999999 || 99999999 < n);
    }

    private ExprEval() {
    }
}
