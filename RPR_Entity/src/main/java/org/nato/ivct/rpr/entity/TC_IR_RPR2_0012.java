/**    Copyright 2022, brf (Fraunhofer IOSB)

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
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.AmphibiousVehicle;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;
import org.nato.ivct.rpr.objects.HLAobjectRoot;
import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib.TcFailed;
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
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidAttributeHandle;
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
 * Interoperability Requirement :   IR-RPR2-0012
 *
 * SuT shall not update non-applicable PhysicalEntity Attributes 
 * as specified in Domain Appropriateness table in SISO-STD-001-2015."
 */

/*   Table 7 Domain Appropriateness for PhysicalEntity Attributes 
 * e.g 
 * Attribute Name       Aircraft  AmphibiousVehicle  GroundVehicle  Spacecraft
 * ----------------------------------------------------------------------------
 * CamouflageType         Yes
 * DamageState            Yes
 * EngineSmoke            Yes
 * FirePowerDisabled      
 * FlamesPresent          Yes
 * Immobilized            Yes
 * IsConcealed                     
 * PowerPlantOn           Yes
 * SmokePlumePresent      Yes
 * TentDeployed           
 * TrailingEffectsCode    Yes
 * 
 * 
 * so we have to test if special Attributes will be updated    for Aircraft  eg.
 * ( we need not to decode,  just see if they are updated )
 *  <name>FirePowerDisabled</name>    <dataType>RPRboolean</dataType>    
 *  <name>IsConcealed</name>          <dataType>RPRboolean</dataType>     
 *  <name>TentDeployed</name>         <dataType>RPRboolean</dataType>
  *     
 *  we have to get the Classname (physicalEntity) of the ObjectInstance with which the Attributes are received by reflectAttributeValues
 *  and check in a List  if for this classname the received attribute is one of the 'special' Attributes
 *   
 *   so we need a List with  classnames and  NaAttributes for this classname -->  classNamesAndNaAttributList
 * 
 *    Aircraft              FirePowerDisabled  IsConcealed    TentDeployed     
 *    AmphibiousVehicle      -----
 *    GroundVehicle          ----- 
 *    Spacecraft             EngineSmokeON FirePowerDisabled  IsConcealed  TentDeployed  TrailingEffectsCode
 *    SurfaceVessel          FirePowerDisabled   IsConcealed  TentDeployed
 *    SubmersibleVessel      EngineSmokeON FirePowerDisabled  IsConcealed  TentDeployed  TrailingEffectsCode
 *    MultiDomainPlatform    -----
 *    Lifeform               EngineSmokeON FirePowerDisabled FlamesPresent Immobilized  PowerPlantOn  SmokePlumePresent TentDeployed  TrailingEffectsCode
 *    ...  
    
 *     ---------------------------------------
 *    
 */


