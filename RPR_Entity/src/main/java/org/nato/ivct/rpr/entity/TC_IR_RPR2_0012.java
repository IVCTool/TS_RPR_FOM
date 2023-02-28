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

import org.nato.ivct.rpr.Aircraft;
import org.nato.ivct.rpr.BaseEntity;
import org.nato.ivct.rpr.PhysicalEntity;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.entity.TC_IR_RPR2_0008.TestCaseAmbassador;
import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
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
import hla.rti1516e.FederateAmbassador.SupplementalReflectInfo;
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
 * SuT shall not update non-applicable PhysicalEntity Attributes 
 * as specified in Domain Appropriateness table in SISO-STD-001-2015."
 */


public class TC_IR_RPR2_0012 extends AbstractTestCaseIf {
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Logger logger = null;
	
	ObjectInstanceHandle _objectInstanceHandle;
	ObjectClassHandle _objectClassHandle;
	
	Aircraft aircraft;
	
	
	

	class TestCaseAmbassador extends NullFederateAmbassador {
		
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject,
				ObjectClassHandle theObjectClass,
				String objectName) throws FederateInternalError {
			logger.trace("discoverObjectInstance {}", theObject);
			System.out.println("######### ich habe was bekommen ObjectInstanceHandle: " +theObject  );	// Debug			
			System.out.println("######### ich habe was bekommen ObjectClassHandle: " +theObjectClass ); // Debug			
			
			//   Debug tests			
			try {
				   System.out.println("1 rtiAmbassador.getObjectInstanceName: " +  rtiAmbassador.getObjectInstanceName(theObject) );
				   System.out.println("1 rtiAmbassador.getObjectClassName: "    +  rtiAmbassador.getObjectClassName(theObjectClass ) );
				} catch ( ObjectInstanceNotKnown |  FederateNotExecutionMember | NotConnected | RTIinternalError | InvalidObjectClassHandle  e ) {
					logger.error("discoverObjectInstance received Exception", e  ); 
			    }
			// Debug Tests End
			
			
		//  irgendwie herauskriegen was da gekommen ist.
					if (theObject.equals(_objectInstanceHandle)) {
						//#################################
					}
			
			//semaphore.release(1);
		}

		@Override
		public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass,
				String objectName, FederateHandle producingFederate) throws FederateInternalError {
			logger.trace("discoverObjectInstance {} with producingFederate {}", theObject, producingFederate);
			discoverObjectInstance(theObject, theObjectClass, objectName);
			;
		}

		
		@Override
		public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
			LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle,
			SupplementalReflectInfo reflectInfo)
			throws FederateInternalError {
			logger.trace("reflectAttributeValues with MessageRetractionHandle, ");
			reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);			
		}	
		
		
		@Override
		public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
			LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo)
			throws FederateInternalError {
			logger.trace("reflectAttributeValues with LogicalTime ");
			reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
	    }
		
		@Override
		public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
			SupplementalReflectInfo reflectInfo)
			throws FederateInternalError {
			logger.trace("reflectAttributeValues without  LogicalTime,  MessageRetractionHandle  ");
		}


	
		
		
		
		
		
		
		
		
		
	}
	

	@Override
	protected void logTestPurpose(Logger logger) {
		logger.info("Test Case Purpose: \n"
		+ "The test case verifies that the SuT do not update non-applicable PhysicalEntity Attributes \n"
		+ "as specified in Domain Appropriateness table in SISO-STD-001-2015."); 
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
			.addRPR_BASE().addRPR_Enumerations().addRPR_Foundation()
			.addRPR_Physical().addRPR_Switches().get();

			rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);

			// rtiAmbassador.createFederationExecution(federationName, fomList.toArray(new URL[fomList.size()]));
			try {
			  rtiAmbassador.createFederationExecution(federationName, fomList.toArray(new URL[fomList.size()]) );
		    } catch (FederationExecutionAlreadyExists ignored) { }
			
			rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName, fomList.toArray(new URL[fomList.size()]));
           
		} catch (RTIinternalError | ConnectionFailed | InvalidLocalSettingsDesignator | UnsupportedCallbackModel 
				| AlreadyConnected | CallNotAllowedFromWithinCallback | CouldNotCreateLogicalTimeFactory 
				| FederationExecutionDoesNotExist | InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD 
				| SaveInProgress | RestoreInProgress | FederateAlreadyExecutionMember | NotConnected e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
	}

	
		
	@Override
	protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
		Aircraft.initialize(rtiAmbassador);
		
		try {
			aircraft = new Aircraft();
			
			this._objectInstanceHandle=aircraft.getObjectHandle();
			
			//aircraft.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
			aircraft.addSubscribe(BaseEntity.Attributes.Spatial);
			aircraft.addSubscribe(PhysicalEntity.Attributes.CamouflageType);
			aircraft.addSubscribe(PhysicalEntity.Attributes.DamageState);
			aircraft.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);
			aircraft.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);
			aircraft.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
			aircraft.addSubscribe(PhysicalEntity.Attributes.Immobilized);
			aircraft.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
			aircraft.addSubscribe(PhysicalEntity.Attributes.PowerPlantOn);
			aircraft.addSubscribe(PhysicalEntity.Attributes.SmokePlumePresent);
			aircraft.addSubscribe(PhysicalEntity.Attributes.TentDeployed);
			aircraft.addSubscribe(PhysicalEntity.Attributes.TrailingEffectsCode);
			
			aircraft.subscribe();
		} catch (Exception e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
		
		// Irgendwie muss doch was zu sehen sein	
		
		/*
		try {
		   System.out.println("2 rtiAmbassador.getObjectInstanceName: " +  rtiAmbassador.getObjectInstanceName(this._objectInstanceHandle) );
		   System.out.println("2 rtiAmbassador.getObjectClassName: "    +  rtiAmbassador.getObjectClassName(this._objectClassHandle ) );
		} catch ( ObjectInstanceNotKnown |  FederateNotExecutionMember | NotConnected | RTIinternalError | InvalidObjectClassHandle  e ) {
		  throw new  TcInconclusiveIf(e.getMessage() ); 
	    }
	    */
		
		
	}

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
		// TODO Auto-generated method stub

	}

}