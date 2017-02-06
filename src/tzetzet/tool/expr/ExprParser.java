package tzetzet.tool.expr;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数式を構文解析する機能を提供する.
 *
 * 構文解析の結果は逆ポーランド記法のトークン列とする.
 */
public final class ExprParser {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\s+|\\(|\\)|[0-9]+|-|\\+|\\*|/");

    /**
     * 与えられた数式を字句解析し、トークン列として返却する.
     *
     * @param exprStr 数式を記述した文字列
     * @return 数式を字句解析したトークン列
     */
    public static List<Token> scan(String exprStr) {
        ArrayList<Token> tokens = new ArrayList<>();

        if (exprStr == null) {
            return tokens;
        }

        Matcher matcher = TOKEN_PATTERN.matcher(exprStr);

        boolean isMinusUnaryFound = false;
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

            /*
             * パターンマッチにより取り出したトークン文字列からトークンオブジェクト
             * を得る. その際、マイナス記号が二項演算子ではなく単項演算子の場合は、
             * 後続の符号付き整数トークンの一部として扱う.
             */
            Token token = Token.getToken(tokenStr);
            if (token == Token.MINUS && (tokens.isEmpty() || tokens.get(tokens.size() - 1) instanceof Token.CtrlToken)) {
                if (isMinusUnaryFound) {
                    throw new RuntimeException();
                }
                isMinusUnaryFound = true;
            } else {
                if (isMinusUnaryFound) {
                    if (!(token instanceof Token.NumToken)) {
                        throw new RuntimeException();
                    }
                    token = Token.getToken("-" + tokenStr);
                }
                tokens.add(token);
                isMinusUnaryFound = false;
            }
        }

        return tokens;
    }

    /**
     * 数式を分割したトークン列を、逆ポーランド記法の列に変換する.
     *
     * @param exprTokens 分割済みトークン列
     * @return 逆ポーランド記法での列
     */
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
