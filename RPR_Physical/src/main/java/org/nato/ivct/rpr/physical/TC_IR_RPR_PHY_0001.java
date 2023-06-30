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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.AmphibiousVehicle;
import org.nato.ivct.rpr.objects.CulturalFeature;
import org.nato.ivct.rpr.objects.Expendables;
import org.nato.ivct.rpr.objects.GroundVehicle;
import org.nato.ivct.rpr.objects.Human;
import org.nato.ivct.rpr.objects.MultiDomainPlatform;
import org.nato.ivct.rpr.objects.Munition;
import org.nato.ivct.rpr.objects.NonHuman;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;
import org.nato.ivct.rpr.objects.Radio;
import org.nato.ivct.rpr.objects.Sensor;
import org.nato.ivct.rpr.objects.Spacecraft;
import org.nato.ivct.rpr.objects.SubmersibleVessel;
import org.nato.ivct.rpr.objects.Supplies;
import org.nato.ivct.rpr.objects.SurfaceVessel;
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

public class TC_IR_RPR_PHY_0001 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    PhysicalEntity phyEntity;
    PhysicalEntity[] toBeTestetEntityList;
    PhysicalEntity[] listOfPossibleEntities;
   
    HashMap<ObjectInstanceHandle, PhysicalEntity> announcedPhysicalEntitys = new HashMap<>();

    

    class TestCaseAmbassador extends NullFederateAmbassador {

        @Override
        public void discoverObjectInstance(
                         ObjectInstanceHandle theObjectInstanceH,
                         ObjectClassHandle theObjectClassH,
                         String objectName) throws FederateInternalError {
            logger.trace("discoverObjectInstance {}",  theObjectInstanceH);
            
            logger.info("# discoverObjectInstance without FederateHandle ");
            
           
            try {
                // Tests and Debug
                logger.debug("# discoverObjectInstance: reveived ObjectInstanceHandle with ObjectInstanceName:  "
                                                                             + rtiAmbassador.getObjectInstanceName(theObjectInstanceH)); // Debug
                logger.debug("# discoverObjectInstance: reveived ObjectClassHandle with rti-ObjectClassName:  " 
                                                                             + rtiAmbassador.getObjectClassName(theObjectClassH)); // Debug

                // Now we have to store this information in some Table 
                // but we have not only one physical entity but a lot of entities. so we take a copy our List  toBeTestetEntityList to test if we get the classnames.
                for (PhysicalEntity possibleElement : listOfPossibleEntities) {
                    //logger.debug("# discoverObjectInstance: Element of listOfPossibleEntities has HLA-Classname: " + toBeTestedElement.getHlaClassName()); // DEBUG
                    
                    // if the objectClassName match with the ObjectClassName of the received  ObjectClassHandle,
                    if (possibleElement.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClassH))) {
                        // we associate the receiced ObjectInstanceHandle to our toBeTestetEntity  Element 
                        possibleElement.setObjectHandle(theObjectInstanceH);
                        // And store it in  the map announcedPhysicalEntitys (knownPhysicalEntitys) with the ObjectInstanceHandle as  Key     
                        announcedPhysicalEntitys.put(theObjectInstanceH, possibleElement  );                                            
                    }
                }
            } catch (ObjectInstanceNotKnown | FederateNotExecutionMember | NotConnected | RTIinternalError
                    | InvalidObjectClassHandle e) {
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
        public void reflectAttributeValues(ObjectInstanceHandle theObjectInstanceH,  AttributeHandleValueMap attributeHandleVM,
                byte[] userSuppliedTag,  OrderType sentOrdering, TransportationTypeHandle theTransport,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
			
		    //logger.info(""); // Debug
			logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
			 logger.debug("# reflectAttributeValues: AttributHandleValueMap \"attributeHandleVM\":  " + attributeHandleVM); // Debug
			
			// what are the attributes we get ?   here we get  ObjectInstanceHandle   and  AttributeHandleValueMap			
			logger.debug("# reflectAttributeValues: got  ObjectInstanceHandle  theObject: " + theObjectInstanceH); // Debug
			
			
			// so we can store both  neither in a new map.   or  in our map  announcedPhysicalEntitys ?
			// is there a method to associate a AttributeHandleValueMap  to a  entity-Object ?
			// not in that simple  manner
			// but there is a method  "decode" that do  something  ?????  and give it back ???? or store it ?????
			
			// so do for each  Entity ....
			for (ObjectInstanceHandle _obIHandl   : announcedPhysicalEntitys.keySet() ) {
			    // if the ObjectInstanceHandle  equals
			    if (_obIHandl == theObjectInstanceH) {
			        try {
			        announcedPhysicalEntitys.get(_obIHandl).decode(attributeHandleVM);
			        } catch (Exception e) {
			            logger.error("reflectAttributeValues received Exception", e);
			        }
			    }
			}
		   
			// Most of them  " Decoding skipped       what to Do now    brf  30.06.2023
			
			
			
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
        
        PhysicalEntity.initialize(rtiAmbassador);
        
        
        
		try {
		    
		    // PhysicalEntity[] toBeTestetEntityList = {new Aircraft(), new AmphibiousVehicle() } ;
		    
	         toBeTestetEntityList =new PhysicalEntity[]  {new Aircraft() , new AmphibiousVehicle(), new GroundVehicle(), new MultiDomainPlatform(),
	                                                  new Spacecraft(), new SubmersibleVessel(), new SurfaceVessel(),
	                                                  new Human(), new NonHuman(), new CulturalFeature(), new  Munition(),
	                                                  new Expendables(), new Radio(), new Sensor(), new Supplies() } ;
	         
	         // we make a copie of the List  to work with it	       	         
	         listOfPossibleEntities = new PhysicalEntity[toBeTestetEntityList.length];	         
	         System.arraycopy(toBeTestetEntityList  , 0,   listOfPossibleEntities,   0, listOfPossibleEntities.length );
		         
		    
		    phyEntity = new PhysicalEntity();  
			
		    //  add all attributes to the subscribedAttribute-Hash of PhysicalEntity ( HLAobjectRoot)
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

			// trie what's about munition
			
			for (PhysicalEntity p : toBeTestetEntityList) {
                logger.debug(
                        "# in performTest subscribing all Elements of platformWorkList now " + p.getHlaClassName()); // Debug
                p.subscribe();
            }
						
            for (int i = 0; i < 10; i++) {  // Testing for 10 Sec
                                               
                // Test if we got with discoverObjectHandle more than physicalEntities, e.g. Aircraft   TODO  brf  27.06.2023                
                //logger.info( "### Test if in announcedPhysicalEntitys (builded in discoverObjectHandle) are more than physicalEntities, eg. Aircraft "  );
                for (ObjectInstanceHandle _OIH : announcedPhysicalEntitys.keySet() ) {                               // DEBUG
                    String  hlaClassname = announcedPhysicalEntitys.get(_OIH).getHlaClassName();
                    //logger.info("performTest:  testing announcedPhysicalEntitys:  got from element with ObjectInstanceHandle " +_OIH +" HlaClassname"    + hlaClassname);
                }
                // why  we get only Aircraft,  what's about munition ?   -> when in e.g. AircraftApp  a Attribut of physicalEntity is 'added' we get munition in DiscoverObjectHandle
                
                
                
                // what to Do now    ? look which attributes are sended     brf 29.06.2023
                
                
                // ...
               Thread.sleep(1000);
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
