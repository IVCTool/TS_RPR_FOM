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
import org.nato.ivct.rpr.datatypes.CamouflageEnum32;
import org.nato.ivct.rpr.datatypes.DamageStatusEnum32;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.ForceIdentifierEnum8;
import org.nato.ivct.rpr.datatypes.TrailingEffectsCodeEnum32;
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.HLAobjectRoot;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;
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
 * IR-RPR-PHY-0003:
 * 
 * All of the Plattform attributes shown in Table 8 in [SISO-STD-001-2015]
 * shall  be treated as optional fields for federates updating instance attributes
 *  of this object class or its subclasses.
 * 
 *   
 *  Platform   Attributes:  SISO-STD-001  P. 46   Table 8  25 attributes   all Optional
 *  Attribute Name                  Default Value                DataType
 *                                  all False                   all RPRboolean
 *                            exept HatchState NotApplicable
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
 */

public class TC_IR_RPR2_PHY_0003 extends AbstractTestCaseIf {
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    
    PhysicalEntity phyEntity;
    
    HashMap<String, Integer> setAttributeReport = new HashMap<>();
    
	class TestCaseAmbassador extends NullFederateAmbassador {

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
				+ " All of the Plattform attributes shown in Table 8 in [SISO-STD-001-2015] "
				+ " shall be treated as optional fields for federates \n"
				+ " updating instance attributes of this object class or its subclasses. "); 
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
         // only if we want to test to get Informations obout Objects and Attributes
         //phyEntity = new PhysicalEntity();
         //phyEntity.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);     
         //phyEntity.subscribe();
       
         Aircraft aircraft = new Aircraft();
         // for testing  we have to send all attributes from Platform entity   
         aircraft.addPublish(Platform.Attributes.AfterburnerOn);
         aircraft.addPublish(Platform.Attributes.AntiCollisionLightsOn);
         aircraft.addPublish(Platform.Attributes.BlackOutBrakeLightsOn);
         aircraft.addPublish(Platform.Attributes.BlackOutLightsOn);
         aircraft.addPublish(Platform.Attributes.BrakeLightsOn);

         aircraft.addPublish(Platform.Attributes.FormationLightsOn);
         aircraft.addPublish(Platform.Attributes.HatchState);
         aircraft.addPublish(Platform.Attributes.HeadLightsOn);
         aircraft.addPublish(Platform.Attributes.InteriorLightsOn);
         aircraft.addPublish(Platform.Attributes.LandingLightsOn);

         aircraft.addPublish(Platform.Attributes.LauncherRaised);
         aircraft.addPublish(Platform.Attributes.NavigationLightsOn);
         aircraft.addPublish(Platform.Attributes.RampDeployed);
         aircraft.addPublish(Platform.Attributes.RunningLightsOn);
         aircraft.addPublish(Platform.Attributes.SpotLightsOn);
         aircraft.addPublish(Platform.Attributes.TailLightsOn);
        
         aircraft.register();
 
         double rangeForTesting = 0.4;
        
        // Testing for n cycles (1000),  the duration is specified  in "Thread.sleep(10);" 
        for (int i = 0; i < 200; i++) { 
            logger.debug("# -------------------   performTest: cycle " +i +"---------------" );
                       
			// change the attribut values ocasionally
			aircraft.clear();

			// all elements has to be transformed to the Platform elements, all are
			// HLAboolean

			// AfterburnerOn
			if (Math.random() <= rangeForTesting) {
				aircraft.setAfterburnerOn(true);
				String randomTestName = "AfterburnerOn";
				logger.debug("performTest: random set of attributes AfterburnerOn");
				collectTestReport(randomTestName);
			}            
            
			// AntiCollisionLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setAntiCollisionLightsOn(true);
				String randomTestName = "AntiCollisionLightsOn";
				logger.debug("performTest: random set of attributes AntiCollisionLightsOn");
				collectTestReport(randomTestName);
			}

			// BlackOutBrakeLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setBlackOutBrakeLightsOn(true);
				String randomTestName = "BlackOutBrakeLightsOn";
				logger.debug("performTest: random set of attributes BlackOutBrakeLightsOn");
				collectTestReport(randomTestName);
			}            
        
			// BlackOutLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setBlackOutLightsOn(true);
				String randomTestName = "BlackOutLightsOn";
				logger.debug("performTest: random set of attributes BlackOutLightsOn");
				collectTestReport(randomTestName);
			}

			// BrakeLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setBrakeLightsOn(true);
				String randomTestName = "BrakeLightsOn";
				logger.debug("performTest: random set of attributes BrakeLightsOn");
				collectTestReport(randomTestName);
			}

			// FormationLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setFormationLightsOn(true);
				String randomTestName = "FormationLightsOn";
				logger.debug("performTest: random set of attributes FormationLightsOn");
				collectTestReport(randomTestName);
			}
         
			// HatchState,
			if (Math.random() <= rangeForTesting) {
				aircraft.setHatchState(true);
				String randomTestName = "HatchState";
				logger.debug("performTest: random set of attributes HatchState");
				collectTestReport(randomTestName);
			}

			// HeadLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setHeadLightsOn(true);
				String randomTestName = "HeadLightsOn";
				logger.debug("performTest: random set of attributes HeadLightsOn");
				collectTestReport(randomTestName);
			}

			// InteriorLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setInteriorLightsOn(true);
				String randomTestName = "InteriorLightsOn";
				logger.debug("performTest: random set of attributes InteriorLightsOn");
				collectTestReport(randomTestName);
			}
        
			// LandingLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setLandingLightsOn(true);
				String randomTestName = "LandingLightsOn";
				logger.debug("performTest: random set of attributes LandingLightsOn");
				collectTestReport(randomTestName);
			}

			// LauncherRaised,
			if (Math.random() <= rangeForTesting) {
				aircraft.setLauncherRaised(true);
				String randomTestName = "LauncherRaised";
				logger.debug("performTest: random set of attributes LauncherRaised");
				collectTestReport(randomTestName);
			}

			// NavigationLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setNavigationLightsOn(true);
				String randomTestName = "NavigationLightsOn";
				logger.debug("performTest: random set of attributes NavigationLightsOn");
				collectTestReport(randomTestName);
			}

			// RampDeployed,
			if (Math.random() <= rangeForTesting) {
				aircraft.setRampDeployed(true);
				String randomTestName = "RampDeployed";
				logger.debug("performTest: random set of attributes RampDeployed");
				collectTestReport(randomTestName);
			}

			// RunningLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setRunningLightsOn(true);
				String randomTestName = "RunningLightsOn";
				logger.debug("performTest: random set of attributes RunningLightsOn");
				collectTestReport(randomTestName);
			}

			// SpotLightsOn,
			if (Math.random() <= rangeForTesting) {
				aircraft.setSpotLightsOn(true);
				String randomTestName = "SpotLightsOn";
				logger.debug("performTest: random set of attributes SpotLightsOn");
				collectTestReport(randomTestName);
			}

			// TailLightsOn
			if (Math.random() <= rangeForTesting) {
				aircraft.setTailLightsOn(true);
				String randomTestName = "TailLightsOn";
				logger.debug("performTest: random set of attributes TailLightsOn");
				collectTestReport(randomTestName);
			}
            
           aircraft.update();
           logger.debug("");
           Thread.sleep(10); // maybe set to 10 ms
        }
        
        // log the settings and show the statistics of the updated values at least
         printTestReport();
      
        //   TODO  change this to a specifig  Exception 
        } catch (Exception e) {
            throw new TcInconclusiveIf("performTest received Exception: ",  e);
        }
  	
        logger.info("test {} passed", this.getClass().getName());
        
	}
	
	public void collectTestReport(String toTestAttribut  ) {
	    String randomTestName= toTestAttribut;
	    if (setAttributeReport.get(randomTestName) == null   ) {
	        setAttributeReport.put(randomTestName, 1);
	    } else { 
	        setAttributeReport.put(randomTestName, (setAttributeReport.get(randomTestName) +1 ) )  ;
	    }	    
	}
	
    public void printTestReport() {
        for (String s : setAttributeReport.keySet()) {
            String ausgabe =  "number of updats for  "+  s +":  "+ setAttributeReport.get(s);
            logger.info(ausgabe);
        }
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
