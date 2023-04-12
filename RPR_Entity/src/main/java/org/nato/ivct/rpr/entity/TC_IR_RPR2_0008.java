/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

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
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.slf4j.Logger;
import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.AttributeHandle;
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
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

/**
 * IR-RPR2-0008: 
 * 
 * SuT shall define at least one leaf object class of `BaseEntity.PhysicalEntity` 
 * as published and/or subscribed in CS/SOM.
 */
public class TC_IR_RPR2_0008 extends AbstractTestCaseIf {

	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
    Logger logger = null;
	Semaphore physicalEntityDiscovered = new Semaphore(0);
    HashMap<ObjectInstanceHandle, PhysicalEntity> knownPhysicalEntities = new HashMap<>();
    PhysicalEntity phyEntity;
	private FederateHandle sutHandle;
	boolean phyEntityFromSutFound = false;

    class TestCaseAmbassador extends NullFederateAmbassador {
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject, 
				ObjectClassHandle theObjectClass,
				String objectName) throws FederateInternalError {
			logger.trace("discoverObjectInstance {}", theObject);
			try {
			
                String receivedClass = rtiAmbassador.getObjectClassName(theObjectClass);
                if (receivedClass.equals(phyEntity.getHlaClassName())) {
					// create the helper object
                    PhysicalEntity obj = new PhysicalEntity();
                    obj.setObjectHandle(theObject);
                    knownPhysicalEntities.put(theObject, obj);
                } 
			} catch (Exception e) {
				logger.warn("discovered object instance, but federate {} is not connected", getSutFederateName());
			}
			physicalEntityDiscovered.release(1);
		}

		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject,
				ObjectClassHandle theObjectClass,
				String objectName,
				FederateHandle producingFederate) throws FederateInternalError {
			logger.trace("discoverObjectInstance {} with producingFederate {}", theObject, producingFederate);
			discoverObjectInstance(theObject, theObjectClass, objectName);
			testSutHandle(producingFederate, theObject);
		}

		@Override
		public void informAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute,
				FederateHandle theOwner) throws FederateInternalError {
			testSutHandle(theOwner, theObject);
		}
    }

	private boolean testSutHandle(FederateHandle theFederate, ObjectInstanceHandle theObject) {
		try {
			PhysicalEntity phyEntity = knownPhysicalEntities.get(theObject);
			sutHandle = rtiAmbassador.getFederateHandle(getSutFederateName());
			if ((sutHandle.equals(theFederate)) && (phyEntity != null)) {
				phyEntityFromSutFound = true;
				physicalEntityDiscovered.release(1);
				return true;
			}
		} catch (NameNotFound | FederateNotExecutionMember | NotConnected | RTIinternalError e) {
			logger.warn("System under Test federate \"{}\" not yet found", getSutFederateName());
		}
		return false;
	}
    
	@Override
	protected void logTestPurpose(Logger logger) {
		logger.info("Test Case Purpose: \n"
			+ "    The test case verifies that the SuT defines at least one leaf object\n"
			+ "    class of `BaseEntity.PhysicalEntity` as published and/or subscribed in CS/SOM."); 
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
            .addTmpRPR_BASE()
            .addTmpRPR_Enumerations()
            .addTmpRPR_Foundation()
            .addTmpRPR_Physical()
            .addTmpRPR_Switches()
            .getArray();
			
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
		try {
			PhysicalEntity.initialize(rtiAmbassador);
			phyEntity = new PhysicalEntity();
			phyEntity.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
			phyEntity.subscribe();
			// wait until object is discovered and check if SuT owns it
			while (! phyEntityFromSutFound) {
				physicalEntityDiscovered.acquire();
				for (PhysicalEntity aPhysicalEntity : knownPhysicalEntities.values()) {
					ObjectInstanceHandle objectHandle = aPhysicalEntity.getObjectHandle();
					AttributeHandle entityIdentifierHandle = aPhysicalEntity.getAttributeHandle(BaseEntity.Attributes.EntityIdentifier.name());
					rtiAmbassador.queryAttributeOwnership(objectHandle, entityIdentifierHandle);
				}

			}
		} catch (Exception e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
        logger.info("test {} passed", this.getClass().getName());
	}

	@Override
	protected void postambleAction(Logger logger) throws TcInconclusiveIf {
        logger.info("postamble action for test {}", this.getClass().getName());
        try {
            rtiAmbassador.resignFederationExecution(ResignAction.NO_ACTION);
        } catch (InvalidResignAction | OwnershipAcquisitionPending | FederateOwnsAttributes | FederateNotExecutionMember
                | NotConnected | CallNotAllowedFromWithinCallback | RTIinternalError e) {
            throw new TcInconclusiveIf(e.getMessage());
        }		
	}

}
