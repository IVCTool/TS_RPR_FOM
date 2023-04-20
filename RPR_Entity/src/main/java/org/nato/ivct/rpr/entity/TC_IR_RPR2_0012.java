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
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidAttributeHandle;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
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

/*  * Table 7 Domain Appropriateness for PhysicalEntity Attributes 
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
 *  <name>FirePowerDisabled</name>    <dataType>RPRboolean</dataType>    
 *  <name>IsConcealed</name>          <dataType>RPRboolean</dataType>     
 *  <name>TentDeployed</name>         <dataType>RPRboolean</dataType>
 *  
 *  further Attributes
 *  ...
 *  <name>EngineSmokeOn</name>        <dataType>RPRboolean</dataType>
 *  <name>FlamesPresent</name>        <dataType>RPRboolean</dataType>
 *  ...
 * 
 *  perhaps we need not to decode,  just see if they are updated 
 *     
 *  so we have to check if the Classname of the Attributes received by reflectAttributeValues 
 *  are the same as the testclass
 *    
 *  then get the attributes of the List and check if it's one of the 'special' Attributes *    
 *    Aircraft              FirePowerDisabled  IsConcealed    TentDeployed     
 *    AmphibiousVehicle      -----
 *    GroundVehicle          ----- 
 *    Spacecraft             EngineSmokeON FirePowerDisabled  IsConcealed  TentDeployed  TrailingEffectsCode
 *    SurfaceVessel          FirePowerDisabled   IsConcealed  TentDeployed
 *    SubmersibleVessel      EngineSmokeON FirePowerDisabled  IsConcealed  TentDeployed  TrailingEffectsCode
 *    ...  
 *    
 *    
 *     
 */


