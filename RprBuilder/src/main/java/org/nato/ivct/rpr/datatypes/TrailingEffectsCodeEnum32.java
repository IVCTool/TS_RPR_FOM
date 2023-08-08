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

import org.nato.ivct.rpr.HLAroot;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAinteger32BE;

/** 
 * Damaged appearance (see RPR-Enumerations_v2.0.xml)
*/

public enum TrailingEffectsCodeEnum32 {
    NoTrail(0),
    SmallTrail(1),
    MediumTrail(2),
    LargeTrail(3);
  
    private final HLAinteger32BE value;
 
    private TrailingEffectsCodeEnum32(int value) {
        this.value = HLAroot.getEncoderFactory().createHLAinteger32BE(value);
    }

    public static TrailingEffectsCodeEnum32 decode (byte[] bytes) throws DecoderException {
        HLAinteger32BE dataElement = HLAroot.getEncoderFactory().createHLAinteger32BE();
        dataElement.decode(bytes);
        switch (dataElement.getValue()) {
            case 0: return NoTrail;
            case 1: return SmallTrail;
            case 2: return MediumTrail;
            case 3: return LargeTrail;
            default: throw new DecoderException("invalid enum value");
        }
    }

    public DataElement getDataElement() {
        return value;
    }

    public int getValue() {
        return value.getValue();
    }
}
