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
                int r = numStack.pop();
                int l = numStack.pop();
                if (token == Token.PLUS) {
                    numStack.push(l + r);
                } else if (token == Token.MINUS) {
                    numStack.push(l - r);
                } else if (token == Token.TIMES) {
                    numStack.push(l * r);
                } else if (token == Token.DIVIDE) {
                    numStack.push(l / r);
                } else {
                    assert false;
                }
            } else {
                numStack.push(token.parseAsNum());
            }
        }

        if (numStack.size() != 1) {
            throw new ExprEvalException("extra inputs");
        }

        return numStack.pop();
    }

    private ExprEval() {
    }
}
