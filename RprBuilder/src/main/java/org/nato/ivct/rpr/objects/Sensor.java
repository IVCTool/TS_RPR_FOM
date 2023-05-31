/**
 * Copyright 2022, brf (Fraunhofer IOSB)

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
import org.nato.ivct.rpr.objects.PhysicalEntity.Attributes;

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class Sensor extends PhysicalEntity {
    
    public enum Attributes {
    AntennaRaised,
    BlackoutLightsOn,
    LightsOn,
    InteriorLightsOn,
    MissionKill    
    }
      
    public Sensor() throws RprBuilderException {
        super();
          
        try {
            // TODO replace setAttributValue here with  getMethod  like in Physicalentity
            setAttributeValue(Attributes.AntennaRaised.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.BlackoutLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.LightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.LightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.MissionKill.name(), encoderFactory.createHLAboolean());
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected
                | RTIinternalError e) {
            throw new RprBuilderException("error while creating member attributes", e);
        }
    }
    
    
    /**
     * TODO: Discuss if the pub/sub methods shall be made type safe. In that case the following two methods shall be private.
     */
    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }
    
    
    
    // TODO   we need getter and setter for all the attributes
    
    public void setAntennaRaised(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.AntennaRaised.name());
        holder.setValue(value);
        setAttributeValue(Attributes.AntennaRaised.name(), holder);
    }
    public boolean  getAntennaRaised() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.AntennaRaised.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.AntennaRaised.name(), attribute);
        }
        return attribute.getValue();
    }
    
    
    
    
    
    
}
