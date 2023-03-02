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
 * Camouflage type (see RPR-Enumerations_v2.0.xml)
 */
public enum CamouflageEnum32 {
    UniformPaintScheme(0),
    DesertCamouflage(1),
    WinterCamouflage(2),
    ForestCamouflage(3),
    GenericCamouflage(4);

    private final HLAinteger32BE value;
    
    private CamouflageEnum32(int value) {
        this.value = Builder.getEncoderFactory().createHLAinteger32BE(value);
    }
    
    public static CamouflageEnum32 decode(byte[] bytes) throws DecoderException  {
        HLAinteger32BE de = Builder.getEncoderFactory().createHLAinteger32BE();
        de.decode(bytes);
        switch (de.getValue()) {
            case 0: return UniformPaintScheme;
            case 1: return DesertCamouflage;
            case 2: return WinterCamouflage;
            case 3: return ForestCamouflage;
            case 4: return GenericCamouflage;
            default: return null;
        }
    }

    public int getValue() {
        return value.getValue();
    }

    public DataElement getDataElement() {
        return value;
    }

}
