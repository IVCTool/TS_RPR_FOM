/**
  * Copyright 2023, brf (Fraunhofer IOSB)
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http: //www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License. 
     */

package org.nato.ivct.rpr.objects;

import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class Lifeform extends PhysicalEntity{
    
    
    //  PhysicalEntity-Lifeform : 5 attributes  SISOTable 10
    public enum Attributes {
        FlashLightsOn,                 // RPRboolean
        StanceCode,                    // StanceCodeEnum32
        PrimaryWeaponState,           // WeaponStateEnum32
        SecondaryWeaponState,         // WeaponStateEnum32
        ComplianceState                // ComplianceStateEnum32
    }

    public Lifeform() throws RprBuilderException {
        super();
       
        // TODO  set the attributes 
        /*
        try {
            // like this, but there is a newer  mechanism
            setAttributeValue(Attributes.AfterburnerOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.AntiCollisionLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.BlackOutBrakeLightsOn.name(), encoderFactory.createHLAboolean());
            
           
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError
                | EncoderException e) {
            throw new RprBuilderException("error while creating member attributes", e);
        }
        */
        
    }

   // TODO   we need the  getter and setter for all attributes
   
    
    
    
    
}

