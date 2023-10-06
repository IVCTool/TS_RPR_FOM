/**    Copyright 2022, brf (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.RTIinternalError;

public class ArticulatedTypeMetricEnum32Test {
    @Test
    void testGetValue() {
        assertEquals(1, ArticulatedTypeMetricEnum32.Position.getValue() );
        assertEquals(2, ArticulatedTypeMetricEnum32.PositionRate.getValue() );        
        assertEquals(3, ArticulatedTypeMetricEnum32.Extension.getValue() );
        assertEquals(4, ArticulatedTypeMetricEnum32.ExtensionRate.getValue() );
        assertEquals(5, ArticulatedTypeMetricEnum32.X.getValue() );
        assertEquals(6, ArticulatedTypeMetricEnum32.XRate.getValue() );
        assertEquals(7, ArticulatedTypeMetricEnum32.Y.getValue() );
        assertEquals(8, ArticulatedTypeMetricEnum32.YRate.getValue() );
        assertEquals(9, ArticulatedTypeMetricEnum32.Z .getValue() );
        assertEquals(10, ArticulatedTypeMetricEnum32.ZRate.getValue() );
        assertEquals(11, ArticulatedTypeMetricEnum32.Azimuth.getValue() );
        assertEquals(12, ArticulatedTypeMetricEnum32.AzimuthRate.getValue() );
        assertEquals(13, ArticulatedTypeMetricEnum32.Elevation.getValue() );
        assertEquals(14, ArticulatedTypeMetricEnum32.ElevationRate.getValue() );
        assertEquals(15, ArticulatedTypeMetricEnum32.Rotation.getValue() );
        assertEquals(16, ArticulatedTypeMetricEnum32.RotationRate.getValue() );
        
        ArticulatedTypeMetricEnum32 ratePosition = ArticulatedTypeMetricEnum32.PositionRate;
        assertTrue(ratePosition == ArticulatedTypeMetricEnum32.PositionRate ) ;        
        assertFalse ( ratePosition == ArticulatedTypeMetricEnum32.ExtensionRate) ;
        assertEquals(2 , ratePosition.getValue() ) ;         
        
        ArticulatedTypeMetricEnum32 rateElevation = ArticulatedTypeMetricEnum32.ElevationRate;
        assertFalse(ratePosition == rateElevation);
    
    }
    /*
    @Test
    void testEncoding() throws RTIinternalError, DecoderException {
        CamouflageEnum32 camouflageDesert = CamouflageEnum32.DesertCamouflage;
        byte[] bytes = camouflageDesert.getDataElement().toByteArray();
        assert(bytes.length > 0);
        CamouflageEnum32 camouflageReceived = CamouflageEnum32.decode(bytes);
        assertTrue(camouflageDesert == camouflageReceived);
    }
    */
    
    
}
