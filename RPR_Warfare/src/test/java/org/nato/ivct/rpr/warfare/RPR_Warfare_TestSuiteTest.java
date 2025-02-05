package org.nato.ivct.rpr.warfare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ServiceLoader;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TestSuite;


public class RPR_Warfare_TestSuiteTest {

    @Test
    void testGetTestCase() {
        RPR_Warfare_TestSuite ts;
        try {
            ts = new RPR_Warfare_TestSuite();
            AbstractTestCaseIf tc = ts.getTestCase("org.nato.ivct.rpr.warfare.TC_IR_RPR2_0017");
            assertEquals(tc.getClass().getName(),"org.nato.ivct.rpr.warfare.TC_IR_RPR2_0017");
        } catch (IOException | ParseException e) {
            fail(e);
        }
    }

    @Test
    void testServiceLoader() {
        ServiceLoader<TestSuite> loader = ServiceLoader.load(TestSuite.class);
        for (TestSuite factory : loader) {
            String label = factory.getId();
            assertEquals(RPR_Warfare_TestSuite.TEST_SUITE_ID, label);
            AbstractTestCaseIf tc = factory.getTestCase("org.nato.ivct.rpr.warfare.TC_IR_RPR2_0018");
            assertEquals(tc.getClass().getName(),"org.nato.ivct.rpr.warfare.TC_IR_RPR2_0018");
        }
    }

    @Test
    void testGetDescription() {

    }

    @Test
    void testGetId() {

    }

    @Test
    void testGetJSONDescriptionObject() {

    }

    @Test
    void testGetLibFolder() {

    }    

    @Test
    void testGetName() {

    }    

    @Test
    void testGetParameterTemplate() {

    }    

    @Test
    void testGetRunTimeFolder() {

    }    

    @Test
    void testGetVersion() {

    }
}
