package org.nato.ivct.rpr.warfare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;


public class TC_IR_RPR2_0017Test {

    public static final String SUT_FEDERATE_NAME = "Flyer1"; 
    public static final String SUT_FEDERATION_NAME = "TestFederation"; 

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_0017Test.class);
    RTIambassador rtiAmbassador = null;

  
    @Test
    void testPerformTest() {
        TC_IR_RPR2_0017 tc = new TC_IR_RPR2_0017();
        tc.setSutFederateName(SUT_FEDERATE_NAME);
        tc.setFederationName(SUT_FEDERATION_NAME);
        tc.setSkipOperatorMsg(true);
        tc.execute(log);
    }

}
