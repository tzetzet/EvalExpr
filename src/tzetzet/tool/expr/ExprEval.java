package tzetzet.tool.expr;

import java.util.ArrayDeque;
import java.util.List;

/**
 */
public class ExprEval {
    public static int evalRPN(List<Token> rpnTokens) {
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
        assert numStack.size() == 1;

        return numStack.pop();
    }
}
