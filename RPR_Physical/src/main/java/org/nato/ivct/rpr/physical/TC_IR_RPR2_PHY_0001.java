/**    Copyright 2023, brf (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */


package org.nato.ivct.rpr.physical;

import java.net.URL;

import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.HLAobjectRoot;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.slf4j.Logger;
import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

/**
 * IR-RPR-PHY-0001:
 * 
 *  All of the PhysicalEntity attributes shall be treated as optional fields
 *  for federates updating instance attributes of this object class or its subclasses.
 *  
 *  remark of VC
 *  "Need to investigate how this could be tested. One option is to create/publish all defined attributes to stress the SuT"
 *  
 *  
 *   But what is the meaning of  "all defined Attributes"    all attributes defined in   RPR-Physical_v2.0  ?
 *   
 * PhysicalEntity Attributes:  SISO-STD-001  P. 44  Table 6  25 attributes   all Optional
 *  Attribute Name                     Default Value             DataType
 *  AcousticSignatureIndex             0                        Integer16
 *  AlternateEntityType                BaseEntity.EntityType    EntityTypeStruct
 *  ArticulatedParametersArray         Empty                    ArticulatedParameterStructLengthlessArray
 *  CamouflageType                     Uniform Paint Scheme     CamouflageEnum32    
 *  DamageState                        No Damage
 *  EngineSmokeOn                      False
 *  FirePowerDisabled                  False
 *  FlamesPresent                      False
 *  ForceIdentifier                    other
 *  HasAmmunitionSupplyCap             False
 *  HasFuelSupplyCap                   False
 *  HasRecoveryCap                     False
 *  HasRepairCap                       False
 *  Immobilized                        False
 *  InfraredSignatureIndex             0
 *  IsConcealed                        False
 *  LiveEntityMeasuredSpeed            0
 *  Marking                            Empty
 *  PowerPlantOn                       False
 *  PropulsionSystemsData              Empty
 *  RadarCrossSectionSignatureIndex    0
 *  SmokePlumePresent                  False                      
 *  TentDeployed                       False
 *  TrailingEffectsCode                False
 *  VectoringNozzleSystemData        Empty
 *  
 *  so first we have to add all attributes to the subscribedAttribute-Hash of HLAobjectRoot
 *   this ist done  by addSubscribe of PhysicalEntity
 *  
 *  and subscribe to all possible entities to the RTI, whith subsrcibe (HLAobjectRoot)
 *   
 * 
 */

