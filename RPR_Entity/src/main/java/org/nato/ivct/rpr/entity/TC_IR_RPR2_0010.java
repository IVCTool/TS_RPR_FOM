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
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;
import org.nato.ivct.rpr.FomFiles;
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
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;






/**
 * Interoperability Requirement:  IR-RPR2-0010
 * 
 * SuT shall in CS specify the use of Dead-Reckoning algorithms for all published
 *  and subscribed BaseEntity.PhysicalEntity and subclasses.
 *   
 *   
 *   -------------------------------------------
 *  SISO-STD-001-2015, Standard for Guidance, Rationale, and Interoperability Modalities
 * for the Real-time Platform Reference Federation Object Model  S 26
 *
 * The basic architecture of DIS specified the use of a dead reckoning mechanism for
 * reducing communication processing (section 1.3.1.f of IEEE Std 1278.1TM-1995 [6]).
 * 
 * The RPR FOM has adopted this mechanism for the same purpose.
 * For each registered object instance, the use of dead reckoning requires that
 * a federate maintain a dead reckoning model in addition to its own internal model.
 *
 * The dead reckoning model shall follow one of the prescribed dead reckoning
 * algorithms defined by DIS 1995 and enumerated in the RPR FOM.
 * 
 * Dead Reckoning shall be applied to all object instances that are derived
 * from the BaseEntity object class.
 * 
 * A federate shall issue a Spatial attribute update whenever the differences
 * in position or orientation between its internal model and its dead reckoning
 *  model have exceeded established thresholds.  
 *  
 *  in rPR-Base_v2.0.xml is defined:
 *  <fixedRecordData notes="RPRnoteBase15">
 *    <name>SpatialFPStruct</name>
 *    <encoding>HLAfixedRecord</encoding>
 *    <semantics>Spatial structure for Dead Reckoning Algorithm FPW (2) and FPB (6).</semantics>
 *      <field> ....
 *      
                
 *  
 *  
 */

public class TC_IR_RPR2_0010 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Logger logger = null;
	
	Semaphore semaphore = new Semaphore(0);
	
	HashMap<AttributeHandle,byte[]> _attributHandleValues  = new HashMap<>() ;
	
	String toTestEntityIdentifier="";
	BaseEntity toTestEntity;
	
	
	 public TC_IR_RPR2_0010() {
			
		}
		
	    public TC_IR_RPR2_0010(String  _toTestEntityName) {
	    	toTestEntityIdentifier=_toTestEntityName;
		}
	
	

	class TestCaseAmbassador extends NullFederateAmbassador {
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject,
				ObjectClassHandle theObjectClass,
				String objectName) throws FederateInternalError {
			logger.trace("discoverObjectInstance {}", theObject);
			//semaphore.release(1);
			
			try {
				// we only want to observe selected ObjectInstances
				if (toTestEntity.getHlaClassName().equals(rtiAmbassador.getObjectClassName(theObjectClass))) {
					logger.info("discoverObjectInstance rtiAmbassador.getObjectInstanceName: " +  rtiAmbassador.getObjectInstanceName(theObject) ); // Debug
					logger.info("discoverObjectInstance rtiAmbassador.getObjectClassName: " + rtiAmbassador.getObjectClassName(theObjectClass)); // Debug
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

		
		// We have to get Informations from the RTI  about Details  what the SUT specify in the CS/FOM
		
	}
	

	@Override
	protected void logTestPurpose(Logger logger) {
	 String msg = "Test Case Purpose: " ;
	        msg += "SuT shall in CS specify the use of Dead-Reckoning algorithms for all published " ;
	        msg += "and subscribed BaseEntity.PhysicalEntity and subclasses.";
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
		
		BaseEntity.initialize(rtiAmbassador);     //to adjust  //  move to Preamble ?

		try {
			
			switch (toTestEntityIdentifier){
			  case "Aircraft":                                    //to adjust 
				  toTestEntity = new Aircraft();
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
				  toTestEntity = new Aircraft();
			}
			
			toTestEntity.addSubscribe(BaseEntity.Attributes.EntityIdentifier);  // to be adjusted
			// ...
			
			toTestEntity.subscribe();

			boolean seenEnough = false;
			while (!seenEnough) {
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
