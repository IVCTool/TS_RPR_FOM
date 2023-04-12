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
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.PhysicalEntityStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;
import org.nato.ivct.rpr.datatypes.SpatialRVStruct.AttributeName;
import org.nato.ivct.rpr.objects.BaseEntity.Attributes;

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
    
    // new by brf    
    private PhysicalEntityStruct aFirePowerDisabledStr = null;                 
    private PhysicalEntityStruct aIsConcealedStr = null;
    private PhysicalEntityStruct aTentDeployedStr = null ;
    
    
 
    public PhysicalEntity() throws RprBuilderException {
        super();
    }

    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }

   
   
    // to add  the Struct buildung out of  AircraftApp should be here ######
    
    public PhysicalEntityStruct getFirePowerDisabledStr()throws RTIinternalError {
    	if (aFirePowerDisabledStr == null) {
    		aFirePowerDisabledStr =  new PhysicalEntityStruct();    		
    	}
    	return aFirePowerDisabledStr;
    }  
    public void activateFirePowerD(PhysicalEntityStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
    	aFirePowerDisabledStr = value;
        setAttributeValue(Attributes.FirePowerDisabled.name(), value.getDataElement());
    }
    
    public PhysicalEntityStruct getIsconcealedStr()throws RTIinternalError {
    	if (aIsConcealedStr == null) {
    		aIsConcealedStr =  new PhysicalEntityStruct();    		
    	}
    	return aIsConcealedStr;
    }
    public void activateIsConcealed(PhysicalEntityStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
    	aIsConcealedStr = value;
    	setAttributeValue(Attributes.IsConcealed.name(), value.getDataElement());
    }    
    
    
    public PhysicalEntityStruct getTentDeployedStr()throws RTIinternalError {
    	if (aTentDeployedStr == null) {
    		aTentDeployedStr =  new PhysicalEntityStruct();    		
    	}
    	return aTentDeployedStr;
    }    
    public void activateTendDeployed(PhysicalEntityStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
    	aFirePowerDisabledStr = value;
        setAttributeValue(Attributes.TentDeployed.name(), value.getDataElement());
    }    

    
    
    
    
    // end of added by brf
    
    
    
    
    // TODO: add remaining sub/pub helpers

    // TODO: add attribute setter and getter
    

    
    // isConcealed
    
    // TentDeployd
    
    
    //CamouflageType ....
    
    
    
    
}