public class TC_IR_RPR2_0012 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Logger logger = null;
	
	HashMap<String , ArrayList > objectClassNamesAndReceivedAttributeList = new HashMap<>(); 
	
	HashMap<ObjectInstanceHandle , ObjectClassHandle > objectInstanceHandlesAndobjectClassHandle = new HashMap<>();
	ObjectClassHandle temp_objectClassHandle = null;   // to be replaced
	
	Platform toTestPlatform;
	Platform[] platformWorkList;
	
    //  todo   complete  for all Physical Entities  these are the Entities listed in Table 2
	// arrays with non-applicable PhysicalEntity Attribute-names for earch Platform
	String[] naListAircraft = new String[] {"FirePowerDisabled" , "IsConcealed" , "TentDeployed"};	
	String[] naListAmphibiousVehicle = new String[] {"-----------"};	
	String[] naListGroundVehicle = new String[] {"--------------"};		
	String[] naListMultiDomainPlatform = new String[] {"--------------"};		
	String[] naListSpacecraft = new String[] {"EngineSmokeON" , "FirePowerDisabled" , "IsConcealed" , "TentDeployed" ,"TrailingEffectsCode"};	
	String[] naListSubmersibleVessel = new String[] {"EngineSmokeON" , "FirePowerDisabled" , "IsConcealed" , "TentDeployed" ,"TrailingEffectsCode"};	
	String[] naListSurfaceVessel = new String[] {"FirePowerDisabled" , "IsConcealed" , "TentDeployed"};
	
	// HashMap  with classnames as key  and an array with non-applicable PhysicalEntity Attributes for this class
	// to  allocate the objectclassnames  with a List of non-applicable PhysicalEntity Attributes for this Physical Entity
	HashMap<String, String[]> classNamesAndNaAttributList = new HashMap<String, String[]>() ;
	
	public TC_IR_RPR2_0012() {		
		// toDo complete
		// fill the Map of classnames and non-applicable PhysicalEntity Attributes for this Physical Entity
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Aircraft", naListAircraft);
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.AmphibiousVehicle", naListAmphibiousVehicle);
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.GroundVehicle", naListGroundVehicle);
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.MultiDomainPlatform", naListMultiDomainPlatform);		
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Spacecraft", naListSpacecraft);
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SubmersibleVessel", naListSubmersibleVessel);
		classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel", naListSurfaceVessel);
	}
	

	class TestCaseAmbassador extends NullFederateAmbassador {
		
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObjectInstanceH,
				ObjectClassHandle theObjectClassH,
				String objectName) throws FederateInternalError {
			
			logger.info(""); // Debug
			logger.trace("discoverObjectInstance {}", theObjectInstanceH);
			// semaphore.release(1);
			
			try {
				// we only want to observe selected  "platform classes" defined in performTest
				for (Platform p : platformWorkList) {
					if ( p.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClassH) ) ) {						
						logger.debug("# discoverObjectInstance: reveived Instance with rti-ObjectClassName:  " + rtiAmbassador.getObjectClassName(theObjectClassH)); // Debug
						
						// we store a List of ObjectClassNames with their ObjectInstanceHandles
						if (objectInstanceHandlesAndobjectClassHandle.get(theObjectInstanceH)==null ) {							
							objectInstanceHandlesAndobjectClassHandle.put(theObjectInstanceH, theObjectClassH);							
						}
						
						// TODO  unclear
						for (ObjectInstanceHandle iH :  objectInstanceHandlesAndobjectClassHandle.keySet() ) {
							logger.debug("#discoverObjectInstance: is something in objectInstanceHandlesAndobjectClassHandle ? : " +objectInstanceHandlesAndobjectClassHandle );
						}			
					}
				}
			} catch (Exception e) {
				logger.error("discoverObjectInstance received Exception", e);
			}
		}		
		
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle  theObject,
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
        public void reflectAttributeValues(ObjectInstanceHandle theObjectInstanceH,
        		AttributeHandleValueMap theAttributes,
                byte[] userSuppliedTag,
                OrderType sentOrdering,
                TransportationTypeHandle theTransport,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
			
			logger.info(""); // Debug
			logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
			//semaphore.release(1);
			
			try {				
				// get the ClassHandle for the class with this objectInstanceHandle
				ObjectClassHandle  tempLocalObjectClasshandle = objectInstanceHandlesAndobjectClassHandle.get(theObjectInstanceH);				
				// get the objectClassname 			
				String tempLocalObjectClassname = rtiAmbassador.getObjectClassName(tempLocalObjectClasshandle);
				
			logger.debug("# ---------   Testing and Debugging --------------------------------"); 
			logger.debug("# reflectAttributeValues: Amount of transmitted  attributes: " +theAttributes.size());	
		    logger.debug("# reflectAttributeValues: got  ObjectInstanceHandle  theObject: " +theObjectInstanceH ); // Debug			    
		    logger.debug("# reflectAttributeValues: rti-objectInstanceName of theObject: " + rtiAmbassador.getObjectInstanceName(theObjectInstanceH));  // Debug		    
		    logger.debug("# reflectAttributeValues: rti-ObjectClassenName for this ObjectInstanceHandle : " + rtiAmbassador.getObjectClassName(tempLocalObjectClasshandle));  // Debug
		    logger.debug("# reflectAttributeValues: AttributHandleValueMap \"theAttributes\":  "+ theAttributes);  // Debug
		    logger.debug("# reflectAttributeValues: the Keys in AttributHandleValueMap  : " + theAttributes.keySet() );  		    
		 	logger.debug("# reflectAttributeValues: Names of received Attributes:");		 	 
		 	//  list the Attributes for this ObjectClassHandle
		 	for ( AttributeHandle a : theAttributes.keySet() ) {
	 			logger.debug(rtiAmbassador.getAttributeName(tempLocalObjectClasshandle, a) );
	 		 }			
			logger.debug("# ---------  End of Testing and Debugging --------------------------------"); 
					
			// use a Array to store all now received attributes		
			ArrayList<String> localListOfReceivedAttributNames = new ArrayList<String>(); 
			
			for ( AttributeHandle a : theAttributes.keySet() ) {		 			
	 			 String tempAttributname = rtiAmbassador.getAttributeName(tempLocalObjectClasshandle, a);		 			
	 			if (! localListOfReceivedAttributNames.contains(tempAttributname)) {
	 				localListOfReceivedAttributNames.add(tempAttributname);
	 			}		 			
	 		}
		
			// use a Map to store the a array of the receivedAttributes with a ObjectClassname
			//    we know that the tempLocalObjectClassname  is correct for the  received ObjectInstanceHandle
			//    so we store all attributes with this classname			    		
			 objectClassNamesAndReceivedAttributeList.put(tempLocalObjectClassname, localListOfReceivedAttributNames);
			 
			// now we have a allocation table ObjectClassNames and Attributlists in ObjectClassNamesAndAttributeList to work with it in performTest
			
			} catch ( FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined | InvalidAttributeHandle | InvalidObjectClassHandle | ObjectInstanceNotKnown  e) 
			{ 	};
		}
	}
	
	
	

	@Override
	protected void logTestPurpose(Logger logger) {
	 String msg = "Test Case Purpose: " ;
	        msg += "The test case verifies that the SuT do not update non-applicable PhysicalEntity Attributes ";
	        msg += "as specified in Domain Appropriateness table in SISO-STD-001-2015.";
		logger .info(msg);		
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
				| FederationExecutionDoesNotExist | InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD | SaveInProgress
				| RestoreInProgress | FederateAlreadyExecutionMember | NotConnected e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
	}

	
		
	@Override
	protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
		logger.info("perform test {}", this.getClass().getName());
		
		Platform.initialize(rtiAmbassador);
		
		boolean match = false;
		
		// to build a fail - Message
		
		ArrayList<String>resultListReceivedNaAttributes=  new ArrayList<String>();;
		HashMap<String, ArrayList> resultMapClassNamesAndReceivedNaAttributes = new HashMap<String, ArrayList>() ;
		
		try {
			
			toTestPlatform = new Platform();
			
			// toDo Complete  the Platform Classes
			platformWorkList = new Platform[] {new Aircraft() , new AmphibiousVehicle() } ;
			//platformWorkList = new Platform[] {new Aircraft() , new AmphibiousVehicle(), new GroundVehicle(), new MultiDomainPlatform(), new Spacecraft(), new SubmersibleVessel(), new SurfaceVessel() } ;
			
			
			toTestPlatform.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
			toTestPlatform.addSubscribe(BaseEntity.Attributes.EntityType);			
			toTestPlatform.addSubscribe(BaseEntity.Attributes.Spatial);
			
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.CamouflageType);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.DamageState);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.Immobilized);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.PowerPlantOn);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.SmokePlumePresent);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.TentDeployed);
			toTestPlatform.addSubscribe(PhysicalEntity.Attributes.TrailingEffectsCode);			
					
		    // because  toTestPlatform.subscribe();   doesnt match correct		    
		    for (Platform p : platformWorkList  ) {
		    	logger.debug("# in performTest subscribing all Elements of platformWorkList now "  +p.getHlaClassName() );  // Debug
		    	p.subscribe();
		    }
		    
				// the Test ......

                for (int i = 0; i < 10; i++) { // toDo change this to a better mechanism

                    // check objectClassNamesAndReceivedAttributeList against a List with non applicable attributes for this class
                    // so we check for every ObjectClassName in the map objectClassNamesAndReceivedAttributeList
                    for (String tempObjectClassname : objectClassNamesAndReceivedAttributeList.keySet()) {

                        // if there is a array of nonApplicable Attributes in our map classNamesAndNaAttributList for this classname
                        if (classNamesAndNaAttributList.get(tempObjectClassname) != null) {
                            String[] tempNonApplicableAttributList = classNamesAndNaAttributList.get(tempObjectClassname);

                            // List of for this classname received attributes ( from the in reflectAttributesvalues created ArrayList)
                            ArrayList<String> tempReceivedAttributList = objectClassNamesAndReceivedAttributeList.get(tempObjectClassname);

                            // for every for/with this classname received attribut
                            for (String receivedAttribut : tempReceivedAttributList) {

                                // for every Attribut in the non applicable attributList for this classname
                                for (String nonAppAttribute : tempNonApplicableAttributList) {

                                    // if the received attribut is in the nonapplicable attributlist for this classname the test failed
                                    if (receivedAttribut.equals(nonAppAttribute)) {
                                        logger.debug("###  test failed  " + tempObjectClassname + " updated  " + receivedAttribut); // Debug
                                        match = true;

                                        if (!resultListReceivedNaAttributes.contains(receivedAttribut)) {
                                            resultListReceivedNaAttributes.add(receivedAttribut);
                                        }
                                        // building a List with classnames and received nonapplicable Attributes
                                        resultMapClassNamesAndReceivedNaAttributes.put(tempObjectClassname, resultListReceivedNaAttributes);
                                    }
                                }
                            }
                        }
                    }
                    Thread.sleep(1000);
                }

		} catch (Exception e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
		
		String failMessage = "";		
		for (String tempClassname : resultMapClassNamesAndReceivedNaAttributes.keySet()) {
			failMessage= failMessage + " " + tempClassname +" updated " + resultMapClassNamesAndReceivedNaAttributes.get(tempClassname);
		}
		
		if ( match) { 	
        	logger.info("test {} failed", this.getClass().getName());
			throw new TcFailed("Failed because: ..."+ failMessage);
        } else {
        	logger.info("test {} passed", this.getClass().getName());	        	
        }
  
	}

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
		 logger.info("postamble action for test {}", this.getClass().getName());
	        try {
	            rtiAmbassador.resignFederationExecution(ResignAction.NO_ACTION);
				rtiAmbassador.disconnect();
	        } catch (InvalidResignAction | OwnershipAcquisitionPending | FederateOwnsAttributes | FederateNotExecutionMember
	                | NotConnected | CallNotAllowedFromWithinCallback | RTIinternalError | FederateIsExecutionMember e) {
	            throw new TcInconclusiveIf(e.getMessage());
	        }
	}
}
