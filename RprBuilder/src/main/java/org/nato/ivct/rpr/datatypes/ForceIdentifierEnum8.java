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
import  hla.rti1516e.encoding.HLAoctet;

/** 
 *      ForceIdentifierEnum8        (see RPR-Enumerations_v2.0.xml)
*/
public enum ForceIdentifierEnum8 { 
    Other((byte)0),
    Friendly((byte)1),
    Opposing((byte)2),
    Neutral((byte)3),
    Friendly_2((byte)4),
    Opposing_2((byte)5),
    Neutral_2((byte)6),
    Friendly_3((byte)7),
    Opposing_3((byte)8),
    Neutral_3((byte)9),
    Friendly_4((byte)10),
    Opposing_4((byte)11),
    Neutral_4((byte)12),
    Friendly_5((byte)13),
    Opposing_5((byte)14),
    Neutral_5((byte)15),
    Friendly_6((byte)16),
    Opposing_6((byte)17),
    Neutral_6((byte)18),
    Friendly_7((byte)19),
    Opposing_7((byte)20),
    Neutral_7((byte)21),
    Friendly_8((byte)22),
    Opposing_8((byte)23),
    Neutral_8((byte)24),
    Friendly_9((byte)25),
    Opposing_9((byte)26),
    Neutral_9((byte)27),
    Friendly_10((byte)28),
    Opposing_10((byte)29),
    Neutral_10((byte)30);
    
    private final HLAoctet value;
 
    private ForceIdentifierEnum8( byte value) {
        this.value = HLAroot.getEncoderFactory().createHLAoctet(value);
    }
    
    public DataElement getDataElement() {
        return value;
    }

    public int getValue() {
        return value.getValue();
    }

    public static ForceIdentifierEnum8 decode (byte[] bytes) throws DecoderException {
        HLAoctet dataElement =HLAroot.getEncoderFactory().createHLAoctet();
        dataElement.decode(bytes);
        switch (dataElement.getValue()) {
            case 0: return Other;
            case 1: return Friendly;
            case 2: return Opposing;
            case 3: return Neutral;            
            case 4: return Friendly_2;
            case 5: return Opposing_2;
            case 6: return Neutral_2;
            case 7: return Friendly_3;
            case 8: return Opposing_3;
            case 9: return Neutral_3;
            case 10: return Friendly_4;
            case 11: return Opposing_4;
            case 12: return Neutral_4;
            case 13: return Friendly_5;
            case 14: return Opposing_5;
            case 15: return Neutral_5;
            case 16: return Friendly_6;
            case 17: return Opposing_6;
            case 18: return Neutral_6;
            case 19: return Friendly_7;
            case 20: return Opposing_7;
            case 21: return Neutral_7;
            case 22: return Friendly_8;
            case 23: return Opposing_8;
            case 24: return Neutral_8;
            case 25: return Friendly_9;
            case 26: return Opposing_9;
            case 27: return Neutral_9;
            case 28: return Friendly_10;
            case 29: return Opposing_10;
            case 30: return Neutral_10;     
            default: throw new DecoderException("invalid enum value");
        }
    }

   
}
