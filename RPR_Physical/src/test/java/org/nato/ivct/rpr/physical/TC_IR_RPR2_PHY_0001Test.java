package org.nato.ivct.rpr.physical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iosb.tc_lib_if.IVCT_Verdict;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import hla.rti1516e.RTIambassador;


public class TC_IR_RPR2_PHY_0001Test {

    public static final Logger log = LoggerFactory.getLogger(TC_IR_RPR2_PHY_0001Test.class);
    RTIambassador rtiAmbassador = null;

    @Test
    void performTest() {
        TC_IR_RPR2_PHY_0001 tc = new TC_IR_RPR2_PHY_0001();
        tc.setSutFederateName("Flyer1");
        tc.setFederationName("TestFederation");
        tc.setSkipOperatorMsg(true);
        IVCT_Verdict result = tc.execute(log);
        assertTrue(result.verdict == IVCT_Verdict.Verdict.PASSED);
    }
}
