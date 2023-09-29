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
 *  (see RPR-Enumerations_v2.0.xml)
*/


 // TODO  complete the list of  enumerator Elements  ca  700 -900 ? 
public enum StationEnum32 {    
    Nothing_Empty(0),              
    Fuselage_Station1(512),                  
    Fuselage_Station2(513),                  
    Fuselage_Station3(514),                  
    Fuselage_Station4(515),                  
    Fuselage_Station5(516),                  
    Fuselage_Station6(517),                  
    Fuselage_Station7(518),                  
    Fuselage_Station8(519),                  
    Fuselage_Station9(520),                  
    Fuselage_Station10(521),
    // ...
    LeftWingStation1(640),
    // ...
    RightWingStation1(768),
    // ...
    M16A42_rifle(896),
    M249_SAW(897),
    M60_Machine_gun(898),
    M203_Grenade_Launcher(899),
    M136_AT4(900),
    M47_Dragon(901),
    AAWS_M_Javelin(902),
    M18A1_Claymore_Mine(903),
    MK19_Grenade_Launcher(904),
    M2_Machine_Gun(905),
    Other_attached_parts(906);
    
  
    private final HLAinteger32BE value;
    // private final RPRunsignedInteger32B value;
 
    private StationEnum32(int value) {       
        this.value = HLAroot.getEncoderFactory().createHLAinteger32BE(value);
    }

    public static StationEnum32 decode (byte[] bytes) throws DecoderException {
        HLAinteger32BE dataElement = HLAroot.getEncoderFactory().createHLAinteger32BE();
        dataElement.decode(bytes);
        
     // TODO  complete the list of  enumerator Elements 
        switch (dataElement.getValue()) {
            case 0:            return Nothing_Empty;
            case 512:            return Fuselage_Station1;
            case 513:            return Fuselage_Station2;
            case 514:            return Fuselage_Station3;
            case 515:            return Fuselage_Station4;
            case 516:            return Fuselage_Station5;
            case 517:            return Fuselage_Station6;
            case 518:            return Fuselage_Station7;
            case 519:            return Fuselage_Station8;
            case 520:            return Fuselage_Station9;
            case 521:            return Fuselage_Station10;

            case 640:            return LeftWingStation1;

            case 768:            return RightWingStation1;

            case 896:            return M16A42_rifle;
            case 897:            return M249_SAW;
            case 898:            return M60_Machine_gun;
            case 899:            return M203_Grenade_Launcher;
            case 900:            return M136_AT4;
            case 901:            return M47_Dragon;
            case 902:            return AAWS_M_Javelin;
            case 903:            return M18A1_Claymore_Mine;
            case 904:            return MK19_Grenade_Launcher;
            case 905:            return M2_Machine_Gun;
            case 906:            return Other_attached_parts;

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
