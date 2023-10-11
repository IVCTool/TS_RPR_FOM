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
 *  (see RPR-Enumerations_v2.0.xml) ,  definition see below
*/

 
public enum ArticulatedTypeMetricEnum32 {    
    Position(1),
    PositionRate(2),
    Extension(3),
    ExtensionRate(4),
    X(5),
    XRate(6),
    Y(7),
    YRate(8),
    Z(9),
    ZRate(10),
    Azimuth(11),
    AzimuthRate(12),
    Elevation(13),
    ElevationRate(14),
    Rotation(15),
    RotationRate(16);
    
    private final HLAinteger32BE value ;
    // private final RPRunsignedInteger32BE value;
 
    private ArticulatedTypeMetricEnum32(int value) {       
        //this.value = HLAroot.getEncoderFactory().createHLAinteger32BE(value);
        this.value = HLAroot.getEncoderFactory().createHLAinteger32BE(value);
    }

    public static ArticulatedTypeMetricEnum32 decode (byte[] bytes) throws DecoderException {
        HLAinteger32BE dataElement = HLAroot.getEncoderFactory().createHLAinteger32BE();
        dataElement.decode(bytes);
        
        switch (dataElement.getValue()) {         
            case  1 : return Position;             //(1),
            case  2 : return PositionRate;             //(2),
            case  3 : return Extension;             //(3),
            case  4 : return ExtensionRate;             //(4),
            case  5 : return X;             //(5),
            case  6 : return XRate;             //(6),
            case  7 : return Y;             //(7),
            case  8 : return YRate;             //(8),
            case  9 : return Z;             //(9),
            case  10 : return ZRate;             //(10),
            case  11 : return Azimuth;             //(11),
            case  12 : return AzimuthRate;             //(12),
            case  13 : return Elevation;             //(13),
            case  14 : return ElevationRate;             //(14),
            case  15 : return Rotation;             //(15),
            case  16 : return RotationRate;             //(16);
            
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

/*
 <enumeratedData>
                ArticulatedTypeMetricEnum32
                <representation>RPRunsignedInteger32BE</representation>
                <semantics>Articulated part type metric</semantics>
                    Position     1
                    PositionRate  2
                    Extension     3
                    ExtensionRate   4
                    X                 5
                    XRate              6
                    Y                  7
                    YRate               8
                    Z                   9
                    ZRate               10
                    Azimuth              11
                    AzimuthRate           12
                    Elevation             13
                    ElevationRate           14
                    Rotation               15
                    RotationRate           16
            </enumeratedData>
            <enumeratedData>
 */


