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
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.interactions.HLAreportInteractionPublication;
import org.nato.ivct.rpr.interactions.HLAreportInteractionSubscription;
import org.nato.ivct.rpr.interactions.HLAreportObjectClassPublication;
import org.nato.ivct.rpr.interactions.HLAreportObjectClassSubscription;
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.AmphibiousVehicle;
import org.nato.ivct.rpr.objects.HLAmanager;
import org.nato.ivct.rpr.objects.HLAfederate;
import org.nato.ivct.rpr.objects.HLAobjectRoot;

import org.nato.ivct.rpr.objects.PhysicalEntity;

import org.slf4j.Logger;
import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.AttributeHandle;
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
import hla.rti1516e.encoding.DecoderException;
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

public class TC_IR_RPR2_PHY_0001 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    PhysicalEntity phyEntity;
    //PhysicalEntity[] toBeTestetEntityList;
    HLAobjectRoot[] toBeTestetEntityList;
    
    //PhysicalEntity[] listOfPossibleEntities;  // listOfPossibleEntities is a Copy of toBeTestetEntityList
    HLAobjectRoot[] listOfPossibleEntities;  // listOfPossibleEntities is a Copy of toBeTestetEntityList
    
    //HashMap<ObjectInstanceHandle, PhysicalEntity> announcedPhysicalEntitys = new HashMap<>();
    HashMap<ObjectInstanceHandle, HLAobjectRoot > announcedEntitys = new HashMap<>();
    
    HLAmanager[] toTestHLAmanagerClasses;
    HashMap<ObjectInstanceHandle, HLAmanager> announcedHLAmanagerEntitys = new HashMap<>();
    private HashMap<ObjectInstanceHandle, Boolean> requestReport = new HashMap<>();
    private Semaphore federateDiscovered = new Semaphore(0);
        

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

                // Now we have to store this information in some Table 
                // but we have not only one (physical or other) entity but a lot. so we take a copy our List  toBeTestetEntityList to test if we get the classnames.
                
                // We  consider all as   HLARoot.entities                so not     //for (PhysicalEntity possibleElement : listOfPossibleEntities) {
                for (HLAobjectRoot possibleElement : listOfPossibleEntities) {                   
                    // if the objectClassName match with the ObjectClassName of the received  ObjectClassHandle,
                    if (possibleElement.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClassH))) {
                        // we associate the receiced ObjectInstanceHandle to our toBeTestetEntity  Element 
                        possibleElement.setObjectHandle(theObjectInstanceH);
                        // And store it in  the map announcedPhysicalEntitys (knownPhysicalEntitys) with the ObjectInstanceHandle as  Key     
                        announcedEntitys.put(theObjectInstanceH, possibleElement  );
                        logger.debug("# discoverObjectInstance:  stored objectInstanceHandle and  Entity-Element in announcedEntitys :  " 
                                + theObjectInstanceH+ " and  Element:  "    +  possibleElement.getHlaClassName()      ); // Debug
                    }
                }
                
                /*     deactivated  temporary for less output
                // at this point  HLAfederate.discover  could be used, because the published attributes should be reached over the anchor
                HLAfederate fed = HLAfederate.discover(theObjectInstanceH, theObjectClassH);                
                if (fed != null) {
                    //logger.trace("discovered HLAfederate object : {}({})", theObjectInstanceH, receivedClassName);
                    logger.debug("# discoverObjectInstance:  discovered HLAfederate object : {}({})", theObjectInstanceH, receivedClassName);
                    requestReport.put(theObjectInstanceH, true);
                    federateDiscovered.release(1);
                } 
                // ok,   here ist something  done,  may be we have a HLAfederate in the hashmap knownObjects ?
                // but what to do with it ?   use it in  reflectAttributValues    with decode  anchor   ?
                 * 
                 */
                
            //} catch (ObjectInstanceNotKnown | FederateNotExecutionMember | NotConnected | RTIinternalError | InvalidObjectClassHandle| RprBuilderException e) {    
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
		
            //logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
            logger.debug("### reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");

            // what are the attributes we get ? here we get ObjectInstanceHandle and  AttributeHandleValueMap
            logger.debug("# reflectAttributeValues: got  ObjectInstanceHandle  \"theObjectInstanceH\" : " + theObjectInstanceH
                                                                       + " with AttributHandleValueMap :   " + attributeHandleVM); // Debug
           /* deactivated temporary for less outpug
            try {  
                // so do for each Entity of all kinds of known Entities   // get the entities ( classnames etc. from announcedEntitys
                for (ObjectInstanceHandle _obIHandl : announcedEntitys.keySet()) {
                    logger.debug("# reflectAttributeValues: show all  ObjectInstanceHandle in  announcedEntitys " + _obIHandl); // Debug

                    if (theObjectInstanceH.equals(_obIHandl)) {
                        ObjectClassHandle toTestObjectClassHandle = announcedEntitys.get(_obIHandl).getClassHandle();
                        for (AttributeHandle a : attributeHandleVM.keySet()) {
                            String tempAttributname = rtiAmbassador.getAttributeName(toTestObjectClassHandle, a);
                            logger.debug(" # reflectAttributeValues: get Attributnames " + tempAttributname);                     // DEBUG
                            // case tempAttributname ........................
                        }
                    }
                }
           
                logger.debug("");
                // Our interest here is to get Attributes from HLAfederate, but with the obove there is nothing

                // in discoverObjectInstance we used the Method HLAfederate.discover to find if  the received Class match with the class in anchor
                // discover uses the method HLAfederate.get to ensure that the Object ist stored in knownObjects.
                // so we assume to have a Object in knownObjects with this objectInstanceHandle

                HLAfederate fed = HLAfederate.get(theObjectInstanceH);             
                if (fed != null) {
                    logger.debug(" #  reflectAttributeValues federatesSection  : get  HLAfederate fed.get found something   " + fed);
                    // so there is an entry in HLAfederate knownObjects with this  ObjectinstanceHandle
                    fed.decode(attributeHandleVM);                    
                    
                    //  older Tests         // but is the Federate the same ?
                    String sutFederateSutName = getSutFederateName();
                    String receivedFederateName = fed.getHLAfederateName();
                    logger.debug("#  reflectAttributeValues :  getSutFederateName() : " + sutFederateSutName+ " -  fed.getHLAfederateName() " + receivedFederateName);
                    //  fed.getHLAfederateName()   does not work !!!!!!!!!!! 
                    // federate is org.nato.ivct.rpr.objects.HLAfederate !
                    
                    // to be continued at this point                     
                    
                    logger.debug(" lets see what gives back the differnd HLAfederate methods: " +                    
                       fed.getHLAfederateName()  + " -- " +
                       fed.getHLAfederateType()   + " -- " +
                       fed.getHLAfederateHost()   + " -- " +
                       fed.getHLARTIversion()                );
                   // But what ' s the Use of this for our  Question ?
                }
         
                 // decode is a method prepared in HLAobjectRoot // but what is it doing ? "  unknown attribute ... Decoding skipped"
                    // logger.debug("# reflectAttributeValues: trying decode with : " +  announcedEntitys.get(_obIHandl) );
                    // announcedEntitys.get(_obIHandl).decode(attributeHandleVM);
                    
                } catch (FederateNotExecutionMember | NameNotFound | NotConnected | RTIinternalError |
                            AttributeNotDefined | InvalidAttributeHandle | InvalidObjectClassHandle| 
                            RprBuilderException | DecoderException e) {
                }
                */
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
        //toBeTestetEntityList = new PhysicalEntity[] { new Aircraft(), new AmphibiousVehicle() };
         // try to make a List of  HLAobjectRoot entities for all kind of Entities
         toBeTestetEntityList = new HLAobjectRoot[] { new Aircraft(), new AmphibiousVehicle(), new HLAfederate() };
       
         // we make a copie of the List to work with it
         //listOfPossibleEntities = new PhysicalEntity[toBeTestetEntityList.length];
         listOfPossibleEntities = new HLAobjectRoot[toBeTestetEntityList.length];      
         System.arraycopy(toBeTestetEntityList, 0, listOfPossibleEntities, 0, listOfPossibleEntities.length);
         
         // only to test if we get Informations obout Objects and Attributes
         phyEntity = new PhysicalEntity();
         phyEntity.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);      
         phyEntity.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);         
         phyEntity.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
         phyEntity.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
         phyEntity.addSubscribe(PhysicalEntity.Attributes.TentDeployed);
         
         // TODO  we look for a posibility to subscribe to  management Objects from the RTI / Federation           
         // something like     // aircraft_FederateHandle.addSubscribe(HLAreportObjectClassPublication);     
        
          // try to analyse other Test to use the HLAfederate specific  methods   brf  10.07.2023
         // with addSub we  create a static HLAfederate-Object anchor (?)  
         // and  add  with addSub to this  the attribut....... (?)
         // the anchor  can be used in  discoverObjectInstance  whith "discover "
         HLAfederate.addSub(HLAfederate.Attributes.HLAfederateHandle) ;
         HLAfederate.addSub(HLAfederate.Attributes.HLAfederateState) ;
         HLAfederate.addSub(HLAfederate.Attributes.HLAfederateName) ;
         HLAfederate.addSub(HLAfederate.Attributes.HLAfederateType) ;
         HLAfederate.addSub(HLAfederate.Attributes.HLAfederateHost) ;
         HLAfederate.addSub(HLAfederate.Attributes.HLARTIversion) ;
         HLAfederate.sub();
         // Seems if we get now 2 HLAfederate Instances in discoverObjectInstance

         (new HLAreportObjectClassPublication()).subscribe();
         (new HLAreportObjectClassSubscription()).subscribe();
         (new HLAreportInteractionPublication()).subscribe();
         (new HLAreportInteractionSubscription()).subscribe();
          // and now,  do we get  Attributes ?  not yet
                  
          // how to get informations about the  federation an other federates
         String  hlaVersion = rtiAmbassador.getHLAversion();
         logger.debug("#### in performTest:  first Try to communicate direkt to RTI getHLAVersion:    " +  hlaVersion );
         
         String  rtiAmbassadorInfos = rtiAmbassador.toString() ;
         logger.debug("#### in performTest:  what shows us rtiAmbassador.toString():    " +  rtiAmbassadorInfos );
                 
         // may be we have to get something with discoverObjectInstance ( with federateHandle ?)
         // try to askt rtiAmbassodor for  more)
         FederateHandle  aircraft_FederateHandle = rtiAmbassador.getFederateHandle("Aircraft");         
         logger.debug("#### in performTest:  rtiAmbassador.getFederateHandle(sutFederateName) gives us:   " + aircraft_FederateHandle );
         logger.debug("#### in performTest:  what we get with aircraft_FederateHandle.getClass():    "+ aircraft_FederateHandle.getClass() );
         
         for (HLAobjectRoot hlaRootEnt : toBeTestetEntityList) {
             logger.debug("# in performTest subscribing all Elements of platformWorkList now " + hlaRootEnt.getHlaClassName()); // Debug
             hlaRootEnt.subscribe();
         }
         
         Aircraft aircraft = new Aircraft();
         // for testing  we have to send all attributes from physical entity     
         aircraft.addPublish(PhysicalEntity.Attributes.CamouflageType);
         aircraft.addPublish(PhysicalEntity.Attributes.DamageState);
         aircraft.addPublish(PhysicalEntity.Attributes.EngineSmokeOn);
         aircraft.addPublish(PhysicalEntity.Attributes.FirePowerDisabled);
         aircraft.addPublish(PhysicalEntity.Attributes.FlamesPresent);
         aircraft.addPublish(PhysicalEntity.Attributes.Immobilized);
         aircraft.addPublish(PhysicalEntity.Attributes.IsConcealed);
         aircraft.addPublish(PhysicalEntity.Attributes.PowerPlantOn);
         aircraft.addPublish(PhysicalEntity.Attributes.SmokePlumePresent);
         aircraft.addPublish(PhysicalEntity.Attributes.TentDeployed);
         aircraft.addPublish(PhysicalEntity.Attributes.TrailingEffectsCode);
         aircraft.register();
         
         /*
         phyEntity.addPublish(PhysicalEntity.Attributes.AcousticSignatureIndex);
         phyEntity.addPublish(PhysicalEntity.Attributes.AlternateEntityType);
         phyEntity.addPublish(PhysicalEntity.Attributes.ArticulatedParametersArray);
         phyEntity.addPublish(PhysicalEntity.Attributes.CamouflageType);
         phyEntity.addPublish(PhysicalEntity.Attributes.DamageState);

         phyEntity.addPublish(PhysicalEntity.Attributes.EngineSmokeOn);
         phyEntity.addPublish(PhysicalEntity.Attributes.FirePowerDisabled);
         phyEntity.addPublish(PhysicalEntity.Attributes.FlamesPresent);
         phyEntity.addPublish(PhysicalEntity.Attributes.ForceIdentifier);
         phyEntity.addPublish(PhysicalEntity.Attributes.HasAmmunitionSupplyCap);

         phyEntity.addPublish(PhysicalEntity.Attributes.HasFuelSupplyCap);
         phyEntity.addPublish(PhysicalEntity.Attributes.HasRecoveryCap);
         phyEntity.addPublish(PhysicalEntity.Attributes.HasRepairCap);
         phyEntity.addPublish(PhysicalEntity.Attributes.Immobilized);
         phyEntity.addPublish(PhysicalEntity.Attributes.InfraredSignatureIndex);

         phyEntity.addPublish(PhysicalEntity.Attributes.IsConcealed);
         phyEntity.addPublish(PhysicalEntity.Attributes.LiveEntityMeasuredSpeed);
         phyEntity.addPublish(PhysicalEntity.Attributes.Marking);
         phyEntity.addPublish(PhysicalEntity.Attributes.PowerPlantOn);
         phyEntity.addPublish(PhysicalEntity.Attributes.PropulsionSystemsData);

         phyEntity.addPublish(PhysicalEntity.Attributes.RadarCrossSectionSignatureIndex);
         phyEntity.addPublish(PhysicalEntity.Attributes.SmokePlumePresent);
         phyEntity.addPublish(PhysicalEntity.Attributes.TentDeployed);
         phyEntity.addPublish(PhysicalEntity.Attributes.TrailingEffectsCode);
         phyEntity.addPublish(PhysicalEntity.Attributes.VectoringNozzleSystemData);
         phyEntity.register();
         */
  
       //  and change some 'simple'  boolean Attributes     // for other Attributes e.g. CamouflageType we may need  a Struct    
       aircraft.setEngineSmokeOn(true);            
       //phyEntity.setFirePowerDisabled(true);            
       //phyEntity.setFlamesPresent(true);           
       //phyEntity.setIsConcealed(true);          
       //phyEntity.setTentDeployed(true);
       
       
       aircraft.update();
        
        for (int i = 0; i < 5; i++) {  // Testing for 10 Sec
            logger.debug("# performTest: cycle " +i );
            
            // show me what is now in the List  announcedentities 
            //for (ObjectInstanceHandle _OIH : announcedPhysicalEntitys.keySet() ) {                  // DEBUG
            for (ObjectInstanceHandle _OIH : announcedEntitys.keySet() ) {                               // DEBUG
                String  temp_objectInstanceHandleName = rtiAmbassador.getObjectInstanceName(_OIH) ;
                String element_Classname = announcedEntitys.get(_OIH).getHlaClassName();
                //logger.info("performTest:  see in announcedEntitys : ObjectInstanceHandleName  and  HLARootEntity " + temp_objectInstanceHandleName + " with " + element_Classname );
                 logger.info("performTest:  see in announcedEntitys : ObjectInstanceHandle  " + _OIH + "  and  HLARootEntity with classname: " + element_Classname );
            }
                       
            // change the values  ocasionally                             
            int randomNumber = (int) (Math.random()  * (1000 -1) +1 );      
            logger.debug("Random Number ist nun " + randomNumber);        
            
            // in the moment we  update only the simple boolean attributes
            
            if (Math.round(randomNumber % 10) == 0) {
                logger.debug(" random  is a 10er : " + Math.round(randomNumber % 10) );
                aircraft.setEngineSmokeOn(false);  
            }
            if (Math.round(randomNumber % 9) == 0) {
                logger.debug(" random  is a 9er : " + Math.round(randomNumber % 9) );
                aircraft.setFirePowerDisabled(false);  
            }
            if (Math.round(randomNumber % 8) == 0) {
                logger.debug(" random  is a 8er : " + Math.round(randomNumber % 8) );
                aircraft.setFlamesPresent(false);
            }
           // HasAmmunitionSupplyCap
            //HasRecoveryCap
            //HasRepairCap
            //Immobilized            
            if (Math.round(randomNumber % 7) == 0) {
                logger.debug(" random  is a 7er : " + Math.round(randomNumber % 7) );
                aircraft.setIsConcealed(false);
            }            
            //PowerPlantOn
            //PropulsionSystemsData            
            //SmokePlumePresent     
            if (Math.round(randomNumber % 6) == 0) {
                logger.debug(" random  is a 6er : " + Math.round(randomNumber % 6) );
                aircraft.setTentDeployed(false);
            }
            //TrailingEffectsCode            
            if (Math.round(randomNumber % 5) == 0) {
                logger.debug(" random  is a 5er : " + Math.round(randomNumber % 5) );
                // repetitions  till there are enough setters in Aircraft
                aircraft.setEngineSmokeOn(false);
            }
            if (Math.round(randomNumber % 4) == 0) {
                logger.debug(" random  is a 4er : " + Math.round(randomNumber % 4) );
                // repetitions  till there are enough setters in Aircraft
                aircraft.setFirePowerDisabled(false);  
            }
            if (Math.round(randomNumber % 3) == 0) {
                logger.debug(" random  is a 3er : " + Math.round(randomNumber % 3) );
                // repetitions  till there are enough setters in Aircraft
            }
            if (Math.round(randomNumber % 2) == 0) {
                logger.debug(" random  is a 2er : " + Math.round(randomNumber % 2) );
            }
            
           /* 
           if  (  Math.round(randomNumber% 1)  == 0  ) {
               System.out.println(" rondom  is a 2er : " + Math.round(randomNumber%1)) ;
          }   */
          aircraft.update();
            
            
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
