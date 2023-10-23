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
import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
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
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NotConnected;
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
 * first step is to subscribe to these to verify Attributes in performTest
 * ( the SUT should to publish some attributes to see results of this test ! --> Aircraft App)
 * (let the task PerformTest run for a while)
 * 
 * 2) then we have to organize to receive Information about the subscribed Classes over discoverObjectInstances 
 *  to debug, let's see there,  what are the Classnames of receiced Informations
 *  
 * 3) store the received ObjectInstanceHandles with their ObjectclassHandles in a Map to use it
 *    later  in reflectAttributeValues
 *  
 *   ....  see 0012
 *   
 */

public class TC_IR_RPR2_PHY_0004 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    Platform toTestPlatform;
    Platform[] platformWorkList;   
    
    HashMap<ObjectInstanceHandle, ObjectClassHandle> objectInstanceHandlesAndobjectClassHandle = new HashMap<>();
   
    // HashMap with classnames as key and an array with non-applicable Platform Attributes for this class
    // to allocate the objectclassnames with a List of  non-applicable Platform Attributes for this Physical Entity
    HashMap<String, String[]> classNamesAndNaAttributList = new HashMap<String, String[]>();
    
    
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
                logger.debug("# discoverObjectInstance:reveived ObjectClassHandle with rti-ObjectClassName: "+ receivedClassName +"\n"); 
                //  End of Tests and Debug
                
                // to refer later the attributvaluemaps to the correct ObjectClasses, we need to store somehow 
                // the ObjectInstanceHandle with the objectClassHandle or objectclassnames:
            } catch (Exception e) {
                logger.error("discoverObjectInstance received Exception", e);
            } 
            
            // store the received ObjectInstanceHandles with their ObjectclassHandles in a Map to use it later in reflectAttributeValues
            objectInstanceHandlesAndobjectClassHandle.put(theObjectInstanceH,theObjectClassH);
	        
	    }
	    
	    
		// public void reflectAttributeValues() {
		// complete if needed
		// }

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
      
        
     try {
         toTestPlatform = new Platform();
         //platformWorkList = new Platform[] { new Aircraft(), new AmphibiousVehicle() };
         platformWorkList = new Platform[] {new Aircraft() , new AmphibiousVehicle(), new GroundVehicle(), new  Spacecraft() ,
                           new SurfaceVessel() , new SubmersibleVessel(), new MultiDomainPlatform() };
        
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
             logger.debug(
                     "# in performTest subscribing all Elements of platformWorkList now " + p.getHlaClassName()); // Debug
             p.subscribe();
         }
         
         // test the classnames       Debug  remove later  (brf)
         logger.debug("----------  let's have a look to the classnames in toTestPlatform: ");
         for (Platform p : platformWorkList) {
             logger.debug (p.getHlaClassName() ) ;
         }
         logger.debug("----------------------");
         
        
         // ################################
         
      // the Test ......
         for (int i = 0; i < 10; i++) { // TODO change this to a better mechanism
             
             
             Thread.sleep(1000);
             logger.debug("TC_IR_RPR2_PHY_004 Perform Test cycle:  " +i);
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
