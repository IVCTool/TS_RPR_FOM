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

 // TODO  complete the list with all other   ca. 150 Elements  
public enum ArticulatedPartsTypeEnum32 {    
    Other(0),
    Rudder(1024),
    LeftFlap(1056),
    RightFlap(1088),
    LeftAileron(1120),
    RightAileron(1152),
    HelicopterMainRotor(1184),
    HelicopterTailRotor(1216),
    OtherAircraftControlSurfaces(1248),
    PropellerNumber1(1280),    
    PropellerNumber2(1312),
    PropellerNumber3(1344),
    PropellerNumber4(1376),
    LeftStabilator_StabilatorNumber1_(1408),
    RightStabilator_StabilatorNumber2_(1440);
    
    
    private final HLAinteger32BE value ;
    // private final RPRunsignedInteger32BE value;
 
    private ArticulatedPartsTypeEnum32(int value) {       
        //this.value = HLAroot.getEncoderFactory().createHLAinteger32BE(value);
        this.value = HLAroot.getEncoderFactory().createHLAinteger32BE(value);
    }

    public static ArticulatedPartsTypeEnum32 decode (byte[] bytes) throws DecoderException {
        HLAinteger32BE dataElement = HLAroot.getEncoderFactory().createHLAinteger32BE();
        dataElement.decode(bytes);
        
     // TODO  complete the list with 150  more  Elements 
        switch (dataElement.getValue()) {            
            case 0:         return Other;             //                     0
            case 1024:    return  Rudder;             //                    1024
            case 1056:     return LeftFlap;             //                    1056
            case 1088:     return RightFlap;             //                    1088
            case 1120:     return LeftAileron;             //                    1120
            case 1152:     return RightAileron;             //                    1152
            case 1184:     return HelicopterMainRotor;            //             //                    1184
            case 1216:     return HelicopterTailRotor;             //                    1216
            case 1248:     return OtherAircraftControlSurfaces;             //                    1248
            case 1280:     return PropellerNumber1;             //                    1280
            case 1312:     return PropellerNumber2;             //                    1312
            case 1344:     return PropellerNumber3;             //                    1344
            case 1376:     return PropellerNumber4;             //                    1376
            case 1408:     return LeftStabilator_StabilatorNumber1_;             //_                    1408
            case 1440:     return RightStabilator_StabilatorNumber2_;             //_                    1440       
       
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
 ArticulatedPartsTypeEnum32
                <representation>RPRunsignedInteger32BE</representation>
                <semantics>Articulated part type class</semantics>
                   Other                     0
                   Rudder                    1024
                   LeftFlap                    1056
                   RightFlap                    1088
                   LeftAileron                    1120
                   RightAileron                    1152
                   HelicopterMainRotor                    1184
                   HelicopterTailRotor                    1216
                   OtherAircraftControlSurfaces                    1248
                   PropellerNumber1                    1280
                   PropellerNumber2                    1312
                   PropellerNumber3                    1344
                   PropellerNumber4                    1376
                   LeftStabilator_StabilatorNumber1_                    1408
                   RightStabilator_StabilatorNumber2_                    1440
                   LeftRuddervator_RuddervatorNumber1_                    1472
                   RightRuddervator_RuddervatorNumber2_                    1504
                   LeftLeadingEdgeFlap_Slat                    1536
                   RightLeadingEdgeFlap_Slat                    1568
                   Periscope                    2048
                   GenericAntenna                    2080
                   Snorkel                    2112
                   OtherExtendableParts                    2144
                   LandingGear                    3072
                   TailHook                    3104
                   SpeedBrake                    3136
                   LeftWeaponBayDoors                    3168
                   RightWeaponBayDoors                    3200
                   TankOrAPChatch                    3232
                   Wingsweep                    3264
                   BridgeLauncher                    3296
                   BridgeSection1                    3328
                   BridgeSection2                    3360
                   BridgeSection3                    3392
                   PrimaryBlade1                    3424
                   PrimaryBlade2                    3456
                   PrimaryBoom                    3488
                   PrimaryLauncherArm                    3520
                   OtherFixedPositionParts                    3552
                   LandingGear-Nose                    3584
                   LandingGear-LeftMain                    3616
                   LandingGear-RightMain                    3648
                   LeftSide_SecondaryWeaponBayDoors                    3680
                   RightSide_SecondaryWeaponBayDoors                    3712
                   PrimaryTurretNumber1                    4096
                   PrimaryTurretNumber2                    4128
                   PrimaryTurretNumber3                    4160
                   PrimaryTurretNumber4                    4192
                   PrimaryTurretNumber5                    4224
                   PrimaryTurretNumber6                    4256
                   PrimaryTurretNumber7                    4288
                   PrimaryTurretNumber8                    4320
                   PrimaryTurretNumber9                    4352
                   PrimaryTurretNumber10                    4384
                   PrimaryGunNumber1                    4416
                   PrimaryGunNumber2                    4448
                   PrimaryGunNumber3                    4480
                   PrimaryGunNumber4                    4512
                   PrimaryGunNumber5                    4544
                   PrimaryGunNumber6                    4576
                   PrimaryGunNumber7                    4608
                   PrimaryGunNumber8                    4640
                   PrimaryGunNumber9                    4672
                   PrimaryGunNumber10                    4704
                   PrimaryLauncher1                    4736
                   PrimaryLauncher2                    4768
                   PrimaryLauncher3                    4800
                   PrimaryLauncher4                    4832
                   PrimaryLauncher5                    4864
                   PrimaryLauncher6                    4896
                   PrimaryLauncher7                    4928
                   PrimaryLauncher8                    4960
                   PrimaryLauncher9                    4992
                   PrimaryLauncher10                    5024
                   PrimaryDefenseSystems1                    5056
                   PrimaryDefenseSystems2                    5088
                   PrimaryDefenseSystems3                    5120
                   PrimaryDefenseSystems4                    5152
                   PrimaryDefenseSystems5                    5184
                   PrimaryDefenseSystems6                    5216
                   PrimaryDefenseSystems7                    5248
                   PrimaryDefenseSystems8                    5280
                   PrimaryDefenseSystems9                    5312
                   PrimaryDefenseSystems10                    5344
                   PrimaryRadar1                    5376
                   PrimaryRadar2                    5408
                   PrimaryRadar3                    5440
                   PrimaryRadar4                    5472
                   PrimaryRadar5                    5504
                   PrimaryRadar6                    5536
                   PrimaryRadar7                    5568
                   PrimaryRadar8                    5600
                   PrimaryRadar9                    5632
                   PrimaryRadar10                    5664
                   SecondaryTurretNumber1                    5696
                   SecondaryTurretNumber2                    5728
                   SecondaryTurretNumber3                    5760
                   SecondaryTurretNumber4                    5792
                   SecondaryTurretNumber5                    5824
                   SecondaryTurretNumber6                    5856
                   SecondaryTurretNumber7                    5888
                   SecondaryTurretNumber8                    5920
                   SecondaryTurretNumber9                    5952
                   SecondaryTurretNumber10                    5984
                   SecondaryGunNumber1                    6016
                   SecondaryGunNumber2                    6048
                   SecondaryGunNumber3                    6080
                   SecondaryGunNumber4                    6112
                   SecondaryGunNumber5                    6144
                   SecondaryGunNumber6                    6176
                   SecondaryGunNumber7                    6208
                   SecondaryGunNumber8                    6240
                   SecondaryGunNumber9                    6272
                   SecondaryGunNumber10                    6304
                   SecondaryLauncher1                    6336
                   SecondaryLauncher2                    6368
                   SecondaryLauncher3                    6400
                   SecondaryLauncher4                    6432
                   SecondaryLauncher5                    6464
                   SecondaryLauncher6                    6496
                   SecondaryLauncher7                    6528
                   SecondaryLauncher8                    6560
                   SecondaryLauncher9                    6592
                   SecondaryLauncher10                    6624
                   SecondaryDefenseSystems1                    6656
                   SecondaryDefenseSystems2                    6688
                   SecondaryDefenseSystems3                    6720
                   SecondaryDefenseSystems4                    6752
                   SecondaryDefenseSystems5                    6784
                   SecondaryDefenseSystems6                    6816
                   SecondaryDefenseSystems7                    6848
                   SecondaryDefenseSystems8                    6880
                   SecondaryDefenseSystems9                    6912
                   SecondaryDefenseSystems10                    6944
                   SecondaryRadar1                    6976
                   SecondaryRadar2                    7008
                   SecondaryRadar3                    7040
                   SecondaryRadar4                    7072
                   SecondaryRadar5                    7104
                   SecondaryRadar6                    7136
                   SecondaryRadar7                    7168
                   SecondaryRadar8                    7200
                   SecondaryRadar9                    7232
                   SecondaryRadar10                    7264
                   DeckElevator1                    7296
                   DeckElevator2                    7328
                   Catapult1                    7360
                   Catapult2                    7392
                   JetBlastDeflector1                    7424
                   JetBlastDeflector2                    7456
                   ArrestorWires1                    7488
                   ArrestorWires2                    7520
                   ArrestorWires3                    7552
                   WingOrRotorFold                    7584
                   FuselageFold                    7616
                   CargoDoor                    7648
                   CargoRamp                    7680
                   Air-to-AirRefuelingBoom                    7712 
 */


