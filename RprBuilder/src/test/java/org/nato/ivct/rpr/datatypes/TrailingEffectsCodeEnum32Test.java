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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import hla.rti1516e.encoding.DecoderException;

public class TrailingEffectsCodeEnum32Test {
    
    @Test
    void testEncodeDecode() throws DecoderException {
        TrailingEffectsCodeEnum32 element = TrailingEffectsCodeEnum32.NoTrail;
        byte[] bytes = element.getDataElement().toByteArray();
        TrailingEffectsCodeEnum32 decodedValue = TrailingEffectsCodeEnum32.decode(bytes);
        assertEquals(element, decodedValue);
    }

    @Test
    void testGetEncodedLength() {
        TrailingEffectsCodeEnum32 value = TrailingEffectsCodeEnum32.SmallTrail;
        assertTrue(4 == value.getDataElement().getEncodedLength());
    }
    
    @Test
    void testGetOctetBoundary() {
        TrailingEffectsCodeEnum32 value = TrailingEffectsCodeEnum32.MediumTrail;
        assertTrue(4 == value.getDataElement().getEncodedLength());
    }
    
    @Test
    void testToByteArray() {
        TrailingEffectsCodeEnum32 value = TrailingEffectsCodeEnum32.LargeTrail;
        byte[] bytes = value.getDataElement().toByteArray();
        assertTrue(0 == bytes[0]);
        assertTrue(0 == bytes[1]);
        assertTrue(0 == bytes[2]);
        assertTrue(3 == bytes[3]);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
