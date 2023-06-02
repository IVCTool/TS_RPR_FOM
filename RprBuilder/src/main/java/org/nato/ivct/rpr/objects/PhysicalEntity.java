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

public class PhysicalEntity extends BaseEntity {
	
    public enum Attributes {
        AcousticSignatureIndex,
        AlternateEntityType,
        ArticulatedParametersArray,
        CamouflageType,
        DamageState,
        EngineSmokeOn,
        FirePowerDisabled,
        FlamesPresent,
        ForceIdentifier,
        HasAmmunitionSupplyCap,
        HasFuelSupplyCap,
        HasRecoveryCap,
        HasRepairCap,
        Immobilized,
        InfraredSignatureIndex,
        IsConcealed,
        LiveEntityMeasuredSpeed,
        Marking,
        PowerPlantOn,
        PropulsionSystemsData,
        RadarCrossSectionSignatureIndex,
        SmokePlumePresent,
        TentDeployed,
        TrailingEffectsCode,
        VectoringNozzleSystemData        
    }
      
    
    public PhysicalEntity() throws RprBuilderException {
        super();
        addAttribute(Attributes.EngineSmokeOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.FirePowerDisabled.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.FlamesPresent.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.IsConcealed.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.TentDeployed.name(), encoderFactory.createHLAboolean());
        // TODO: create remaining fields
    }


    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }
   
    
    // attribute setter and getter

    public void setEngineSmokeOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.EngineSmokeOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.EngineSmokeOn.name(), holder);
    }
    public boolean  getEngineSmokeOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.EngineSmokeOn.name());
        return attribute.getValue();
    }
    
    public void setFirePowerDisabled(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.FirePowerDisabled.name());
        holder.setValue(value);
        setAttributeValue(Attributes.FirePowerDisabled.name(), holder);
    }
    public boolean  getFirePowerDisabled() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FirePowerDisabled.name());
        return attribute.getValue();
    }
    
    public void setFlamesPresent(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.FlamesPresent.name());
        holder.setValue(value);
        setAttributeValue(Attributes.FlamesPresent.name(), holder);
    }
    public boolean  getFlamesPresent() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FlamesPresent.name());
        return attribute.getValue();
    }
    
    public void setIsConcealed(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.IsConcealed.name());
        holder.setValue(value);
        setAttributeValue(Attributes.IsConcealed.name(), holder);
    }
    public boolean  getIsConcealed() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.IsConcealed.name());
        return attribute.getValue();
    }
    
    public void setTentDeployed(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.TentDeployed.name());
        holder.setValue(value);
        setAttributeValue(Attributes.TentDeployed.name(), holder);
    }
    public boolean  getTentDeployed() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.TentDeployed.name());
        return attribute.getValue();
    }  
}

