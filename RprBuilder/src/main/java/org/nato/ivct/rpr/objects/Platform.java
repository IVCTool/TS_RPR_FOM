/**
 * Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

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

public class Platform extends PhysicalEntity{
    
    public enum Attributes {
        AfterburnerOn,
        AntiCollisionLightsOn,
        BlackOutBrakeLightsOn,
        BlackOutLightsOn,
        BrakeLightsOn,
        FormationLightsOn,
        HatchState,
        HeadLightsOn,
        InteriorLightsOn,
        LandingLightsOn,
        LauncherRaised,
        NavigationLightsOn,
        RampDeployed,
        RunningLightsOn,
        SpotLightsOn,
        TailLightsOn
    }

    public Platform() throws RprBuilderException {
        super();
        addAttribute(Attributes.AfterburnerOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.AntiCollisionLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.BlackOutBrakeLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.BlackOutLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.BrakeLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.FormationLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.HatchState.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.HeadLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.InteriorLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.LandingLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.LauncherRaised.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.NavigationLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.RampDeployed.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.RunningLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.SpotLightsOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.TailLightsOn.name(), encoderFactory.createHLAboolean());
    }

    /**
     * TODO: Discuss if class based subscription methods shall be used 
     */
    private static Platform anchor;
    public static void addPub(Attributes attribute) throws Exception {
        if (anchor == null) anchor = new Platform();
        anchor.addPubAttribute(attribute.name());
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

    /*
     * Type Safe pub/sub Methods
     */
    public void subscribeAfterburnerOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(Attributes.AfterburnerOn.name());
    }
    public void publishAfterburnerOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addPubAttribute(Attributes.AfterburnerOn.name());
    }


    // attribute setter and getter
    
    public void setAfterburnerOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.AfterburnerOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.AfterburnerOn.name(), holder);
    }
    public boolean  getAfterburnerOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.AfterburnerOn.name());
        return attribute.getValue();
    }
    
    public void setAntiCollisionLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.AfterburnerOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.AntiCollisionLightsOn.name(), holder);
    }
    public boolean  getAntiCollisionLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.AntiCollisionLightsOn.name());
        return attribute.getValue();
    }
    
    public void setBlackOutBrakeLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.BlackOutBrakeLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.BlackOutBrakeLightsOn.name(), holder);
    }
    public boolean  getBlackOutBrakeLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BlackOutBrakeLightsOn.name());
        return attribute.getValue();
    }
    
    public void setBlackOutLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.BlackOutLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.BlackOutLightsOn.name(), holder);
    }
    public boolean  getBlackOutLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BlackOutLightsOn.name());
        return attribute.getValue();
    }
    
    public void setBrakeLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.BrakeLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.BrakeLightsOn.name(), holder);
    }
    public boolean  getBrakeLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BrakeLightsOn.name());
        return attribute.getValue();
    }
    
    public void setFormationLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.FormationLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.FormationLightsOn.name(), holder);
    }
    public boolean  getFormationLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FormationLightsOn.name());
        return attribute.getValue();
    }
    
    public void setHatchState (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.HatchState.name());
        holder.setValue(value);
        setAttributeValue(Attributes.HatchState.name(), holder);
    }
    public boolean  getHatchState () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HatchState.name());
        return attribute.getValue();
    }
    
    public void setHeadLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.HeadLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.HeadLightsOn.name(), holder);
    }
    public boolean  getHeadLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HeadLightsOn.name());
        return attribute.getValue();
    }
    
    public void setInteriorLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.InteriorLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.InteriorLightsOn.name(), holder);
    }
    public boolean  getInteriorLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.InteriorLightsOn.name());
        return attribute.getValue();
    }
    
    public void setLandingLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.LandingLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.LandingLightsOn.name(), holder);
    }
    public boolean  getLandingLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.LandingLightsOn.name());
        return attribute.getValue();
    }
    
    public void setLauncherRaised (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.LauncherRaised.name());
        holder.setValue(value);
        setAttributeValue(Attributes.LauncherRaised.name(), holder);
    }
    public boolean  getLauncherRaised () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.LauncherRaised.name());
        return attribute.getValue();
    }
    
    public void setNavigationLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.NavigationLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.NavigationLightsOn.name(), holder);
    }
    public boolean  getNavigationLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.NavigationLightsOn.name());
        return attribute.getValue();
    }
    
    public void setRampDeployed (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.RampDeployed.name());
        holder.setValue(value);
        setAttributeValue(Attributes.RampDeployed.name(), holder);
    }
    public boolean  getRampDeployed () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.RampDeployed.name());
        return attribute.getValue();
    }
    
    public void setRunningLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.RunningLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.RunningLightsOn.name(), holder);
    }
    public boolean  getRunningLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.RunningLightsOn.name());
        return attribute.getValue();
    }
    
    public void setSpotLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.SpotLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.SpotLightsOn.name(), holder);
    }
    public boolean  getSpotLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.SpotLightsOn.name());
        return attribute.getValue();
    }
    
    public void setTailLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.TailLightsOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.TailLightsOn.name(), holder);
    }
    public boolean  getTailLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.TailLightsOn.name());
        return attribute.getValue();
    }
}

