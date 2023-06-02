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


package org.nato.ivct.rpr.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.slf4j.Logger;
import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
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
import hla.rti1516e.FederateAmbassador.SupplementalReflectInfo;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AttributeNotDefined;
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
import hla.rti1516e.exceptions.InvalidAttributeHandle;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NameNotFound;
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
 *   But what is the meaning of  "all defined Attributes" 
 *   all attributes defined in   RPR-Physical_v2.0  ?
 *   
 *   in SISO-STD-001  P. 44  Table 6   "PhysicalEntity Attributes"  they are listed
 *   AcousticSignatureIndex   AlternateEntityType   ArticulatedParametersArray
 *   CamouflageType    DamageState  EngineSmokeOn   FirePowerDisabled  FlamesPresent
 *   ForceIdentifier  
 *   HasAmmunitionSupplyCap  HasFuelSupplyCap  HasRecoveryCap  HasRepairCap
 *   Immobilized  InfraredSignatureIndex   IsConcealed  LiveEntityMeasuredSpeed
 *   Marking   PowerPlantOn  PropulsionSystemsData  RadarCrossSectionSignatureIndex
 *   SmokePlumePresent    TentDeployed   TrailingEffectsCode   VectoringNozzleSystemData
 *          
 * 
 */

public class TC_IR_RPR_PHY_0001 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
	Semaphore physicalEntityDiscovered = new Semaphore(0);
	
    HashMap<ObjectInstanceHandle, PhysicalEntity> knownPhysicalEntitys = new HashMap<>();
    
    ObjectClassHandle temp_objectClassHandle;
    
    PhysicalEntity phyEntity;


    class TestCaseAmbassador extends NullFederateAmbassador {
    	
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject, 
				ObjectClassHandle theObjectClass,
				String objectName) throws FederateInternalError {
			logger.trace("discoverObjectInstance {}", theObject);			
			
			// Tests and Debug
			try {
			logger.info("discoverObjectInstance rtiAmbassador.getObjectInstanceName: " +  rtiAmbassador.getObjectInstanceName(theObject) );
			logger.info("discoverObjectInstance rtiAmbassador.getObjectClassName: "    +  rtiAmbassador.getObjectClassName(theObjectClass ) );
			}  catch ( ObjectInstanceNotKnown |  FederateNotExecutionMember | NotConnected | RTIinternalError |InvalidObjectClassHandle e) {
				logger.error("discoverObjectInstance received Exception", e  ); 
			}
			
			try {			
                String receivedClass = rtiAmbassador.getObjectClassName(theObjectClass);
                
                // to Do
                // not yet  implanted,  incorrect Klassnames ..... 
                
                if (receivedClass.equals(phyEntity.getHlaClassName())) {
					// create the helper object
                    PhysicalEntity obj = new PhysicalEntity();
                    obj.setObjectHandle(theObject);
                    knownPhysicalEntitys.put(theObject, obj);
                    
                   temp_objectClassHandle = theObjectClass;     // debug  testing                    
                } 
			} catch (Exception e) {
				logger.warn("discovered object instance, but federate {} is not connected", getSutFederateName());
			}
			physicalEntityDiscovered.release(1);
		}

		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject,
				ObjectClassHandle theObjectClass,
				String objectName,
				FederateHandle producingFederate) throws FederateInternalError {
			logger.trace("discoverObjectInstance {} with producingFederate {}", theObject, producingFederate);
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
        public void reflectAttributeValues(ObjectInstanceHandle theObject,
        		AttributeHandleValueMap theAttributes,
                byte[] userSuppliedTag,
                OrderType sentOrdering,
                TransportationTypeHandle theTransport,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
			
			System.out.println(""); // Debug
			logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
			//semaphore.release(1);
			
			try {			
			// we have to store the incoming Information to analyse it later
			System.out.println("\n# ---------   Testing and Debugging --------------------------------"); 	
			logger.info("reflectAttributeValues: Amount of transmitted  attributes: " +theAttributes.size());			
		    System.out.println("# reflectAttributeValues: got  ObjectInstanceHandle  theObject: " +theObject ); // Debug		    
		    System.out.println("# reflectAttributeValues: rti-objectInstanceName of theObject:  \t" + rtiAmbassador.getObjectInstanceName(theObject));  // Debug
		    
		    System.out.println("\n# reflectAttributeValues:   AttributHandleValueMap theAttributes:  "+ theAttributes);  // Debug
		    System.out.println("#reflectAttributeValues:  the Keys in AttributHandleValueMap  : " + theAttributes.keySet() );
		    
		    // we need the Names of the Attributes 
		 	System.out.println("# reflectAttributeValues: Names of received Attributes ");
		 		for ( AttributeHandle a : theAttributes.keySet() ) {
		 			System.out.println(rtiAmbassador.getAttributeName(temp_objectClassHandle, a) );
		 		}
		    
			
			if (phyEntity.getHlaClassName().equals(rtiAmbassador.getObjectClassName(temp_objectClassHandle))) {					
				System.out.println("# class names are equal, do here something"); // Debug
				
				// eg.  write the Informations to    known_Instance_AttrValueMap
			}
			
			
			
			} catch ( FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined | InvalidAttributeHandle | InvalidObjectClassHandle | ObjectInstanceNotKnown  e) 
			{ 	};
			
			// (ObjectInstanceNotKnown | FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined | InvalidAttributeHandle | InvalidObjectClassHandle    e
			
			
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
            .getArray();
			
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
		try {
			PhysicalEntity.initialize(rtiAmbassador);
			phyEntity = new PhysicalEntity();			
			
			//phyEntity.addSubscribe(BaseEntity.Attributes.EntityIdentifier);			
			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.AcousticSignatureIndex);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.AlternateEntityType);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.ArticulatedParametersArray);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.CamouflageType);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.DamageState);
			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.ForceIdentifier);  
			phyEntity.addSubscribe(PhysicalEntity.Attributes.HasAmmunitionSupplyCap);
			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.HasFuelSupplyCap);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.HasRecoveryCap);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.HasRepairCap);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.Immobilized);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.InfraredSignatureIndex);
			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.IsConcealed);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.LiveEntityMeasuredSpeed);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.Marking);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.PowerPlantOn);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.PropulsionSystemsData);
			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.RadarCrossSectionSignatureIndex);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.SmokePlumePresent);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.TentDeployed);			
			phyEntity.addSubscribe(PhysicalEntity.Attributes.TrailingEffectsCode);
			phyEntity.addSubscribe(PhysicalEntity.Attributes.VectoringNozzleSystemData);
			
			phyEntity.subscribe();
			
			// temp_objectClassHandle
			
			AttributeHandle _AcousticSignatureIndex ;
			AttributeHandle _AlternateEntityType;
			AttributeHandle _ArticulatedParametersArray;
			AttributeHandle _CamouflageType;
			
			_AcousticSignatureIndex = rtiAmbassador.getAttributeHandle(temp_objectClassHandle, "AcousticSignatureIndex");
			_AlternateEntityType = rtiAmbassador.getAttributeHandle(temp_objectClassHandle, "AlternateEntityType");
			_ArticulatedParametersArray = rtiAmbassador.getAttributeHandle(temp_objectClassHandle, "ArticulatedParametersArray");
			_CamouflageType = rtiAmbassador.getAttributeHandle(temp_objectClassHandle, "CamouflageType");
			
			AttributeHandleSet _attributes = rtiAmbassador.getAttributeHandleSetFactory().create();
			
			_attributes.add(_AcousticSignatureIndex);
			_attributes.add(_AlternateEntityType);
			_attributes.add(_ArticulatedParametersArray);
			_attributes.add(_CamouflageType);
			
			rtiAmbassador.publishObjectClassAttributes(temp_objectClassHandle, _attributes);
			
			System.out.println("Added some  attributes to :"+  rtiAmbassador.getObjectClassName(temp_objectClassHandle ) );
			
			// wait until object is discovered
			
			boolean gotEnoughAtttributes = true;
            while (! gotEnoughAtttributes) {
                // the Test ......
            }
    
			
		} catch (Exception e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
        logger.info("test {} passed", this.getClass().getName());
	}

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
        logger.info("postamble action for test {}", this.getClass().getName());
        try {
            rtiAmbassador.resignFederationExecution(ResignAction.NO_ACTION);
        } catch (InvalidResignAction | OwnershipAcquisitionPending | FederateOwnsAttributes | FederateNotExecutionMember
                | NotConnected | CallNotAllowedFromWithinCallback | RTIinternalError e) {
            throw new TcInconclusiveIf(e.getMessage());
        }		
	}

}
