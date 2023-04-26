package org.nato.ivct.rpr.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;


public class TC_IR_RPR2_0009Test {

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0009Test.class);
    RTIambassador rtiAmbassador = null;

  
    @Test
    void testPerformTest() {
        TC_IR_RPR2_0009 tc = new TC_IR_RPR2_0009();
        tc.setSutFederateName("TC_IR_RPR2_0009");
        tc.setFederationName("TestFederation");
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }
}
