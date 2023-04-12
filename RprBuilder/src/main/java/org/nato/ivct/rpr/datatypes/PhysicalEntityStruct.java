/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.rpr.datatypes;

import org.nato.ivct.rpr.datatypes.SpatialFPStruct.AttributeName;
import org.nato.ivct.rpr.objects.BaseEntity.Attributes;

import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

/** 
 * Spatial structure for Dead Reckoning Algorithm RPW (3) and RPB (7). 
 */
public class PhysicalEntityStruct extends HLAfixedRecordStruct {
	
	/*  
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
	 *   */

    public enum AttributeName {
    	CamouflageType,
    	FirePowerDisabled,
    	IsConcealed,
        TentDeployed,
    }

    public PhysicalEntityStruct() throws RTIinternalError {
        super();
        add(AttributeName.FirePowerDisabled.name(), encoderFactory.createHLAboolean());
        add(AttributeName.IsConcealed.name(), encoderFactory.createHLAboolean());
        add(AttributeName.TentDeployed.name(), encoderFactory.createHLAboolean()); 
    }
    
    // ----------------------------------
    public boolean  getFirePowerDisabled() {
        return ((HLAboolean ) get(AttributeName.FirePowerDisabled.name())).getValue();
    }    
    public void setFirePowerDisabled(boolean firePowerDisabled) {
        ((HLAboolean) get(AttributeName.FirePowerDisabled.name())).setValue(firePowerDisabled);
    }
       
    public boolean  getIsConcealed() {
        return ((HLAboolean ) get(AttributeName.IsConcealed.name())).getValue();
    }    
    public void setIsConcealed(boolean isConcealed) {
        ((HLAboolean) get(AttributeName.IsConcealed.name())).setValue(isConcealed);
    }
    
    public boolean  getTentDeployed() {
        return ((HLAboolean ) get(AttributeName.TentDeployed.name())).getValue();
    }    
    public void setTentDeployed(boolean tentDeployed) {
        ((HLAboolean) get(AttributeName.TentDeployed.name())).setValue(tentDeployed);
    }
 
    /*
    public boolean  getCamouflageType() {
        return ((HLAboolean ) get(AttributeName.TentDeployed.name())).getValue();
    }
    
    public void setCamouflageType(boolean tentDeployed) {
        ((HLAboolean) get(AttributeName.TentDeployed.name())).setValue(tentDeployed);
    }
    */
    
    
}
