package tzetzet.tool.expr;

import java.util.Arrays;
import java.util.List;

/**
 */
public class Main {
    public static void main(String[] args) {
        String[] exprs = {
            "1 + 2 + 3 - 4",
            "12 + 34 + 56 * (78 - 9 * 2)",
            "-10 + 20 * 30 - 40 +  - 50",
            "1 + (2 * 3 - 4)",
            "1 + (2 * (3 - 4))",
            "1 + 2 * (3 - 4)",
            "90 + 80 - 70 * 60 / 50 + 40 - 30 * 20 / 10",
        };

        for (String expr : exprs) {
            List<Token> tokens = ExprParser.scan(expr);
            System.out.println("tokens: " + Arrays.toString(tokens.toArray()));
            List<Token> rpn = ExprParser.makeRPN(tokens);
            System.out.println("RPN: " + Arrays.toString(rpn.toArray()));
            int ans = ExprEval.evalRPN(rpn);
            System.out.println("ANS: " + ans);
        }
    }
}
