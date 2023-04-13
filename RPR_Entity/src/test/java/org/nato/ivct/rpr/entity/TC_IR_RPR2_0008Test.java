package org.nato.ivct.rpr.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iosb.tc_lib_if.IVCT_Verdict;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;


public class TC_IR_RPR2_0008Test {

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0008Test.class);
    RTIambassador rtiAmbassador = null;

  
    @Test
    void testPerformTest() {
        TC_IR_RPR2_0008 tc = new TC_IR_RPR2_0008();
        tc.setSutFederateName("Flyer1");
        tc.setFederationName("TestFederation");
        tc.setSkipOperatorMsg(true);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);

    }

    @Disabled
    @Test
    void testPerformTestHologate() {
        TC_IR_RPR2_0008 tc = new TC_IR_RPR2_0008();
        tc.setSutFederateName("Hologate");
        tc.setFederationName("HologateFederation");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }

    @Test
    void testPerformTestCoreDS() {
        TC_IR_RPR2_0008 tc = new TC_IR_RPR2_0008();
        tc.setSutFederateName("X-Plane");
        tc.setFederationName("MAK-RPR-2.0");
        tc.setSkipOperatorMsg(true);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }

    @Test
    void testPerformTestVRforces() {
        TC_IR_RPR2_0008 tc = new TC_IR_RPR2_0008();
        tc.setSutFederateName("Federate27");
        tc.setFederationName("MAK-RPR-2.0");
        tc.setSkipOperatorMsg(true);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }
}
