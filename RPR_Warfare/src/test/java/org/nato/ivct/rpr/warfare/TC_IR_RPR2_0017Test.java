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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;


public class TC_IR_RPR2_0017Test {

    public static final String SUT_FEDERATE_NAME = "Flyer1"; 
    public static final String SUT_FEDERATION_NAME = "TestFederation"; 
    public static final String SUT_PARAMETER = "{ \"timeout\" : \"500000\" }"; 

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0017Test.class);
    RTIambassador rtiAmbassador = null;

  
    @Test
    @Tag("RefFedAircraft")
    void testRefFedAircraft() throws FileNotFoundException, IOException, ParseException {
        RPR_Warfare_TestSuite ts = new RPR_Warfare_TestSuite();
        AbstractTestCaseIf tc = ts.getTestCase("org.nato.ivct.rpr.warfare.TC_IR_RPR2_0017");
        JSONObject p = ts.getParameterTemplate();
        p.put("timeout", "100");

        tc.setSutFederateName(SUT_FEDERATE_NAME);
        tc.setFederationName(SUT_FEDERATION_NAME);
        tc.setSkipOperatorMsg(true);
        tc.setTcParam(p);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }

    @Test
    @Disabled
    @Tag("VRforces")
    void testVRforces() throws FileNotFoundException, IOException, ParseException {
        RPR_Warfare_TestSuite ts = new RPR_Warfare_TestSuite();
        AbstractTestCaseIf tc = ts.getTestCase("org.nato.ivct.rpr.warfare.TC_IR_RPR2_0017");
        JSONObject p = ts.getParameterTemplate();

        tc.setSutFederateName("VR-Forces Sim Engine");
        tc.setFederationName("MAK-RPR-2.0");
        tc.setSkipOperatorMsg(true);
        tc.setTcParam(SUT_PARAMETER);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }

    @Test
    @Disabled
    @Tag("CoreDS")
    void testCoreDS() throws FileNotFoundException, IOException, ParseException {
        RPR_Warfare_TestSuite ts = new RPR_Warfare_TestSuite();
        AbstractTestCaseIf tc = ts.getTestCase("org.nato.ivct.rpr.warfare.TC_IR_RPR2_0017");
        JSONObject p = ts.getParameterTemplate();

        tc.setSutFederateName("X-Plane");
        tc.setFederationName("MAK-RPR-2.0");
        tc.setSkipOperatorMsg(true);
        tc.setTcParam(SUT_PARAMETER);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }
}
