package org.nato.ivct.rpr.warfare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;

public class TC_IR_RPR2_0018Test {

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0018Test.class);
    RTIambassador rtiAmbassador = null;
    
    @Test
    void testPerformTest() {
        TC_IR_RPR2_0018 tc = new TC_IR_RPR2_0018();
        tc.setSutFederateName("Flyer1");
        tc.setFederationName("TestFederation");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }

    @Test
    @Disabled
    void testNETN_ETR_Test() {
        TC_IR_RPR2_0018 tc = new TC_IR_RPR2_0018();
        tc.setSutFederateName("TC_IR_RPR2_0011");
        tc.setFederationName("NETN-ETR_TEST");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }

    @Test
    @Disabled
    void testPerformTestHologate() {
        TC_IR_RPR2_0018 tc = new TC_IR_RPR2_0018();
        tc.setSutFederateName("Hologate");
        tc.setFederationName("HologateFederation");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }


}
