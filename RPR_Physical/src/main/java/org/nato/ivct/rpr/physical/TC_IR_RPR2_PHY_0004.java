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
import java.util.ArrayList;
import java.util.HashMap;

import org.nato.ivct.rpr.FomFiles;

import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.AmphibiousVehicle;
import org.nato.ivct.rpr.objects.GroundVehicle;
import org.nato.ivct.rpr.objects.MultiDomainPlatform;
import org.nato.ivct.rpr.objects.Platform;
import org.nato.ivct.rpr.objects.Spacecraft;
import org.nato.ivct.rpr.objects.SubmersibleVessel;
import org.nato.ivct.rpr.objects.SurfaceVessel;
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
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;



/**
 * Interoperability Requirement : IR-RPR-PHY-0004:
 * 
 * SuT federates updating instance attributes of one of these object classes shall limit
 * use to those object classes indicated by a «yes» in Table 9,
 * when indicated restricted to the enumerators listed.
 */

 /* This Test may be similar to IR-RPR2-0012
 * 
 * so we have to  List all non-applicable  Attributes as listed in
 * "Table 9 Domain Appropriateness for Platform Attributes"
 * 
 *   AfterburnerOn, 
 *   AntiCollisionLightsOn,
 *   BlackOutBrakeLightsOn,
 *   BlackOutLightsOn,
 *   BrakeLightsOn,
 *   FormationLightsOn,
 *   HatchState,
 *   HeadLightsOn,
 *   InteriorLightsOn,
 *   LandingLightsOn,
 *   LauncherRaised,
 *   NavigationLightsOn,
 *   RampDeployed,
 *   RunningLightsOn,
 *   SpotLightsOn,
 *   TailLightsOn
 * 
 * are they applicable ? for
 *  Aircraft  AmphibiousVehicle  GroundVehicle Spacecraft  SurfaceVessel  SubmersibleVessel MultiDomainPlatform
 * 
 * so we have to test if special Attributes will be updated for Aircraft eg.
 *( we need not to decode, just see if they are updated )
 *
 *
 *  As Preparation  we  create ArrayLists with  non-applicable Attributes for every to test Object-Class
 *  eg "String[] naListAircraft "  with the names of non-applicable Attributes as Elements
 *  
 *  And a HashMap which containes these ArrayList organized by the Object-Class-Names as keys
 *   "classNamesAndNaAttributList"
 *
 * 1) first step is to subscribe to these to verify Attributes in performTest
 * ( the SUT should to publish some attributes to see results of this test ! --> Aircraft App)
 * (let the task PerformTest run for a while)
 * 
 * 2) then we have to organize to receive Information about the subscribed Classes over discoverObjectInstances 
 *  (to debug, let's see there,  what are the Classnames of receiced Informations)
 *  
 * 3) in discoverObjectInstance, store the received ObjectInstanceHandles with their ObjectclassHandles
 *    in a Map to use it  later  in reflectAttributeValues (objectInstanceHandlesAndobjectClassHandle)
 *    
 * 4) prepare reflectAttributeValues to  receive the ObjectInstanceHandle and attached AttributeHandleValueMaps
 * 
 *    get here the to the ObjectInstanceHandle corresponding ObjectClassHandle out of the Map filled in discoverObjectInstance
 *    objectInstanceHandlesAndobjectClassHandle
 *    
 *    get with this ObjectClassHandle over the RTIAmbassador for every attributhandle in AttributHandleValueMap
 *    the Attributname   and 
 *    store it in a arrayList  localListOfReceivedAttributNames
 *    
 *    get the ObjectClassName for this ObjectClassHandle
 *    
 *    Store the arrayList localListOfReceivedAttributNames with the ObjectClassName as Key
 *     in a HashMap objectClassNamesAndReceivedAttributeList
 *     
 *  5) Now we have 2 Lists,   "classNamesAndNaAttributList" and "objectClassNamesAndReceivedAttributeList"
 *     both have ObjectClassNames as keys and  AttributNames  as value
 *     
 *  6) in the  test (perfomTest) we have now to match these two lists,
 *     HashMap<String, ArrayList> objectClassNamesAndReceivedAttributeList = new HashMap<>();
 *     HashMap<String, String[]> objectClassNamesAndNaAttributList = new HashMap<String, String[]>();
 *      
 *     is there a Attribut send for a ObjectClass, which is in the List of non-applicable Attributes for this Object-Class
 *     
 *  
 */