public class TC_IR_RPR2_PHY_0001 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    PhysicalEntity phyEntity;
    
    class TestCaseAmbassador extends NullFederateAmbassador {

        @Override
        public void discoverObjectInstance(
                         ObjectInstanceHandle theObjectInstanceH,
                         ObjectClassHandle theObjectClassH,
                         String objectName) throws FederateInternalError {
            logger.trace("discoverObjectInstance {}",  theObjectInstanceH);            
            logger.info("### discoverObjectInstance without FederateHandle ");            
           
            try {
                // Tests and Debug
                logger.debug("# discoverObjectInstance: reveived ObjectInstanceHandle:  " + theObjectInstanceH ) ; // Debug                
                String receivedObjectInstanceHandleName = rtiAmbassador.getObjectInstanceName(theObjectInstanceH) ;
                logger.debug("# discoverObjectInstance: reveived ObjectInstanceHandle with Name:  " + receivedObjectInstanceHandleName);  // Debug                
                String receivedClassName =  rtiAmbassador.getObjectClassName(theObjectClassH);
                logger.debug("# discoverObjectInstance: reveived ObjectClassHandle with rti-ObjectClassName:  "  + receivedClassName +"\n");  // Debug  
            } catch (ObjectInstanceNotKnown | FederateNotExecutionMember | NotConnected | RTIinternalError | InvalidObjectClassHandle e) {
                logger.error("discoverObjectInstance received Exception", e);
            }
		}

		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject,
				ObjectClassHandle theObjectClass,
				String objectName,
				FederateHandle producingFederate) throws FederateInternalError {
			logger.trace("discoverObjectInstance {} with producingFederate {}", theObject, producingFederate);
			 logger.info("# discoverObjectInstance with FederateHandle ");  
			discoverObjectInstance(theObject, theObjectClass, objectName);
		}
		
		
		@Override		
        public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
                byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
                LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
            logger.trace("reflectAttributeValues with retractionHandle");
            reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
        }
		
		@Override
        public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
        		byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
                LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo)
                throws FederateInternalError {
            logger.trace("reflectAttributeValues with reflectInfo");
            reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
        }		
		
		@Override
        public void reflectAttributeValues(ObjectInstanceHandle theObjectInstanceH, 
                AttributeHandleValueMap attributeHandleVM,  byte[] userSuppliedTag,  OrderType sentOrdering, 
                TransportationTypeHandle theTransport,  SupplementalReflectInfo reflectInfo)
                 throws FederateInternalError {		
            // logger.trace("reflectAttributeValues without LogicalTime,  MessageRetractionHandle ");
            logger.debug("### reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
            logger.debug("# reflectAttributeValues: got  ObjectInstanceHandle  \"theObjectInstanceH\" : " + theObjectInstanceH
                    + " with AttributHandleValueMap :   " + attributeHandleVM); // Debug
        }

    }
    
	@Override
	protected void logTestPurpose(Logger logger) {
		logger.info("Test Case Purpose: \n"
			+ "    All of the PhysicalEntity attributes shall be treated as optional fields\n"
			+ "    for federates updating instance attributes of this object class or its subclasses."); 
		this.logger = logger;
	}

	@Override
	protected void preambleAction(Logger logger) throws TcInconclusiveIf {
        RtiFactory rtiFactory;
        logger.info("preamble action for test {}", this.getClass().getName());
		try {
			rtiFactory = RtiFactoryFactory.getRtiFactory();
			rtiAmbassador = rtiFactory.getRtiAmbassador();
			tcAmbassador = new TestCaseAmbassador();
			URL[] fomList = new FomFiles()
            .addRPR_BASE()
            .addRPR_Enumerations()
            .addRPR_Foundation()
            .addRPR_Physical()
            .addRPR_Switches()
            .get();
			
			rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(federationName, fomList);
			} catch (FederationExecutionAlreadyExists ignored) { }
			rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName, fomList);
		} catch (RTIinternalError | ConnectionFailed | InvalidLocalSettingsDesignator | UnsupportedCallbackModel 
				| AlreadyConnected | CallNotAllowedFromWithinCallback | CouldNotCreateLogicalTimeFactory 
				| FederationExecutionDoesNotExist | InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD 
				| SaveInProgress | RestoreInProgress | FederateAlreadyExecutionMember | NotConnected e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
	}

	@Override
	protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
        logger.info("perform test {}", this.getClass().getName());
        
        HLAobjectRoot.initialize(rtiAmbassador);
        
     try {
         // only to test if we get Informations obout Objects and Attributes
         phyEntity = new PhysicalEntity();
         phyEntity.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);      
         phyEntity.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);         
         phyEntity.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
         phyEntity.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
         phyEntity.addSubscribe(PhysicalEntity.Attributes.TentDeployed);         
         phyEntity.subscribe();
         
         
         Aircraft aircraft = new Aircraft();
         // for testing  we have to send all attributes from physical entity   
         aircraft.addPublish(PhysicalEntity.Attributes.AcousticSignatureIndex);
         aircraft.addPublish(PhysicalEntity.Attributes.AlternateEntityType);
         aircraft.addPublish(PhysicalEntity.Attributes.ArticulatedParametersArray);
         aircraft.addPublish(PhysicalEntity.Attributes.CamouflageType);
         aircraft.addPublish(PhysicalEntity.Attributes.DamageState);

         aircraft.addPublish(PhysicalEntity.Attributes.EngineSmokeOn);
         aircraft.addPublish(PhysicalEntity.Attributes.FirePowerDisabled);
         aircraft.addPublish(PhysicalEntity.Attributes.FlamesPresent);
         aircraft.addPublish(PhysicalEntity.Attributes.ForceIdentifier);
         aircraft.addPublish(PhysicalEntity.Attributes.HasAmmunitionSupplyCap);

         aircraft.addPublish(PhysicalEntity.Attributes.HasFuelSupplyCap);
         aircraft.addPublish(PhysicalEntity.Attributes.HasRecoveryCap);
         aircraft.addPublish(PhysicalEntity.Attributes.HasRepairCap);
         aircraft.addPublish(PhysicalEntity.Attributes.Immobilized);
         aircraft.addPublish(PhysicalEntity.Attributes.InfraredSignatureIndex);

         aircraft.addPublish(PhysicalEntity.Attributes.IsConcealed);
         aircraft.addPublish(PhysicalEntity.Attributes.LiveEntityMeasuredSpeed);
         aircraft.addPublish(PhysicalEntity.Attributes.Marking);
         aircraft.addPublish(PhysicalEntity.Attributes.PowerPlantOn);
         aircraft.addPublish(PhysicalEntity.Attributes.PropulsionSystemsData);

         aircraft.addPublish(PhysicalEntity.Attributes.RadarCrossSectionSignatureIndex);
         aircraft.addPublish(PhysicalEntity.Attributes.SmokePlumePresent);
         aircraft.addPublish(PhysicalEntity.Attributes.TentDeployed);
         aircraft.addPublish(PhysicalEntity.Attributes.TrailingEffectsCode);
         aircraft.addPublish(PhysicalEntity.Attributes.VectoringNozzleSystemData);
         aircraft.register();    
 
        for (int i = 0; i < 5; i++) {  // Testing for 10 Sec
            logger.debug("# performTest: cycle " +i );
                       
            // change the attribut values  ocasionally                             
            // TODO  in the moment we  update only the simple boolean attributes
            // the other has to be  set too. 
            aircraft.clear();            
            double rangeForTesting = 0.4;
            
            if ( Math.random()  <= rangeForTesting   ) {
                aircraft.setEngineSmokeOn(false);
                logger.debug("performTest: random set of attributes setEngineSmokeOn" );
            }          
           if ( Math.random()  <= rangeForTesting   ) {
               aircraft.setFirePowerDisabled(false);
               logger.debug("performTest: random set of attributes set FirePowerDisabled" );
            }            
            if (Math.random() <= rangeForTesting) {
                aircraft.setFlamesPresent(false);
                logger.debug("performTest: random set of attributes set FlamesPresent" );
            }
            // HasAmmunitionSupplyCap
            // HasRecoveryCap
            // HasRepairCap
            // Immobilized            
            if (Math.random() <= rangeForTesting) {
                aircraft.setIsConcealed(false);
                logger.debug("performTest: random set of attributes set IsConcealed" );                
            }
            // PowerPlantOn
            // PropulsionSystemsData
            // SmokePlumePresent            
            if (Math.random() <= rangeForTesting) {
                aircraft.setTentDeployed(false);
                logger.debug("performTest: random set of attributes set TentDeployed" );                                
            }
            // TrailingEffectsCode
            
          aircraft.update();
            
          logger.debug("");
            
            Thread.sleep(2000);
        }
        
        //   TODO  change this to a specifig  Exception 
        } catch (Exception e) {
            throw new TcInconclusiveIf("performTest received Exception: ",  e);
        }
  	
        logger.info("test {} passed", this.getClass().getName());
	}

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
        logger.info("postamble action for test {}", this.getClass().getName());
        try {
            rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS);
            //rtiAmbassador.resignFederationExecution(ResignAction.NO_ACTION);
        } catch (InvalidResignAction | OwnershipAcquisitionPending | FederateOwnsAttributes | FederateNotExecutionMember
                | NotConnected | CallNotAllowedFromWithinCallback | RTIinternalError e) {
            throw new TcInconclusiveIf(e.getMessage());
        }		
	}

}
