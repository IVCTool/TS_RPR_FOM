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
 */


public class TC_IR_RPR2_0012 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Logger logger = null;
	
	Semaphore semaphore = new Semaphore(0);
	
	HashMap<ObjectInstanceHandle,AttributeHandleValueMap> known_oInstance_AttrValueMap = new HashMap<>();
	
	HashMap<AttributeHandle,byte[]> _attributeHandleValues  = new HashMap<>() ;
	
	ObjectClassHandle temp_objectClassHandle;
	HashMap<ObjectInstanceHandle, PhysicalEntity> knownPhysicalEntitys = new HashMap<>();
	
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
			
			System.out.println(""); // Debug
			logger.trace("discoverObjectInstance {}", theObject);
			// semaphore.release(1);

			try {
				// we only want to observe selected ObjectInstances
				if (toTestPlatform.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClass))) {

					temp_objectClassHandle = theObjectClass;
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
			
			System.out.println(""); // Debug
			logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
			//semaphore.release(1);
			
			try {
			
			// we have to store the incoming Information to analyse it later
			System.out.println("# ---------   Testing and Debugging --------------------------------"); 	
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
		    
			System.out.println("\n# reflectAttributeValues: getHlaClassName der zu bearbeitenden Klasse   \t" + toTestPlatform.getHlaClassName()); // Debug			
			System.out.println("# reflectAttributeValues: rti-ObjectKlassenName of with discoverObjectInstance received temp_objectClassHandle \t" + rtiAmbassador.getObjectClassName(temp_objectClassHandle));  // Debug

			// testing some other
			//rtiAmbassador.get
			
			
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
		
		PhysicalEntity.initialize(rtiAmbassador);

		try {
			switch (toTestPlatformName){
			  case "Aircraft": 
				  toTestPlatform = new Aircraft();
			   //Anweisung1   toTestEntity
			    break;			   
			  case "AmphibiousVehicle":
				  //toTestPlatform = new AmphibiousVehicle();
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
			
			// aircraft = new Aircraft();    //to adjust
			// aircraft.addSubscribe(BaseEntity.Attributes.EntityIdentifier);   //to adjust
			// aircraft.subscribe();         //to adjust
			
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
				
				//
				// the Test ......
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
