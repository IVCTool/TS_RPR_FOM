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
 *    ---------------- additional Information 
 *     Plattform :                       Table 8    16 Attributes     all Optional *  
 *     Aircraft              attributless
 *     AmphibiousVehicle    attributless
 *     GroundVehicle        attributless
 *     MultiDomainPlatform  attributless
 *     Spacecraft            attributless
 *     SubmersibleVessel     attributless
 *     SurfaceVessel         attributless
 *     ---------------------------------------
 *    
 *    
 *    First we write the Name of in discoverObjectInstance  received  ObjectClassHandle in a variable, ( later in a map )
 *    
 *    further we write the names of all attributes in a ArrayList "listOfReceivedAttributNames"
 *    
 *    we have a table classNamesAndNaAttributList with to verify Classnames  as key and an String-Array of non-applicable Attributenames.
 *    
 *    in the test we take the name of the received ObjectClassHandle,
 *    get from the List classNamesAndNaAttributList  one after the other attributname  which is not applicable
 *    and if this is received with the objectClassName  the Test failed  
 *         
 */


public class TC_IR_RPR2_0012 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Logger logger = null;
	
	//Semaphore semaphore = new Semaphore(0);
	
	//HashMap<ObjectInstanceHandle,AttributeHandleValueMap> known_Instance_AttrValueMap = new HashMap<>();	
	//HashMap<AttributeHandle,byte[]> _attributeHandleValues  = new HashMap<>() ;	
	
	
	HashMap<String ,  ObjectInstanceHandle> objectClassNamesAndInstanceHandles = new HashMap<>();
	HashMap<String , ArrayList > objectClassNamesAndReceivedAttributeList = new HashMap<>(); 
	
	HashMap<ObjectInstanceHandle , ObjectClassHandle > objectInstanceHandlesAndobjectClassHandle = new HashMap<>();
	ObjectClassHandle temp_objectClassHandle = null;   // to be replaced
	
	// List of received Attributes
	ArrayList<String> listOfReceivedAttributNames = new ArrayList<String>();   	
	String toTestPlatformName="";
	Platform toTestPlatform;
	//Aircraft aircraft;
	Platform[] platformWorkList;
	
    //  todo   complete  for all Physical Entities
	String[] naListAircraft = new String[] {"FirePowerDisabled" , "IsConcealed" , "TentDeployed"};	
	String[] naListAmphibiousVehicle = new String[] {"-----------"};	
	String[] naListGroundVehicle = new String[] {"--------------"};	
	String[] naListSpacecraft = new String[] {"EngineSmokeON" , "FirePowerDisabled" , "IsConcealed" , "TentDeployed" ,"TrailingEffectsCode"};
	String[] naListSurfaceVessel = new String[] {"FirePowerDisabled" , "IsConcealed" , "TentDeployed"};
	// to be completet
	
	// HashMap  with classnames as key  and an array with non-applicable PhysicalEntity Attributes for this class
	HashMap<String, String[]> classNamesAndNaAttributList = new HashMap<String, String[]>() ;
	
	
    public TC_IR_RPR2_0012() {
		
	}
	
    public TC_IR_RPR2_0012(String  _testPlatformNameString) {
    	toTestPlatformName=_testPlatformNameString;
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
			
			
			//objectInstanceHandlesAndobjectClassNames
			
			try {
				// we only want to observe selected Classes e.g. platform	
				// checking for each element in the List of to examine platforms
				for (Platform p : platformWorkList) {
					if ( p.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClassH) ) ) {						
						logger.info("# discoverObjectInstance  given rti-ObjectClassName:  " + rtiAmbassador.getObjectClassName(theObjectClassH)); // Debug

						// temp_objectClassHandle should later be in a map ?
						temp_objectClassHandle = theObjectClassH;     // to be changed								
						
						if (objectInstanceHandlesAndobjectClassHandle.get(theObjectInstanceH)==null ) {							
							objectInstanceHandlesAndobjectClassHandle.put(theObjectInstanceH, theObjectClassH);							
						}
						
						
						// we store a List of ObjectClassNames with their ObjectInstanceHandles in objectClassNamesAndInstanceHandles  
						objectClassNamesAndInstanceHandles.put(rtiAmbassador.getObjectClassName(theObjectClassH) , theObjectInstanceH) ;  // ????
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
			logger.info("\n# ---------   Testing and Debugging --------------------------------"); 
			logger.info("# reflectAttributeValues: Amount of transmitted  attributes: " +theAttributes.size());	
		    logger.info("# reflectAttributeValues: got  ObjectInstanceHandle  theObject: " +theObjectInstanceH ); // Debug			    
		    logger.info("# reflectAttributeValues: rti-objectInstanceName of theObject: " + rtiAmbassador.getObjectInstanceName(theObjectInstanceH));  // Debug
		    logger.info("# reflectAttributeValues: rti-ObjectClassenName of with discoverObjectInstance received temp_objectClassHandle \t" + rtiAmbassador.getObjectClassName(temp_objectClassHandle));  // Debug
		    
		    logger.info("# reflectAttributeValues:  AttributHandleValueMap \"theAttributes\":  "+ theAttributes);  // Debug
		    logger.info("# reflectAttributeValues:  the Keys in AttributHandleValueMap  : " + theAttributes.keySet() );  		    
		 	logger.info("# reflectAttributeValues: Names of received Attributes ");
		 	 
		 	    // too be changed  temp_objectClassHandle  should not be used
		 		for ( AttributeHandle a : theAttributes.keySet() ) {
		 			logger.info(rtiAmbassador.getAttributeName(temp_objectClassHandle, a) );
		 		}
		    
		 	// to be changed  , the Name of the "toTestPlatform should be obtained directly from the class 
			//logger.info("# reflectAttributeValues: getHlaClassName toBe tested Entity \t" + toTestPlatform.getHlaClassName()); // Debug	
			logger.info("\n# ---------  End of Testing  --------------------------------"); 
			
			// add all received attributes to a List			
			
			// get the ClassHandle for the class with this objectInstanceHandle
			ObjectClassHandle  tempLocalObjectClasshandle = objectInstanceHandlesAndobjectClassHandle.get(theObjectInstanceH);
			
			// get the objectClassname 			
			String tempLocalObjectClassname = rtiAmbassador.getObjectClassName(tempLocalObjectClasshandle);
			
			// use a Map to store attributes with a ObjectClassname	
			ArrayList<String> localListOfReceivedAttributNames = new ArrayList<String>(); 
			
			// store the names of  received attributes in a List
			for ( AttributeHandle a : theAttributes.keySet() ) {		 			
	 			 String tempAttributname = rtiAmbassador.getAttributeName(tempLocalObjectClasshandle, a);		 			
	 			if (! localListOfReceivedAttributNames.contains(tempAttributname)) {
	 				localListOfReceivedAttributNames.add(tempAttributname);
	 			}		 			
	 		}
			
			//                may be better store this in a map ObjectInstanceHandle and attributeList ??
			// now we store a map ObjectClassNamesAndAttributeList   ObjectclassNames with a List of attributnames
			
			 // we know that the tempLocalObjectClassname  is corect for the  received ObjectInstanceHandle
			 // so we store all alltributes with this classname
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
		    	logger.info("# in performTest subscribing all Elements of platformWorkList now "  +p.getHlaClassName() );
		    	p.subscribe();
		    }
			
			// toDo complete
			/* we now need a  Map or someting else and store in it the different
			*   ObjectClassNames with a List of non-applicable PhysicalEntity Attributes like the Arrays obove eg. naListAircraft
			*/	
			// fill the table  which Attributes should not be updatet for a specific Platform
			classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Aircraft", naListAircraft);
			classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.AmphibiousVehicle", naListAmphibiousVehicle);
			classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.GroundVehicle", naListGroundVehicle);
			classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Spacecraft", naListSpacecraft);
			classNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel", naListSurfaceVessel);
			//DomAppPhysEntAttributes.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SubmersibleVessel", naListSubmersibleVessel);
			
			

			boolean seenEnough = false;
			while (!seenEnough) {		
				// the Test ......

				// We can get here the Names of the received Attributes.
				for (int i = 0; i < 10; i++) {
					logger.info("# performTest: Names of stored Attributes:  "); // Debug
					logger.info("# performTest: toString of received Attributes gives: " + listOfReceivedAttributNames); // Debug
					for (String s : listOfReceivedAttributNames) {
						logger.info(s);
					}
					
					// we might get Attributes for different ObjectClasses,  check if the received ObjectClassName is in the List ....	
					
					//check  objectClassNamesAndReceivedAttributeList  against a List with non applicable attributes for this class
					
					for (String ObjectClassname : objectClassNamesAndReceivedAttributeList.keySet() ) {
						
						// for  this classname  the List with non applicable atttibutes 
						String[] tempNonApplicableAttributList = classNamesAndNaAttributList.get(ObjectClassname);
						
						// for this classname received attributes ( from the in reflectAttributesvalues created ArrayList)
						ArrayList<String> tempReceivedAttributList = objectClassNamesAndReceivedAttributeList.get(ObjectClassname);						
						
						// for every  for/with this classname received attribut 
						for (String receivedAttribut :  tempReceivedAttributList)  {
							
							// fuer jedes Attribut aus den fuer dieses Object non applicable attributes
							for (String nonAppAttribute : tempNonApplicableAttributList ) {
								
								if (nonAppAttribute.equals(receivedAttribut)) {
									
									logger.warn("###  test failed  "+ObjectClassname+ " received  "+ receivedAttribut  );									
								}								
							}				
							
						}
						
					}
		
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
