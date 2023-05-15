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

import java.util.Map.Entry;

import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;
import org.nato.ivct.rpr.datatypes.SpatialRVStruct.AttributeName;
import org.nato.ivct.rpr.objects.BaseEntity.Attributes;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
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
    private HLAboolean aEngineSmokeOn = null;
    private HLAboolean aFirePowerDisabled = null;
    private HLAboolean aFlamesPresent= null;
    private HLAboolean aIsConcealed = null;
    private HLAboolean aTentDeployed = null;
    //------------
    
    //modified by brf
    public PhysicalEntity() throws RprBuilderException {
        super();
        aEngineSmokeOn = encoderFactory.createHLAboolean();
        aFirePowerDisabled = encoderFactory.createHLAboolean();
        aFlamesPresent = encoderFactory.createHLAboolean();
        aIsConcealed = encoderFactory.createHLAboolean();
        aTentDeployed = encoderFactory.createHLAboolean();
    }
    
    

    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }

    
    // added by brf
    public void setEngineSmokeOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aEngineSmokeOn.setValue(value);
        setAttributeValue(Attributes.EngineSmokeOn.name(), aEngineSmokeOn);
    }
    public boolean  getEngineSmokeOn() {
        return aEngineSmokeOn.getValue();
    }
    
    public void setFirePowerDisabled(boolean value)  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aFirePowerDisabled.setValue(value);
        setAttributeValue(Attributes.FirePowerDisabled.name(), aFirePowerDisabled );
        
    }
    public boolean  getFirePowerDisabled()  {
        return aFirePowerDisabled.getValue();
    }
    
    public void setFlamesPresent(boolean value)  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aFirePowerDisabled.setValue(value);
        setAttributeValue(Attributes.FlamesPresent.name(), aFlamesPresent);
    }
    public boolean  getFlamesPresent() {
        return aFirePowerDisabled.getValue();
    }
    
    public void setIsConcealed(boolean isConcealed)  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aIsConcealed.setValue(isConcealed);
        setAttributeValue(Attributes.IsConcealed.name(), aIsConcealed);
    }
    public boolean  getIsConcealed() {
        return aIsConcealed.getValue();
    }    
    
    public void setTentDeployed(boolean tentDeployed)  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        aTentDeployed.setValue(tentDeployed);
        setAttributeValue(Attributes.TentDeployed.name(), aTentDeployed);
     }
    public boolean  getTentDeployed() {
        return aTentDeployed.getValue();
        
    }    
    
}

