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
import hla.rti1516e.NullFederateAmbassador;
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
 * first step is to subscribe to these attributes
 *  ########################
 *   ....  see 0012
 *   
 */

public class TC_IR_RPR2_PHY_0004 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    Platform toTestPlatform;
    Platform[] platformWorkList;
    
    
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
    
   
    // HashMap with classnames as key and an array with non-applicable Platform Attributes for this class
    // to allocate the objectclassnames with a List of  non-applicable Platform Attributes for this Physical Entity
    HashMap<String, String[]> classNamesAndNaAttributList = new HashMap<String, String[]>();
    
    // ##################################################
    
    
    
	class TestCaseAmbassador extends NullFederateAmbassador {

	    // The SUT set Attributes, so we "hear" here on the 
		// public void discoverObjectInstance() {
		// complete if needed
		// }

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
        
         // ################################
        
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
