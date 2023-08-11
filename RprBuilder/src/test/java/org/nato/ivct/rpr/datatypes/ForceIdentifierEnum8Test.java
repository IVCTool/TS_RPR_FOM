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

public class ForceIdentifierEnum8Test {
    @Test
    void testEncodeDecode() throws DecoderException {
        ForceIdentifierEnum8 element = ForceIdentifierEnum8.Friendly;
        byte[] bytes = element.getDataElement().toByteArray();
        ForceIdentifierEnum8 decodedValue = ForceIdentifierEnum8.decode(bytes);
        assertEquals(element, decodedValue);
    }

    @Test
    void testGetEncodedLength() {
        ForceIdentifierEnum8 element = ForceIdentifierEnum8.Neutral;
        assertTrue(1 == element.getDataElement().getEncodedLength());
    }

    @Test
    void testGetOctetBoundary() {
        ForceIdentifierEnum8 element = ForceIdentifierEnum8.Opposing;
        assertTrue(1 == element.getDataElement().getOctetBoundary());
    }

    @Test
    void testToByteArray() {
        ForceIdentifierEnum8 element = ForceIdentifierEnum8.Other;
        byte[] bytes = element.getDataElement().toByteArray();
        assertTrue(0 == bytes[0]);
    }
}
