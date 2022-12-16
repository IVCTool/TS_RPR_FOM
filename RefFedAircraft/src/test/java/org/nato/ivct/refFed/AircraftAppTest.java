package org.nato.ivct.refFed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AircraftAppTest {
    
    @Test
    void testAircraftApp () {
        String[] args = {"-rtiHost", "testHost", "-federationName", "testFederation", "-federateName", "testFederate", "-FlyAircraft"};
        AircraftApp app = new AircraftApp(args);
        assertNotNull(app);
    }
}
