package org.nato.ivct.refFed;

import org.junit.jupiter.api.Test;


public class AircraftAppTest {
    
    @Test
    void testAircraftApp () {
        String[] args = {"-rtiHost", "localhost", "-federationName", "TestFederation", "-federateName", "Flyer1", "-pFlyAircraft"};
        AircraftApp.main(args);
    }
}