public class TC_IR_RPR2_0012 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Logger logger = null;
	
	//Semaphore semaphore = new Semaphore(0);
	
	//HashMap<ObjectInstanceHandle,AttributeHandleValueMap> known_Instance_AttrValueMap = new HashMap<>();	
	//HashMap<AttributeHandle,byte[]> _attributeHandleValues  = new HashMap<>() ;	
	//HashMap<String ,  ObjectInstanceHandle> knownObjectInstanceHandles = new HashMap<>();
	
	ObjectClassHandle temp_objectClassHandle = null;
	ArrayList<String> attributNames = new ArrayList<String>();   
	
	
    //  todo   complete  for all Physical Entities
	String[] naListAircraft = new String[] {"FirePowerDisabled" , "IsConcealed" , "TentDeployed"};	
	String[] naListAmphibiousVehicle = new String[] {"-----------"};	
	String[] naListGroundVehicle = new String[] {"--------------"};	
	String[] naListSpacecraft = new String[] {"EngineSmokeON" , "FirePowerDisabled" , "IsConcealed" , "TentDeployed" ,"TrailingEffectsCode"};
	String[] naListSurfaceVessel = new String[] {"FirePowerDisabled" , "IsConcealed" , "TentDeployed"};
	// to be completet
	
		
	String toTestPlatformName="";
	Platform toTestPlatform;
	//Aircraft aircraft;
	
	
    public TC_IR_RPR2_0012() {
		
	}
	
    public TC_IR_RPR2_0012(String  _testPlatformNameString) {
    	toTestPlatformName=_testPlatformNameString;
	}
    

	class TestCaseAmbassador extends NullFederateAmbassador {
		
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject,
				ObjectClassHandle theObjectClass,
				String objectName) throws FederateInternalError {
			
			logger.info(""); // Debug
			logger.trace("discoverObjectInstance {}", theObject);
			// semaphore.release(1);

			try {
				// we only want to observe selected ObjectInstances  eg. aircraft
				if (toTestPlatform.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClass))) {

					temp_objectClassHandle = theObjectClass;     // Debug 
					
					//knownObjectInstanceHandles.put(rtiAmbassador.getObjectClassName(theObjectClass) , theObject) ;
					
					logger.info("rti-ObjectClassName in discoverObjectInstance:  " + rtiAmbassador.getObjectClassName(theObjectClass)); // Debug
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
        public void reflectAttributeValues(ObjectInstanceHandle theObject,
        		AttributeHandleValueMap theAttributes,
                byte[] userSuppliedTag,
                OrderType sentOrdering,
                TransportationTypeHandle theTransport,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
			
			logger.info(""); // Debug
			logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
			//semaphore.release(1);
			
			try {
			
			// we have to store the incoming Information to analyse it later
			logger.info("\n# ---------   Testing and Debugging --------------------------------"); 
			logger.info("# reflectAttributeValues: Amount of transmitted  attributes: " +theAttributes.size());	
		    logger.info("# reflectAttributeValues: got  ObjectInstanceHandle  theObject: " +theObject ); // Debug			    
		    logger.info("# reflectAttributeValues: rti-objectInstanceName of theObject: " + rtiAmbassador.getObjectInstanceName(theObject));  // Debug
		    
		    logger.info("\n# reflectAttributeValues:   AttributHandleValueMap theAttributes:  "+ theAttributes);  // Debug
		    logger.info("#reflectAttributeValues:  the Keys in AttributHandleValueMap  : " + theAttributes.keySet() );  
		    
		    // we need the Names of the Attributes 
		 	logger.info("# reflectAttributeValues: Names of received Attributes ");
		 		for ( AttributeHandle a : theAttributes.keySet() ) {
		 			logger.info(rtiAmbassador.getAttributeName(temp_objectClassHandle, a) );
		 		}
		    
		 	// this will be changed  the Name of the "toTestPlatform should be obtained directly from the class 
			logger.info("# reflectAttributeValues: getHlaClassName toBe tested Entity \t" + toTestPlatform.getHlaClassName()); // Debug			
			logger.info("# reflectAttributeValues: rti-ObjectKlassenName of with discoverObjectInstance received temp_objectClassHandle \t" + rtiAmbassador.getObjectClassName(temp_objectClassHandle));  // Debug
			logger.info("\n# ---------  End of Testing  --------------------------------"); 
			
			
			// now we store the names of received Attributes
			if (toTestPlatform.getHlaClassName().equals(rtiAmbassador.getObjectClassName(temp_objectClassHandle))) {					
				logger.info("# class names are equal, do here something"); // Debug
				
				for ( AttributeHandle a : theAttributes.keySet() ) {		 			
		 			 String tempAttributname = rtiAmbassador.getAttributeName(temp_objectClassHandle, a);
		 			
		 			if (! attributNames.contains(tempAttributname)) {
		 				attributNames.add(tempAttributname);
		 			}		 			
		 		}
				
			}
			
			
			// so we might get Attributes for different ObjectClasses.
			
			/* we no need a  Map or someting else and store in it the different
			*   ObjectClassNames with a List of non-applicable PhysicalEntity Attributes
			*   like the Arrays obove eg. naListAircraft
			*/
			
			// only temporary at this place 
			HashMap<String, String[]> DomAppPhysEntAttributes = new HashMap<String, String[]>() ;
			
			DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Aircraft", naListAircraft);
			DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.AmphibiousVehicle", naListAmphibiousVehicle);
			DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.GroundVehicle", naListGroundVehicle);
			DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Spacecraft", naListSpacecraft);
			DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel", naListSurfaceVessel);
			//DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SubmersibleVessel", naListSubmersibleVessel);
			
			// now check if the received ObjectClassName  is in the List .... (rtiAmbassador.getObjectClassName(temp_objectClassHandle))
			// ....
			
			
			//  only temporary at this place
			
			if (DomAppPhysEntAttributes.containsKey(rtiAmbassador.getObjectClassName(temp_objectClassHandle))) {
				logger.info("# DomAppPhysEntAttributes  contains "
						+ rtiAmbassador.getObjectClassName(temp_objectClassHandle)); // Debug

				String[] nonAppAttributes = DomAppPhysEntAttributes
						.get(rtiAmbassador.getObjectClassName(temp_objectClassHandle));

				for (String naAttribute : nonAppAttributes) {

					for (String atName : attributNames) {

						if (atName.equals(naAttribute)) {
							logger.warn("###### Test temporairly in reflectAttributeValues failed  "+ atName +" equals " +naAttribute  );

						}
					}
				}
			}
	
			
			} catch ( FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined | InvalidAttributeHandle | InvalidObjectClassHandle | ObjectInstanceNotKnown  e) 
			{ 	};
			
			// (ObjectInstanceNotKnown | FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined | InvalidAttributeHandle | InvalidObjectClassHandle    e
			
			
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
			ArrayList<URL> fomList = new FomFiles()
					.addRPR_BASE()
					.addRPR_Enumerations()
					.addRPR_Foundation()
					.addRPR_Physical()
					.addRPR_Switches()
					.get();

			rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(federationName, fomList.toArray(new URL[fomList.size()]));
			} catch (FederationExecutionAlreadyExists ignored) { }

			rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName,
					fomList.toArray(new URL[fomList.size()]));

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
		//Aircraft.initialize(rtiAmbassador);  //to adjust
		
		Platform.initialize(rtiAmbassador);

		try {
			switch (toTestPlatformName){
			  case "Aircraft": 
				  toTestPlatform = new Aircraft();
			   //Anweisung1   toTestEntity
			    break;			   
			  case "AmphibiousVehicle":
				  toTestPlatform = new AmphibiousVehicle();
			    break;			    
			  case "GroundVehicle":
				  //toTestPlatform = new GroundVehicle();
				    break;				    
			  case "Spacecraft":
				  //toTestPlatform = new Spacecraft();
				    break;				    
			  default :
				  logger.info(" to Test Type  unknown, we assume Aircraft ");
				  toTestPlatform = new Aircraft();
			}
			
			
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
			
			toTestPlatform.subscribe();

			boolean seenEnough = false;
			while (!seenEnough) {		

				// the Test ......

				// We can get here the Names of the received Attributes.
				for (int i = 0; i < 100; i++) {
					logger.info("#### performTest: Names of stored Attributes "); // Debug
					logger.info("toString of received Attributes gives: " + attributNames); // Debug
					for (String s : attributNames) {
						logger.info(s);
					}
					
				//-----------------------------------------------------					
					
					// So now we have to test which Platform should not sent which Attributes

					// test this here and then add to the obove switch ....
					String[] nonApTestList;
					switch (toTestPlatformName) {
					case "Aircraft":
						nonApTestList = naListAircraft;
						break;
					case "AmphibiousVehicle":
						nonApTestList = naListAmphibiousVehicle;
						break;
					case "GroundVehicle":
						nonApTestList = naListGroundVehicle;
						break;
					case "Spacecraft":
						nonApTestList = naListSpacecraft;
						break;
					default:
						logger.info(" non-applicable TestList unknown, we assume Aircraft ");
						nonApTestList = naListAircraft;
					}
					
					for (String nonAppAttribut : nonApTestList ) {
						
						for (String s : attributNames) {
							logger.info("AttributName of received Attributs is now: " + s);
							logger.info("non-applicable PhysicalEntity Attribute is now "+nonAppAttribut);							
							logger.info("---------------------------------------------------------\n");
							
							// Test if there is a match
							
							if (s.equals(nonAppAttribut)) {
								logger.info("We have a match off received Attributs " + s+ " with non-applicable Atttibut List " + nonAppAttribut);
								logger.info("AttributName of received Attributs is now: " + s);
								logger.warn("################## Test failed ###################### ");
								
							}
							
						}
						
					}
			  //---------------------------------------------------------
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					Thread.sleep(1000);
				}

				
				
				
				
				
			}
			
			

		} catch (Exception e) {
			throw new TcInconclusiveIf(e.getMessage());
		}

		logger.info("test {} passed", this.getClass().getName());
	}
	

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
		// TODO Auto-generated method stub

	}

}
