package org.nato.ivct.rpr.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iosb.tc_lib_if.IVCT_Verdict;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;

public class TC_IR_RPR2_0011Test {

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0011Test.class);
    RTIambassador rtiAmbassador = null;
    
    @Test
    void testPerformTest() {
        TC_IR_RPR2_0011 tc = new TC_IR_RPR2_0011();
        tc.setSutFederateName("Flyer1");
        tc.setFederationName("TestFederation");
        tc.setSkipOperatorMsg(true);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }

    @Test
    @Disabled
    void testNETN_ETR_Test() {
        TC_IR_RPR2_0011 tc = new TC_IR_RPR2_0011();
        tc.setSutFederateName("TC_IR_RPR2_0011");
        tc.setFederationName("NETN-ETR_TEST");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }

    @Test
    @Disabled
    void testPerformTestHologate() {
        TC_IR_RPR2_0011 tc = new TC_IR_RPR2_0011();
        tc.setSutFederateName("Hologate");
        tc.setFederationName("HologateFederation");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }


}
