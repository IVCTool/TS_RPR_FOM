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

import java.util.ArrayList;

import org.nato.ivct.rpr.HLAroot;
import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.datatypes.ArticulatedParameterStruct;
import org.nato.ivct.rpr.datatypes.CamouflageEnum32;
import org.nato.ivct.rpr.datatypes.DamageStatusEnum32;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.TrailingEffectsCodeEnum32;
import org.nato.ivct.rpr.datatypes.ForceIdentifierEnum8;
import org.nato.ivct.rpr.objects.BaseEntity.Attributes;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class PhysicalEntity extends BaseEntity {
	
    public enum Attributes {
        AcousticSignatureIndex,               // Integer16   HLAinteger16BE
        AlternateEntityType,                     // EntityTypeStruct
        ArticulatedParametersArray,         // ArticulatedParameterStructLengthlessArray
        CamouflageType,                         // CamouflageEnum32
        DamageState,                              // <dataType>DamageStatusEnum32</dataType>             
        EngineSmokeOn,                          //    RPRboolean
        FirePowerDisabled,                       //     RPRboolean
        FlamesPresent,                             //     RPRboolean
        ForceIdentifier,                             //<dataType>ForceIdentifierEnum8</dataType>
        HasAmmunitionSupplyCap,           //    RPRboolean
        HasFuelSupplyCap,                      //     RPRboolean
        HasRecoveryCap,                        //     RPRboolean
        HasRepairCap,                            //      RPRboolean
        Immobilized,                               //      RPRboolean
        InfraredSignatureIndex,                //  <dataType>Integer16</dataType>             
        IsConcealed,                               //    RPRboolean
        LiveEntityMeasuredSpeed,           //<dataType>VelocityDecimeterPerSecondInteger16</dataType>
        Marking,                                    //  <dataType>MarkingStruct</dataType>          
        PowerPlantOn,                            //      RPRboolean
        PropulsionSystemsData,                // <dataType>PropulsionSystemDataStructLengthlessArray</dataType>
        RadarCrossSectionSignatureIndex, //  <dataType>Integer16</dataType>
        SmokePlumePresent,                     //    RPRboolean
        TentDeployed,                              //    RPRboolean
        TrailingEffectsCode,                      //<dataType>TrailingEffectsCodeEnum32</dataType>
        VectoringNozzleSystemData         //<dataType>VectoringNozzleSystemDataStructLengthlessArray</dataType>
    }
    
    // try to build a Array for  ArticulatedParametersArray
    // how to create a dataElement    ArticulatedParameterStructLengthlessArray
    ArrayList<ArticulatedParameterStructLengthlessArray> ArticulatedParametersArray = new ArrayList<ArticulatedParameterStructLengthlessArray>();
    ArrayList<ArticulatedParameterStruct> ArticulatedParameterStructLengthlessArray = new ArrayList<ArticulatedParameterStruct>();
    
    
    public PhysicalEntity() throws RprBuilderException {
        super();
        // TODO: create remaining fields     //  further attributes  have different Datatypes
        
        addAttribute(Attributes.AcousticSignatureIndex.name(),encoderFactory.createHLAinteger16BE());
        
        try {
            addAttribute(Attributes.AlternateEntityType.name(), new EntityTypeStruct());
        } catch (RTIinternalError e) {
            throw new RprBuilderException(e.getMessage());
        }

        //ArticulatedParametersArray,
        /*
          RPR-Physical_v2.0.xml:
          <name>ArticulatedParametersArray</name> 
          <dataType>ArticulatedParameterStructLengthlessArray</dataType>
          
         */
        /*
        PRP-Base_v2.0.xml : 
           <name>ArticulatedParameterStructLengthlessArray</name>
           <dataType>ArticulatedParameterStruct</dataType>
        */
        // How to add this Element ?
        //addAttribute(Attributes.ArticulatedParametersArray.name(),     );    
        
        
        addAttribute(Attributes.CamouflageType.name(), CamouflageEnum32.GenericCamouflage.getDataElement());      
        addAttribute(Attributes.DamageState.name(), DamageStatusEnum32.NoDamage.getDataElement() );     // New,  incorrect !  
        addAttribute(Attributes.EngineSmokeOn.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.FirePowerDisabled.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.FlamesPresent.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.ForceIdentifier.name(), ForceIdentifierEnum8.Friendly.getDataElement()   );   // New   ForceIdentifierEnum8        
        addAttribute(Attributes.HasAmmunitionSupplyCap.name(), encoderFactory.createHLAboolean() );        
        addAttribute(Attributes.HasFuelSupplyCap.name(), encoderFactory.createHLAboolean() );      
        addAttribute(Attributes.HasRecoveryCap.name(), encoderFactory.createHLAboolean() );    
        addAttribute(Attributes.HasRepairCap.name(), encoderFactory.createHLAboolean() );               
        addAttribute(Attributes.Immobilized.name(), encoderFactory.createHLAboolean() );         
        addAttribute(Attributes.InfraredSignatureIndex.name(), encoderFactory.createHLAinteger16BE() );  // ??      
        addAttribute(Attributes.IsConcealed.name(), encoderFactory.createHLAboolean()); 

        //LiveEntityMeasuredSpeed,           //<dataType>VelocityDecimeterPerSecondInteger16</dataType>  
        addAttribute(Attributes.LiveEntityMeasuredSpeed.name(), encoderFactory.createHLAinteger16BE()  ); 
        
         //Marking,                                    //  <dataType>MarkingStruct</dataType>        
        addAttribute(Attributes.PowerPlantOn.name(), encoderFactory.createHLAboolean() );        
        //PropulsionSystemsData,                // <dataType>PropulsionSystemDataStructLengthlessArray</dataType>        
        addAttribute(Attributes.RadarCrossSectionSignatureIndex.name(), encoderFactory.createHLAinteger16BE() );  // ??  
        addAttribute(Attributes.SmokePlumePresent.name(), encoderFactory.createHLAboolean() );        
        addAttribute(Attributes.TentDeployed.name(), encoderFactory.createHLAboolean());         
        addAttribute(Attributes.TrailingEffectsCode.name(), TrailingEffectsCodeEnum32.NoTrail.getDataElement()  );   // 
        //VectoringNozzleSystemData         //<dataType>VectoringNozzleSystemDataStructLengthlessArray</dataType>
        addAttribute(Attributes.FlamesPresent.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.IsConcealed.name(), encoderFactory.createHLAboolean());
        addAttribute(Attributes.TentDeployed.name(), encoderFactory.createHLAboolean());     
    }

    public void addSubscribe(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        addSubAttribute(attribute.name());
    }
    public void addPublish(Attributes attribute) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError { 
        addPubAttribute(attribute.name()); 
    }
   
    
    // attribute setter and getter

    public void setAcousticSignatureIndex(short value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
    	HLAinteger16BE holder = (HLAinteger16BE) getAttribute(Attributes.AcousticSignatureIndex.name());
        holder.setValue(value);
        setAttributeValue(Attributes.AcousticSignatureIndex.name(), holder);
    }
    public short getAcousticSignatureIndex() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
    	HLAinteger16BE attribute = (HLAinteger16BE) getAttribute(Attributes.AcousticSignatureIndex.name());
        return (short) attribute.getValue();
    }
        
   public void setAlternateEntityType (EntityTypeStruct value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue(Attributes.AlternateEntityType.name(), value);
    }
    public EntityTypeStruct getAlternateEntityType() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        EntityTypeStruct aEntityType = (EntityTypeStruct) getAttribute(Attributes.AlternateEntityType.name());
        return (aEntityType);
    }
    
    //ArticulatedParametersArray,         // ArticulatedParameterStructLengthlessArray
    
    public void setCamouflageType (CamouflageEnum32 value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        setAttributeValue("CamouflageType", value.getDataElement());
    }
    public CamouflageEnum32 getCamouflageType() throws EncoderException, DecoderException {
        DataElement de = getAttribute("CamouflageType");
        return CamouflageEnum32.decode(de.toByteArray());
    }

    public void setDamageState (DamageStatusEnum32 element  )  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException   {
        setAttributeValue("DamageState",  element.getDataElement()  );   
    }
    public DamageStatusEnum32 getDamageState() throws EncoderException, DecoderException  {
        DataElement de = getAttribute("DamageState");
        return DamageStatusEnum32.decode(de.toByteArray());
    }
    
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
    
    public void setForceIdentifier (ForceIdentifierEnum8  element  )  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException   {
        setAttributeValue("ForceIdentifier",  element.getDataElement()  );   
    }    
    public ForceIdentifierEnum8 getForceIdentifier() throws EncoderException, DecoderException  {
        DataElement de = getAttribute("ForceIdentifier");
        return ForceIdentifierEnum8.decode(de.toByteArray());
    }
    
    public void setHasAmmunitionSupplyCap(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.HasAmmunitionSupplyCap.name());
        holder.setValue(value);
        setAttributeValue(Attributes.HasAmmunitionSupplyCap.name(), holder);
    }
    public boolean  getHasAmmunitionSupplyCap() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HasAmmunitionSupplyCap.name());
        return attribute.getValue();
    } 
    
    public void setHasFuelSupplyCap(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.HasFuelSupplyCap.name());
        holder.setValue(value);
        setAttributeValue(Attributes.HasFuelSupplyCap.name(), holder);
    }
    public boolean  getHasFuelSupplyCap() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HasFuelSupplyCap.name());
        return attribute.getValue();
    }        
   
    public void setHasRecoveryCap(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.HasRecoveryCap.name());
        holder.setValue(value);
        setAttributeValue(Attributes.HasRecoveryCap.name(), holder);
    }
    public boolean  getHasRecoveryCap() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HasRecoveryCap.name());
        return attribute.getValue();
    }        
   
    public void setHasRepairCap(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.HasRepairCap.name());
        holder.setValue(value);
        setAttributeValue(Attributes.HasRepairCap.name(), holder);
    }
    public boolean  getHasRepairCap() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.HasRepairCap.name());
        return attribute.getValue();
    }        
 
    public void setImmobilized(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.Immobilized.name());
        holder.setValue(value);
        setAttributeValue(Attributes.Immobilized.name(), holder);
    }
    public boolean  getImmobilized() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.Immobilized.name());
        return attribute.getValue();
    }

    public void setInfraredSignatureIndex(short value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAinteger16BE holder = (HLAinteger16BE) getAttribute(Attributes.InfraredSignatureIndex.name());
        holder.setValue(value);
        setAttributeValue(Attributes.InfraredSignatureIndex.name(), holder);
    }
    public short getInfraredSignatureIndex() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAinteger16BE attribute = (HLAinteger16BE) getAttribute(Attributes.InfraredSignatureIndex.name());
        return (short) attribute.getValue();
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
    
    public void setLiveEntityMeasuredSpeed(short value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAinteger16BE   holder = (HLAinteger16BE) getAttribute(Attributes.LiveEntityMeasuredSpeed.name());
        holder.setValue(value);
        setAttributeValue(Attributes.LiveEntityMeasuredSpeed.name(), holder);
    }
    public short getLiveEntityMeasuredSpeed() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAinteger16BE attribute = (HLAinteger16BE) getAttribute(Attributes.LiveEntityMeasuredSpeed.name());
        return (short) attribute.getValue();
    }
        
    //Marking,                                    //  <dataType>MarkingStruct</dataType>      
    
    public void setPowerPlantOn(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.PowerPlantOn.name());
        holder.setValue(value);
        setAttributeValue(Attributes.PowerPlantOn.name(), holder);
    }
    public boolean  getPowerPlantOn() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.PowerPlantOn.name());
        return attribute.getValue();
    }
    
    //PropulsionSystemsData,                // <dataType>PropulsionSystemDataStructLengthlessArray</dataType>
    
    public void setRadarCrossSectionSignatureIndex(short value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAinteger16BE holder = (HLAinteger16BE) getAttribute(Attributes.RadarCrossSectionSignatureIndex.name());
        holder.setValue(value);
        setAttributeValue(Attributes.RadarCrossSectionSignatureIndex.name(), holder);
    }
    public short getRadarCrossSectionSignatureIndex() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAinteger16BE attribute = (HLAinteger16BE) getAttribute(Attributes.RadarCrossSectionSignatureIndex.name());
        return (short) attribute.getValue();
    }    

    public void setSmokePlumePresent(boolean value) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean holder = (HLAboolean) getAttribute(Attributes.SmokePlumePresent.name());
        holder.setValue(value);
        setAttributeValue(Attributes.SmokePlumePresent.name(), holder);
    }
    public boolean  getSmokePlumePresent() throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException {
        HLAboolean attribute = (HLAboolean) getAttribute(Attributes.SmokePlumePresent.name());
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

    public void setTrailingEffectsCode (TrailingEffectsCodeEnum32 element)  throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException   {
        setAttributeValue("TrailingEffectsCode",  element.getDataElement()  );   
    }
    public TrailingEffectsCodeEnum32 getTrailingEffectsCode() throws EncoderException, DecoderException  {
        DataElement de = getAttribute("TrailingEffectsCode");
        return TrailingEffectsCodeEnum32.decode(de.toByteArray());
    }
     
    //VectoringNozzleSystemData         //<dataType>VectoringNozzleSystemDataStructLengthlessArray</dataType>
    
}

