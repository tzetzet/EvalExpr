package tzetzet.tool.expr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 標準入力から数式を読み取り、評価結果を標準出力に書き出すプログラム.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // 標準入力から１行ずつ読み込み、行単位で数式として評価する
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            // 数式でなく "quit" が入力されたらプログラム終了
            if (line.trim().equalsIgnoreCase("quit")) {
                break;
            }

            // 数式を構文解析
            List<Token> tokens;
            try {
                tokens = ExprParser.parse(line);
            } catch (ExprParserException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

            // 構文解析した結果を評価
            int n;
            try {
                n = ExprEval.evalRPN(tokens);
                System.out.println(n);
            } catch (ExprEvalException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
