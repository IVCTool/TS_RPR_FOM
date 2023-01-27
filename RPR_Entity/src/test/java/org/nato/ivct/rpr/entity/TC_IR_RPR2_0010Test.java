package org.nato.ivct.rpr.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;

public class TC_IR_RPR2_0010Test {

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0010Test.class);
    RTIambassador rtiAmbassador = null;
    
    @Test
    void testPerformTest() {
        TC_IR_RPR2_0010 tc = new TC_IR_RPR2_0010();
        tc.setSutFederateName("TC_IR_RPR2_0010");
        tc.setFederationName("TestFederation");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }

    @Test
    void testCapabilitiesTest() {
        TC_IR_RPR2_0010 tc = new TC_IR_RPR2_0010();
        tc.setSutFederateName("TC_IR_RPR2_0010");
        tc.setFederationName("NETN-ETR_TEST");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }
}
