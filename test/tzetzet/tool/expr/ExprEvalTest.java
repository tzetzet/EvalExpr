/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tzetzet.tool.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sue
 */
public class ExprEvalTest {

    public ExprEvalTest() {
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

    /*
     * Test of evalRPN method, of class ExprEval.
     */

    @Test
    public void testEvalRPN_NULL入力() {
        String expResult = "java.lang.IllegalArgumentException: rpnTokens: null";

        String result = null;
        List<Token> rpnTokens = null;
        try {
            ExprEval.evalRPN(rpnTokens);
            fail();
        } catch (ExprEvalException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (IllegalArgumentException ex) {
            // Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            result = ex.toString();
        }

        assertEquals(expResult, result);
    }

    @Test
    public void testEvalRPN_空リスト入力() {
        String expResult = "java.lang.IllegalArgumentException: rpnTokens: []";

        String result = null;
        List<Token> rpnTokens = Collections.EMPTY_LIST;
        try {
            ExprEval.evalRPN(rpnTokens);
            fail();
        } catch (ExprEvalException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (IllegalArgumentException ex) {
            // Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            result = ex.toString();
        }

        assertEquals(expResult, result);
    }

    @Test
    public void testEvalRPN_正常系() throws ExprEvalException {
        String rpn = "90 80 70 - 60 * 50 / + -40 + 30 20 * 10 / -";
        List<Token> rpnTokens = new ArrayList<>();
        for (String token : rpn.split("\\s")) {
            rpnTokens.add(Token.getToken(token));
        }
        int expResult = 2;

        int result = ExprEval.evalRPN(rpnTokens);

        assertEquals(expResult, result);
    }
}
