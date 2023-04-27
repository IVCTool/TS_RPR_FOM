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
        try {
            setAttributeValue(Attributes.AfterburnerOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.AntiCollisionLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.BlackOutBrakeLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.BlackOutLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.BrakeLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.FormationLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.HatchState.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.HeadLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.InteriorLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.LandingLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.LauncherRaised.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.NavigationLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.RampDeployed.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.RunningLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.SpotLightsOn.name(), encoderFactory.createHLAboolean());
            setAttributeValue(Attributes.TailLightsOn.name(), encoderFactory.createHLAboolean());
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError
                | EncoderException e) {
            throw new RprBuilderException("error while creating member attributes", e);
        }
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

    // TODO: add remaining sub/pub helpers

    // attribute setter and getter
    
    public void setAfterburnerOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getAfterburnerOn();
        holder.setValue(value);
        setAttributeValue(Attributes.AfterburnerOn.name(), holder);
    }
    public HLAboolean getAfterburnerOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.AfterburnerOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.AfterburnerOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setAntiCollisionLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getAfterburnerOn();
        holder.setValue(value);
        setAttributeValue(Attributes.AntiCollisionLightsOn.name(), holder);
    }
    public HLAboolean getAntiCollisionLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.AntiCollisionLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.AntiCollisionLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setBlackOutBrakeLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getBlackOutBrakeLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.BlackOutBrakeLightsOn.name(), holder);
    }
    public HLAboolean getBlackOutBrakeLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BlackOutBrakeLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.BlackOutBrakeLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setBlackOutLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getBlackOutLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.BlackOutLightsOn.name(), holder);
    }
    public HLAboolean getBlackOutLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BlackOutLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.BlackOutLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setBrakeLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getBrakeLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.BrakeLightsOn.name(), holder);
    }
    public HLAboolean getBrakeLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.BrakeLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.BrakeLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setFormationLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getFormationLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.FormationLightsOn.name(), holder);
    }
    public HLAboolean getFormationLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FormationLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.FormationLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setHatchState (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getHatchState();
        holder.setValue(value);
        setAttributeValue(Attributes.HatchState.name(), holder);
    }
    public HLAboolean getHatchState () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HatchState.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.HatchState.name(), attribute);
        }
        return attribute;
    }
    
    public void setHeadLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getHeadLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.HeadLightsOn.name(), holder);
    }
    public HLAboolean getHeadLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HeadLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.HeadLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setInteriorLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getInteriorLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.InteriorLightsOn.name(), holder);
    }
    public HLAboolean getInteriorLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.InteriorLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.InteriorLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setLandingLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getLandingLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.LandingLightsOn.name(), holder);
    }
    public HLAboolean getLandingLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.LandingLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.LandingLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setLauncherRaised (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getLauncherRaised();
        holder.setValue(value);
        setAttributeValue(Attributes.LauncherRaised.name(), holder);
    }
    public HLAboolean getLauncherRaised () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.LauncherRaised.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.LauncherRaised.name(), attribute);
        }
        return attribute;
    }
    
    public void setNavigationLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getNavigationLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.NavigationLightsOn.name(), holder);
    }
    public HLAboolean getNavigationLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.NavigationLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.NavigationLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setRampDeployed (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getRampDeployed();
        holder.setValue(value);
        setAttributeValue(Attributes.RampDeployed.name(), holder);
    }
    public HLAboolean getRampDeployed () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.RampDeployed.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.RampDeployed.name(), attribute);
        }
        return attribute;
    }
    
    public void setRunningLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getRunningLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.RunningLightsOn.name(), holder);
    }
    public HLAboolean getRunningLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.RunningLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.RunningLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setSpotLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getSpotLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.SpotLightsOn.name(), holder);
    }
    public HLAboolean getSpotLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.SpotLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.SpotLightsOn.name(), attribute);
        }
        return attribute;
    }
    
    public void setTailLightsOn (Boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = getTailLightsOn();
        holder.setValue(value);
        setAttributeValue(Attributes.TailLightsOn.name(), holder);
    }
    public HLAboolean getTailLightsOn () throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.TailLightsOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.TailLightsOn.name(), attribute);
        }
        return attribute;
    }
}

