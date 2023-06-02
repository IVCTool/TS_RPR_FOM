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
        addAttribute(Attributes.AntennaRaised.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.BlackoutLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.LightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.InteriorLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.MissionKill.name(), encoderFactory.createHLAboolean());
    }
    
   
    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }
    

    //  getter and setter for all the attributes
    
    public void setAntennaRaised(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.AntennaRaised.name());
        holder.setValue(value);
        setAttributeValue(Attributes.AntennaRaised.name(), holder);
    }
    public boolean  getAntennaRaised() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.AntennaRaised.name());
        return attribute.getValue();
    }    
    

    public void setBlackoutLightsOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.BlackoutLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.BlackoutLightsOn.name(), holder);
    }
    public boolean  getBlackoutLightsOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BlackoutLightsOn.name());
        return attribute.getValue();
    }
    

    public void setLightsOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.LightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.LightsOn.name(), holder);
    }
    public boolean  getLightsOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.LightsOn.name());
        return attribute.getValue();
    }
    
  
    public void setInteriorLightsOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.InteriorLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.InteriorLightsOn.name(), holder);
    }
    public boolean  getInteriorLightsOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.InteriorLightsOn.name());
        return attribute.getValue();
    }
    
    
    public void setMissionKill(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.MissionKill.name());
        holder.setValue(value);
        setAttributeValue(Attributes.MissionKill.name(), holder);
    }
    public boolean  getMissionKill() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.MissionKill.name());
        return attribute.getValue();
    }
    
    
    
}
