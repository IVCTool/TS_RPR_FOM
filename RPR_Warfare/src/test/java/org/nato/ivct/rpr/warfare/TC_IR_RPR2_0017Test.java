package org.nato.ivct.rpr.warfare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.IVCT_Verdict;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;


public class TC_IR_RPR2_0017Test {

    public static final String SUT_FEDERATE_NAME = "Flyer1"; 
    public static final String SUT_FEDERATION_NAME = "TestFederation"; 
    public static final String SUT_PARAMETER = "{ \"timeout\" : \"5000\" }"; 

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0017Test.class);
    RTIambassador rtiAmbassador = null;

  
    @Test
    void testPerformTest() throws FileNotFoundException, IOException, ParseException {
        // TC_IR_RPR2_0017 tc = new TC_IR_RPR2_0017();
        RPR_Warfare_TestSuite ts = new RPR_Warfare_TestSuite();
        AbstractTestCaseIf tc = ts.getTestCase("org.nato.ivct.rpr.warfare.TC_IR_RPR2_0017");
        JSONObject p = ts.getParameterTemplate();

        tc.setSutFederateName(SUT_FEDERATE_NAME);
        tc.setFederationName(SUT_FEDERATION_NAME);
        tc.setSkipOperatorMsg(true);
        tc.setTcParam(p);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }

}
