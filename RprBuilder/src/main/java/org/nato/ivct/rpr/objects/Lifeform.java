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
import org.nato.ivct.rpr.objects.Sensor.Attributes;

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

        // TODO set the attributes
        // like this, but there may be a newer mechanism
        try {
            //setAttributeValue(Attributes.FlashLightsOn.name(), encoderFactory.createHLAboolean());
            // ...
            getFlashLightsOn();
            
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError
                | EncoderException e) {
            throw new RprBuilderException("error while creating member attributes", e);
        }
    }
    
    
    // public void addSubscribe    is inherited from physicalEntity  ???
    //  TODO  do we need this here ?
    
    public void addSubscribe(Attributes attribute)
            throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }

    public void addPublish(Attributes attribute)
            throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addPubAttribute(attribute.name());
    }

    
   // TODO   we need the  getter and setter for all attributes
    
    public void setFlashLightsOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.FlashLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.FlashLightsOn.name(), holder);
    }
    public boolean  getFlashLightsOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FlashLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.FlashLightsOn.name(), attribute);
        }
        return attribute.getValue();
    }
    
    
   
    
}

