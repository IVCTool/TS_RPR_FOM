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
import org.nato.ivct.rpr.objects.PhysicalEntity.Attributes;

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class CulturalFeature extends PhysicalEntity {

    public enum Attributes {
        ExternalLightsOn,
        InternalHeatSourceOn,
        InternalLightsOn
    }

    
    public CulturalFeature() throws RprBuilderException {
        super();
        
        try {
            getExternalLightsOn();
            getInternalHeatSourceOn();
            getInternalLightsOn();
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError
                | EncoderException e) {
            throw new RprBuilderException("error while creating member attributes", e);
        }
        
    }
    
    // public void addSubscribe    is inherited from physicalEntity  ?
    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }
    
    // attribute setter and getter
    
    public void setExternalLightsOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.ExternalLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.ExternalLightsOn.name(), holder);
    }    
    public boolean  getExternalLightsOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.ExternalLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.ExternalLightsOn.name(), attribute);
        }
        return attribute.getValue();
    }


    public void setInternalHeatSourceOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.InternalHeatSourceOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.InternalHeatSourceOn.name(), holder);
    }    
    public boolean  getInternalHeatSourceOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.InternalHeatSourceOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.InternalHeatSourceOn.name(), attribute);
        }
        return attribute.getValue();
    }
    
    public void setInternalLightsOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.InternalLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.InternalLightsOn.name(), holder);
    }    
    public boolean  getInternalLightsOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.InternalLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.InternalLightsOn.name(), attribute);
        }
        return attribute.getValue();
    }
    
     
}
