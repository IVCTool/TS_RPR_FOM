package org.nato.ivct.rpr.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;

public class RPR_Entity_TestSuiteTest {
    @Test
    void testGetTCase() {
        try {
            RPR_Entity_TestSuite ts = new RPR_Entity_TestSuite();
            AbstractTestCaseIf tc = ts.getTestCase("org.nato.ivct.rpr.entity.TC_IR_RPR2_0008");
            assertEquals(tc.getClass().getName(),"org.nato.ivct.rpr.entity.TC_IR_RPR2_0008");

        } catch (IOException | ParseException e) {
            fail(e);
        }
    }
}
