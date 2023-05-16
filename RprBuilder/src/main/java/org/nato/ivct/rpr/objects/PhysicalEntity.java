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

    
    
    // TODO: create fields
    
    // added by brf    
    //private PhysicalEntityStruct aFirePowerDisabledStr = null;                 
    //private PhysicalEntityStruct aIsConcealedStr = null;
    //private PhysicalEntityStruct aTentDeployedStr = null ;

    /**
     * attributes moved into generic attributeMap managed by HLAobjectRoot
     * 
     private HLAboolean aEngineSmokeOn = null;
     private HLAboolean aFirePowerDisabled = null;
     private HLAboolean aFlamesPresent= null;
     private HLAboolean aIsConcealed = null;
     private HLAboolean aTentDeployed = null;
     */
    //------------
    

    public PhysicalEntity() throws RprBuilderException {
        super();
        try {
            getEngineSmokeOn();
            getFirePowerDisabled();
            getFlamesPresent();
            getIsConcealed();
            getTentDeployed();
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError
                | EncoderException e) {
            throw new RprBuilderException("error while creating member attributes", e);
        }
    }


    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }

    
    // attribute setter and getter

    public void setEngineSmokeOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        // aEngineSmokeOn.setValue(value);
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.EngineSmokeOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.EngineSmokeOn.name(), holder);
    }
    public boolean  getEngineSmokeOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.EngineSmokeOn.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.EngineSmokeOn.name(), attribute);
        }
        return attribute.getValue();
    }
    
    public void setFirePowerDisabled(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        // aFirePowerDisabled.setValue(value);
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.FirePowerDisabled.name());
        holder.setValue(value);
        setAttributeValue(Attributes.FirePowerDisabled.name(), holder);
    }
    public boolean  getFirePowerDisabled() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FirePowerDisabled.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.FirePowerDisabled.name(), attribute);
        }
        return attribute.getValue();
    }
    
    public void setFlamesPresent(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.FlamesPresent.name());
        holder.setValue(value);
        setAttributeValue(Attributes.FlamesPresent.name(), holder);
    }
    public boolean  getFlamesPresent() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.FlamesPresent.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.FlamesPresent.name(), attribute);
        }
        return attribute.getValue();
    }
    
    public void setIsConcealed(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.IsConcealed.name());
        holder.setValue(value);
        setAttributeValue(Attributes.IsConcealed.name(), holder);
    }
    public boolean  getIsConcealed() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.IsConcealed.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.IsConcealed.name(), attribute);
        }
        return attribute.getValue();
    }
    
    public void setTentDeployed(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.TentDeployed.name());
        holder.setValue(value);
        setAttributeValue(Attributes.TentDeployed.name(), holder);
    }
    public boolean  getTentDeployed() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.TentDeployed.name());
        if (attribute == null) {
            attribute = encoderFactory.createHLAboolean();
            setAttributeValue(Attributes.TentDeployed.name(), attribute);
        }
        return attribute.getValue();
    }
    
    
 
    /*
     *   public void setCamouflageType(boolean tentDeployed) {
        (( whatever   .....Value(tentDeployed);
    }
    public boolean  getCamouflageType() {
        return ((whatever ......getValue();
    }
     
    */
    // -------------  end of added by brf
    
    
    
}

