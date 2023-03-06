/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

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

import org.nato.ivct.rpr.Builder;

import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAinteger32BE;

/** 
 * Damaged appearance (see RPR-Enumerations_v2.0.xml)
*/
public enum DamageStatusEnum32 {
    NoDamage(0),
    SlightDamage(1),
    ModerateDamage(2),
    Destroyed(3);
  
    private final HLAinteger32BE value;
 
    private DamageStatusEnum32(int value) {
        this.value = Builder.getEncoderFactory().createHLAinteger32BE(value);
    }

    public static DamageStatusEnum32 decode (byte[] bytes) throws DecoderException {
        HLAinteger32BE dataElement = Builder.getEncoderFactory().createHLAinteger32BE();
        dataElement.decode(bytes);
        switch (dataElement.getValue()) {
            case 0: return NoDamage;
            case 1: return SlightDamage;
            case 2: return ModerateDamage;
            case 3: return Destroyed;
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