public class TC_IR_RPR2_PHY_0004 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    Platform toTestPlatform;
    Platform[] platformWorkList;   
    
    HashMap<ObjectInstanceHandle, ObjectClassHandle> objectInstanceHandlesAndobjectClassHandle = new HashMap<>();
    
    HashMap<String, ArrayList> objectClassNamesAndReceivedAttributeList = new HashMap<>();
   
    // HashMap with classnames as key and an array with non-applicable Platform Attributes for this class
    // to allocate the objectclassnames with a List of  non-applicable Platform Attributes for this Physical Entity
    HashMap<String, String[]> objectClassNamesAndNaAttributList = new HashMap<String, String[]>();
    
    
  //arrays with non-applicable Platform Attribute-names for earch Platform as shown in Table 9
    String[] naListAircraft = new String[] { "BlackOutBrakeLightsOn", "BlackOutLightsOn", "BrakeLightsOn", "HatchState",
                              "HeadLightsOn","LauncherRaised", "RampDeployed", "RunningLightsOn", "TailLightsOn"};
  
    String[] naListAmphibiousVehicle = new String[] {"AfterburnerOn", "AntiCollisionLightsOn", "FormationLightsOn",
                              "LandingLightsOn", "NavigationLightsOn" };
    
    String[] naListGroundVehicle = new String[] {"AfterburnerOn", "AntiCollisionLightsOn", "FormationLightsOn" ,
                              "LandingLightsOn", "NavigationLightsOn", "RunningLightsOn" };    

    String[] naListSpacecraft = new String[] {"AfterburnerOn", "AntiCollisionLightsOn", "BlackOutBrakeLightsOn",
                              "BlackOutLightsOn",  "BrakeLightsOn", "FormationLightsOn", "HatchState", "HeadLightsOn",
                              "InteriorLightsOn", "LandingLightsOn", "LauncherRaised",  "NavigationLightsOn",
                              "RampDeployed", "RunningLightsOn", "SpotLightsOn", "TailLightsOn" };
  
    String[] naListSurfaceVessel = new String[] {"AfterburnerOn", "AntiCollisionLightsOn", "BlackOutBrakeLightsOn",
                              "BlackOutLightsOn",  "BrakeLightsOn", "FormationLightsOn", "HatchState", "HeadLightsOn",
                              "LandingLightsOn", "LauncherRaised",  "NavigationLightsOn", "RampDeployed", "TailLightsOn" }; 
    
    String[] naListSubmersibleVessel = new String[] {"AfterburnerOn", "AntiCollisionLightsOn", "BlackOutBrakeLightsOn",
                             "BlackOutLightsOn",  "BrakeLightsOn", "FormationLightsOn", "HeadLightsOn",
                             "InteriorLightsOn", "LandingLightsOn", "LauncherRaised",  "NavigationLightsOn",
                             "RampDeployed",  "SpotLightsOn", "TailLightsOn" };
    
    String[] naListMultiDomainPlatform = new String[] { "--------------" };
    
   
    public TC_IR_RPR2_PHY_0004() {
        // TODO  put the naList* to a Map organized by the Classnames 
        // fill the Map of classnames and non-applicable PhysicalEntity Attributes
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Aircraft", naListAircraft);
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.AmphibiousVehicle", naListAmphibiousVehicle);
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.GroundVehicle", naListGroundVehicle);
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Spacecraft", naListSpacecraft);
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel", naListSurfaceVessel);    
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SubmersibleVessel", naListSubmersibleVessel);
        objectClassNamesAndNaAttributList.put("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.MultiDomainPlatform", naListMultiDomainPlatform);
    }
    
    
	class TestCaseAmbassador extends NullFederateAmbassador {
	    
	    @Override
	    public void discoverObjectInstance(
	            ObjectInstanceHandle theObjectInstanceH,
	            ObjectClassHandle theObjectClassH,
	            String objectName,
	            FederateHandle producingFederate)
	            throws FederateInternalError {
	        logger.trace("discoverObjectInstance {} with producingFederate {}", theObjectInstanceH, producingFederate);
	         logger.info("# discoverObjectInstance with FederateHandle ");  
	        discoverObjectInstance(theObjectInstanceH, theObjectClassH, objectName);
	    }
	    
	    public void discoverObjectInstance(
	            ObjectInstanceHandle theObjectInstanceH,
	            ObjectClassHandle theObjectClassH,
	            String objectName)
	            throws FederateInternalError {
	        logger.trace("discoverObjectInstance {}", theObjectInstanceH);
	        logger.debug("### discoverObjectInstance without FederateHandle ");
	        
            try {
                // some Tests and Debug
                String receivedClassName = rtiAmbassador.getObjectClassName(theObjectClassH);
                logger.debug("## discoverObjectInstance:reveived ObjectClassHandle with rti-ObjectClassName: "+ receivedClassName +"\n"); 
                //  End of Tests and Debug
                
                // to refer later the attributvaluemaps to the correct ObjectClasses, we need to store somehow 
                // the ObjectInstanceHandle with the objectClassHandle or objectclassnames:
            } catch (Exception e) {
                logger.error("discoverObjectInstance received Exception", e);
            } 
            
            // store the received ObjectInstanceHandles with their ObjectclassHandles in a Map to use it later in reflectAttributeValues
            objectInstanceHandlesAndobjectClassHandle.put(theObjectInstanceH,theObjectClassH);
	        
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
	        logger.trace("reflectAttributeValues without  MessageRetractionHandle");
	        reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
	    }
	    
	    
	    @Override
	    public void reflectAttributeValues(ObjectInstanceHandle theObjectInstancH, AttributeHandleValueMap theAttributes,
	            byte[] userSuppliedTag,  OrderType sentOrdering, TransportationTypeHandle theTransport,
	            SupplementalReflectInfo reflectInfo)  throws FederateInternalError {
	            logger.trace("reflectAttributeValues without LogicalTime, receivedOrdering,  MessageRetractionHandle ");
	            
	        // we get the Names of the attributes only with the to the objectInstanceHandle correspondending ObjectClassHandle
	        ObjectClassHandle tempLocalObjectClasshandle = objectInstanceHandlesAndobjectClassHandle.get(theObjectInstancH);
	           
	        // let's store all Attributnames for a specific  ObjectInstanceHandle / ObjectClasshandle in a array
	        ArrayList<String> localListOfReceivedAttributNames = new ArrayList<String>();	        
	        try {         
	            
	       // get with this ObjectClassHandle over the RTIAmbassador for every attributhandle in AttributHandleValueMap
	       // the Attributname  and store it in a arrayList  localListOfReceivedAttributNames    
	        for (AttributeHandle a : theAttributes.keySet()) {
	           String tempAttributname = rtiAmbassador.getAttributeName(tempLocalObjectClasshandle, a);
	           if(!localListOfReceivedAttributNames.contains(tempAttributname)  ) {
	               localListOfReceivedAttributNames.add(tempAttributname);
	           }
	        }
	        
	        // get the ObjectClassName for this ObjectClassHandle
            String tempLocalObjectClassname = rtiAmbassador.getObjectClassName(tempLocalObjectClasshandle);
            logger.debug("## reflectAttributeValues: Name of ObjectClass from objectInstanceHandlesAndobjectClassHandle  " +tempLocalObjectClassname );
	        
            //Store the arrayList  localListOfReceivedAttributNames in a HashMap with the ObjectClassName as Key
            objectClassNamesAndReceivedAttributeList.put(tempLocalObjectClassname, localListOfReceivedAttributNames);
	        
	        } catch (FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined
                    | InvalidAttributeHandle | InvalidObjectClassHandle e) {
            } 
	        
	       //Debug  Test if there is any Attribut in the List objectClassNamesAndReceivedAttributeList
	        /*
            for ( String tempObjectClassname : objectClassNamesAndReceivedAttributeList.keySet() ) {
                ArrayList<String> tempReceivedAttributList  = objectClassNamesAndReceivedAttributeList.get(tempObjectClassname);                
                logger.debug("reflectAttributeValues: for the ObjectClass received Attributes:  " + tempObjectClassname);    // DEBUG         
                for (String tmpAttributname : tempReceivedAttributList) {
                    logger.debug(tmpAttributname);                                                                   // DEBUG    
                }
            }
            */
	        
	    }
	    	    
	}
    
	@Override
	protected void logTestPurpose(Logger logger) {
		
		logger.info("Test Case Purpose: \n"				
				+ " SuT federates updating instance attributes of one of these object classes \n "
				+ " shall limit use to those object classes indicated by a «yes» in Table 9,\n"
				+ " when indicated restricted to the enumerators listed. "); 
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
        
        Platform.initialize(rtiAmbassador);      
        boolean match = false;
        ArrayList<String> resultListReceivedNaAttributes = new ArrayList<String>();
        HashMap<String, ArrayList> resultMapClassNamesAndReceivedNaAttributes = new HashMap<String, ArrayList>();
        
        
     try {
         toTestPlatform = new Platform();
         platformWorkList = new Platform[] { new Aircraft(), new AmphibiousVehicle() };
         //platformWorkList = new Platform[] {new Aircraft() , new AmphibiousVehicle(), new GroundVehicle(), new  Spacecraft() ,
         //                  new SurfaceVessel() , new SubmersibleVessel(), new MultiDomainPlatform() };
        
         // first step is to subscribe to these to verify attributes
         toTestPlatform.addSubscribe(Platform.Attributes.AfterburnerOn);
         toTestPlatform.addSubscribe(Platform.Attributes.AntiCollisionLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.BlackOutBrakeLightsOn);         
         toTestPlatform.addSubscribe(Platform.Attributes.BlackOutLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.BrakeLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.FormationLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.HatchState);
         toTestPlatform.addSubscribe(Platform.Attributes.HeadLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.InteriorLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.LandingLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.LauncherRaised);
         toTestPlatform.addSubscribe(Platform.Attributes.NavigationLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.RampDeployed);
         toTestPlatform.addSubscribe(Platform.Attributes.RunningLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.SpotLightsOn);
         toTestPlatform.addSubscribe(Platform.Attributes.TailLightsOn);   
         
      // because toTestPlatform.subscribe(); doesnt match correct
         for (Platform p : platformWorkList) {
             p.subscribe();
         }
         
         // test the classnames       Debug  remove later  (brf)
         logger.debug("----------  let's have a look to the classnames in toTestPlatform: ");
         for (Platform p : platformWorkList) {
             logger.debug (p.getHlaClassName() ) ;
         }
         logger.debug("----------------------");
         
         
      // the Test ......         
         for (int i = 0; i < 10; i++) { // TODO change this to a better mechanism  
             
             // Debug and preparations for Testing
             /*
             for (ObjectInstanceHandle keyElement :  objectInstanceHandlesAndobjectClassHandle.keySet() ) {
                 ObjectClassHandle tempLocalObjectClasshandle = objectInstanceHandlesAndobjectClassHandle.get(keyElement);
                 String tempLocalObjectClassname = rtiAmbassador.getObjectClassName(tempLocalObjectClasshandle);                 
                 logger.debug("performTest: till now we got Informations from Class: "+tempLocalObjectClassname);                 
             }
             */
             
             //  Compare the two Lists if non-applicable attributes are updated from a special Object Class 
             //   objectClassNamesAndReceivedAttributeList
             //   objectClassNamesAndNaAttributList
             for ( String tempObjectClassname : objectClassNamesAndReceivedAttributeList.keySet() ) {
                 
                 logger.debug("performTest: for "+tempObjectClassname );
                 ArrayList<String> objectClassReceivedAttributList  = objectClassNamesAndReceivedAttributeList.get(tempObjectClassname);                 
                 String[] objectClassNonAppAttributList = objectClassNamesAndNaAttributList.get(tempObjectClassname);                 
                
                 for (String receivedAttribut : objectClassReceivedAttributList) {                     
                     for (String nonAppAttribut :  objectClassNonAppAttributList) {
                         
                         if ( receivedAttribut.equals(nonAppAttribut)) {                             
                             //logger.debug(" there is a match between receivedAttributList and nonApplicableAttributList: "+receivedAttribut+" : "+nonAppAttribut);
                             match = true; 
                             if (!resultListReceivedNaAttributes.contains(receivedAttribut)) {
                                 resultListReceivedNaAttributes.add(receivedAttribut);
                             }                                
                         }                       
                     } 
                 }                 
                 // building a List with classnames and received nonapplicable Attributes             
                 resultMapClassNamesAndReceivedNaAttributes.put(tempObjectClassname, resultListReceivedNaAttributes);                 
             }
                        
             Thread.sleep(1000);
             logger.debug("TC_IR_RPR2_PHY_004 Perform Test cycle:  " +i);
         }
         
         
        //   TODO  change this to a specifig  Exception 
        } catch (Exception e) {
            throw new TcInconclusiveIf("performTest received Exception: ",  e);
        }
     
        String failMessage = "";
        for (String tempClassname : resultMapClassNamesAndReceivedNaAttributes.keySet()) {
            failMessage = failMessage + " " + tempClassname + " updating  "
                    + resultMapClassNamesAndReceivedNaAttributes.get(tempClassname);
        }
     
        if (match) {
            logger.info("test {} failed", this.getClass().getName());
            throw new TcFailed("Failed because: ..." + failMessage);        
            
        } else {
            logger.info("test {} passed", this.getClass().getName());
        }
        
	}         // end of performTest
	
	

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
