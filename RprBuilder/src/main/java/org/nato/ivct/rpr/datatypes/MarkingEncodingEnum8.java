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
import hla.rti1516e.encoding.HLAoctet;


/** 
 *  (see RPR-Enumerations_v2.0.xml)   definition see below
*/

public enum MarkingEncodingEnum8 {    
    Other( (byte)  0),
    ASCII( (byte)1),
    ArmyMarkingCCTT( (byte)2),
    DigitChevron( (byte)3);
    
  
    private final HLAoctet value;
 
    private MarkingEncodingEnum8(byte value) {       
        this.value = HLAroot.getEncoderFactory().createHLAoctet(value);
    }

    public static MarkingEncodingEnum8 decode (byte[] bytes) throws DecoderException {
        HLAoctet dataElement = HLAroot.getEncoderFactory().createHLAoctet();
        dataElement.decode(bytes);
        switch (dataElement.getValue()) {
            case 0:            return Other;
            case 1:            return ASCII;
            case 2:            return ArmyMarkingCCTT;
            case 3:            return DigitChevron;
            default:
                throw new DecoderException("invalid enum value");
        }
    }

    public DataElement getDataElement() {
        return value;
    }

    public int getValue() {
        return value.getValue();
    }
}

/** 
 *  (see RPR-Enumerations_v2.0.xml)  
 <enumeratedData>
                <name>MarkingEncodingEnum8</name>
                <representation>HLAoctet</representation>
                <semantics>Marking character set</semantics>
                <enumerator>
                    <name>Other</name>
                    <value>0</value>
                </enumerator>
                <enumerator>
                    <name>ASCII</name>
                    <value>1</value>
                </enumerator>
                <enumerator>
                    <name>ArmyMarkingCCTT</name>
                    <value>2</value>
                </enumerator>
                <enumerator>
                    <name>DigitChevron</name>
                    <value>3</value>
                </enumerator>
            </enumeratedData>
*/
