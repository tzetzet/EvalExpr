/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tzetzet.tool.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExprParserTest {

    public ExprParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    String makePrintableRPN(List<Token> rpnTokens) {
        StringJoiner sj = new StringJoiner(" ");
        for (Token token : rpnTokens) {
            sj.add(token.toString());
        }
        return sj.toString();
    }

    /*
     * Test of scan method, of class ExprParser.
     */

    @Test
    public void testScan_NULL入力() throws ExprParserException {
        String exprStr = null;
        List<Token> result = ExprParser.scan(exprStr);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testScan_空文字列入力() throws ExprParserException {
        String exprStr = "";
        List<Token> result = ExprParser.scan(exprStr);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testScan_正常系1() throws ExprParserException {
        String exprStr = "90 +((80- 70 ) * 60) /50 \r\n  + -40 -30 *  20 / 10";
        String expResult = "[90, +, (, (, 80, -, 70, ), *, 60, ), /, 50, +, -40, -, 30, *, 20, /, 10]";
        List<Token> result = ExprParser.scan(exprStr);
        assertEquals(expResult, Arrays.toString(result.toArray()));
    }

    /*
     * Test of makeRPN method, of class ExprParser.
     */

    @Test
    public void testMakeRPN_NULL入力() throws ExprParserException {
        List<Token> exprTokens = null;
        List<Token> result = ExprParser.makeRPN(exprTokens);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMakeRPN_空リスト入力() throws ExprParserException {
        List<Token> exprTokens = Collections.EMPTY_LIST;
        List<Token> result = ExprParser.makeRPN(exprTokens);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMakeRPN_正常系1() throws ExprParserException {
        String expr = "90 + ( ( 80 - 70 ) * 60 ) / 50 + -40 - 30 * 20 / 10";
        List<Token> exprTokens = new ArrayList<>();
        for (String tokenStr : expr.split("\\s")) {
            exprTokens.add(Token.getToken(tokenStr));
        }

        String expResult = "90 80 70 - 60 * 50 / + -40 + 30 20 * 10 / -";

        List<Token> result = ExprParser.makeRPN(exprTokens);
        assertEquals(expResult, makePrintableRPN(result));
    }

    /*
     * Test of parse method, of class ExprParser.
     */

    @Test
    public void testParse_NULL入力() {
        Locale.setDefault(Locale.ENGLISH);
        String expResult = "tzetzet.tool.expr.ExprParserException: syntax error: no expression input found.";

        String exprStr = null;
        String result = null;
        try {
            ExprParser.parse(exprStr);
            fail();
        } catch (ExprParserException ex) {
            // Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            result = ex.toString();
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testParse_空文字列入力() {
        Locale.setDefault(Locale.JAPANESE);
        String expResult = "tzetzet.tool.expr.ExprParserException: 文法エラー: 数式が見つかりません.";

        String exprStr = "";
        String result = null;
        try {
            ExprParser.parse(exprStr);
            fail();
        } catch (ExprParserException ex) {
            // Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            result = ex.toString();
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testParser_正常系1() throws ExprParserException {
        String exprStr = "90 +((80- 70 ) * 60) /50 \r\n  + -40 -30 *  20 / 10";
        String expResult = "90 80 70 - 60 * 50 / + -40 + 30 20 * 10 / -";

        List<Token> result = ExprParser.parse(exprStr);
        assertEquals(expResult, makePrintableRPN(result));
    }

    @Test
    public void testParser_正常系_加減算のみ() throws ExprParserException {
        String exprStr = "1 + 2 + 3 - 4";
        String expResult = "1 2 + 3 + 4 -";

        List<Token> result = ExprParser.parse(exprStr);
        assertEquals(expResult, makePrintableRPN(result));
    }

    @Test
    public void testParser_正常系_乗除算のみ() throws ExprParserException {
        String exprStr = "1 * 2 * 3 / 4";
        String expResult = "1 2 * 3 * 4 /";

        List<Token> result = ExprParser.parse(exprStr);
        assertEquals(expResult, makePrintableRPN(result));
    }

    @Test
    public void testParser_正常系_括弧による優先順位1() throws ExprParserException {
        String exprStr = "12 + 34 + 56 * (78 - 9 * 2)";
        String expResult = "12 34 + 56 78 9 2 * - * +";

        List<Token> result = ExprParser.parse(exprStr);
        assertEquals(expResult, makePrintableRPN(result));
    }

    @Test
    public void testParser_正常系_括弧による優先順位2() throws ExprParserException {
        String[] exprStrs = {
            "1 + 2 * 3 - 4",
            "(1 + 2) * 3 - 4",
            "(1 + 2 * 3) - 4",
            "((1 + 2) * 3) - 4",
            "1 + (2 * 3) - 4",
            "1 + (2 * 3 - 4)",
            "1 + (2 * (3 - 4))",
            "1 + 2 * (3 - 4)",
            "1 + (2) * 3 - 4",
        };
        String[] expResults = {
            "1 2 3 * + 4 -",
            "1 2 + 3 * 4 -",
            "1 2 3 * + 4 -",
            "1 2 + 3 * 4 -",
            "1 2 3 * + 4 -",
            "1 2 3 * 4 - +",
            "1 2 3 4 - * +",
            "1 2 3 4 - * +",
            "1 2 3 * + 4 -",
        };

        for (int i = 0; i < exprStrs.length; i++) {
            String exprStr = exprStrs[i];
            String expResult = expResults[i];
            List<Token> result = ExprParser.parse(exprStr);
            assertEquals(expResult, makePrintableRPN(result));
        }
    }

    @Test
    public void testParser_正常系_符号付き整数() throws ExprParserException {
        String exprStr = "-10 + 20 * 30 - 40 +  - 50";
        String expResult = "-10 20 30 * + 40 - -50 +";

        List<Token> result = ExprParser.parse(exprStr);
        assertEquals(expResult, makePrintableRPN(result));
    }
}
