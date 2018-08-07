package taboo.exam.calc;

import taboo.exam.calc.Utils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class UtilsTest {
    @Test
    void simplify() {
        assertTrue(Utils.simplify("5+3*10").equals("35"));
        //todo: mltiple memebers (10+5+3*10)
    }

    @Test
    void isNumber() {
        assertTrue(Utils.isNumber("1"));
        assertFalse(Utils.isNumber("1.1"));
        assertTrue(Utils.isNumber("-1"));
        assertFalse(Utils.isNumber("-1.2"));
    }

    @Test
    void isEquation() {
        assertTrue(Utils.isEquation("1 + 3"));
        assertTrue(Utils.isEquation("10/2"));
        assertTrue(Utils.isEquation("1+3+5"));
        assertTrue(Utils.isEquation("1+3*10"));
        assertFalse(Utils.isEquation("i++ + 3"));
    }

    @Test
    void isJavaExpression() {
        assertTrue(Utils.isJavaExpression("i++"));
        assertTrue(Utils.isJavaExpression("i++ + 5"));
    }

    @Test
    void isSimpleEquation() {
        assertTrue(Utils.isSimpleEquation(Utils.listParts("10+5")));
        assertFalse(Utils.isSimpleEquation(Utils.listParts("i+5")));
        assertTrue(Utils.isSimpleEquation(Utils.listParts("5+1")));
        assertFalse(Utils.isSimpleEquation(Utils.listParts("5~1")));
    }

    @Test
    void listParts() {
        assertTrue(Utils.listParts("10+3").size()==3);
        assertTrue(Utils.listParts("10+3+1").size()==5);
        assertTrue(Utils.listParts("5*4+3").size()==3);
        //5*4 will be calculated and returned "20,+,3"
    }

    @Test
    void solveSimpleEquation() {
        assertTrue(Utils.solveSimpleEquation(Utils.listParts("10+5")).equals("15"));
        //todo: add negative number testing
        //todo: add minus test
        //todo: add multiply, devide
    }

    @Test
    void isOperator() {
        assertTrue(Utils.isOperator("+"));
        assertTrue(Utils.isOperator("-"));
        assertTrue(Utils.isOperator("*"));
        assertTrue(Utils.isOperator("/"));
    }

}