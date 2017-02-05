package tzetzet.tool.expr;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class ExprParser {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\s+|\\(|\\)|[0-9]+|-|\\+|\\*|/");

    public static List<Token> scan(String exprStr) {
        ArrayList<Token> tokens = new ArrayList<>();

        if (exprStr == null) {
            return tokens;
        }

        Matcher matcher = TOKEN_PATTERN.matcher(exprStr);

        boolean rememberMinusUnary = false;
        int nextStart = 0;
        while (matcher.find(nextStart)) {
            if (nextStart != matcher.start()) {
                // syntax error
                throw new RuntimeException();
            }
            nextStart = matcher.end();

            String tokenStr = matcher.group().trim();
            if (tokenStr.length() == 0) {
                continue;
            }

            Token token = Token.getToken(tokenStr);
            if (token == Token.MINUS && (tokens.size() == 0 || tokens.get(tokens.size() - 1) instanceof Token.CtrlToken)) {
                if (rememberMinusUnary) {
                    throw new RuntimeException();
                }
                rememberMinusUnary = true;
            } else {
                if (rememberMinusUnary) {
                    if (!(token instanceof Token.NumToken)) {
                        throw new RuntimeException();
                    }
                    token = Token.getToken("-" + tokenStr);
                }
                tokens.add(token);
                rememberMinusUnary = false;
            }
        }

        return tokens;
    }

    public static List<Token> makeRPN(List<Token> exprTokens) {
        ArrayDeque<Token> ctrlStack = new ArrayDeque<>();
        ArrayList<Token> rpnTokens = new ArrayList<>();
        for (Token nextToken : exprTokens) {
            if (nextToken instanceof Token.BinOpeToken) {
                while (ctrlStack.size() > 0) {
                    Token poppedCtrlToken = ctrlStack.pop();
                    if (poppedCtrlToken instanceof Token.ParenToken ||
                            nextToken.getPrecedence() > poppedCtrlToken.getPrecedence()) {
                        ctrlStack.push(poppedCtrlToken);
                        break;
                    } else {
                        rpnTokens.add(poppedCtrlToken);
                    }
                }
                ctrlStack.push(nextToken);
            } else if (nextToken == Token.OPEN_PAREN) {
                ctrlStack.push(nextToken);
            } else if (nextToken == Token.CLOSE_PAREN) {
                while (ctrlStack.size() > 0) {
                    Token ctrlToken = ctrlStack.pop();
                    if (ctrlToken == Token.OPEN_PAREN) {
                        break;
                    } else {
                        rpnTokens.add(ctrlToken);
                    }
                }
            } else {
                rpnTokens.add(nextToken);
            }
        }
        while (ctrlStack.size() > 0) {
            rpnTokens.add(ctrlStack.pop());
        }

        return rpnTokens;
    }
}
