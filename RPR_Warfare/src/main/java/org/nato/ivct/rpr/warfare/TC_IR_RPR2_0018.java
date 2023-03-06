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

package org.nato.ivct.rpr.warfare;

import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.BaseEntity;
import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.HLAfederate;
import org.nato.ivct.rpr.PhysicalEntity;
import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.Munition;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.DecoderException;
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
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;


/**
 * IR-RPR2-0011:
 * 
 * SuT shall define interaction class WeaponFire or at least one leaf class as published 
 * and/or subscribed in CS/SOM."
 */
public class TC_IR_RPR2_0018 extends AbstractTestCaseIf {
    
    Logger logger = null;
	RTIambassador rtiAmbassador = null;
	FederateAmbassador tcAmbassador = null;
	Semaphore federateDiscovered = new Semaphore(0);
    HashMap<ObjectInstanceHandle, Munition> knownMunitionEntities = new HashMap<>();
    Munition munitionProxy;
	boolean sutDiscovered = false;
	private int timeout;

    class TestCaseAmbassador extends NullFederateAmbassador {
		@Override
		public void discoverObjectInstance(
				ObjectInstanceHandle theObject, 
				ObjectClassHandle theObjectClass,
				String objectName) throws FederateInternalError {
			logger.trace("discoverObjectInstance {}", theObject);
			try {
                String receivedClass = rtiAmbassador.getObjectClassName(theObjectClass);
				HLAfederate fed = HLAfederate.discover(theObject, theObjectClass);
                if (fed != null) {
					logger.trace("discovered HLAfederate object : {}", theObject);
				} 
			} catch (NotConnected | InvalidObjectClassHandle | FederateNotExecutionMember | RTIinternalError | RprBuilderException e) {
				logger.warn("exception in discovered object instance: {}", e.getMessage());
			} 
		}

        @Override
        public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
                byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
            logger.trace("reflectAttributeValues without time");
			try {
				HLAfederate fed = HLAfederate.get(theObject);
				if (fed != null) {
					fed.decode(theAttributes);
					logger.trace("HLAfederate values: {}<{}>", fed.getHLAfederateName(), fed.getHLAfederateHandle());
					if (fed.getHLAfederateName().equalsIgnoreCase(getSutFederateName())) {
						sutDiscovered = true;
						federateDiscovered.release(1);
					}
				}
			} catch (RprBuilderException | DecoderException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    @Override
    protected void logTestPurpose(Logger logger) {
        this.logger = logger;
		this.logger.info("Test Case Purpose: \n"
			+ "    SuT shall define interaction class WeaponFire or at least one leaf class as published\n" 
			+ "    and/or subscribed in CS/SOM."); 
    }

    @Override
    protected void preambleAction(Logger logger) throws TcInconclusiveIf {
        RtiFactory rtiFactory;
        logger.info("preamble action for test {}", this.getClass().getName());
		timeout = Integer.parseInt(getTcParam("timeout"));
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
            .addRPR_Warfare()
            .get();
			
			rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(federationName, fomList.toArray(new URL[fomList.size()]));
			} catch (FederationExecutionAlreadyExists ignored) { }
			rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName, fomList.toArray(new URL[fomList.size()]));
            OmtBuilder.initialize(rtiAmbassador);
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
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateHandle);
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateName);
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateType);
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateHost);
            HLAfederate.addSub(HLAfederate.Attributes.HLARTIversion);
			HLAfederate.sub();

			PhysicalEntity.initialize(rtiAmbassador);
			munitionProxy = new Munition();
			munitionProxy.subscribeLauncherFlashPresent();
			munitionProxy.subscribeEntityIdentifier();
			munitionProxy.subscribe();
		} catch (RprBuilderException | AttributeNotDefined | ObjectClassNotDefined | SaveInProgress | RestoreInProgress | FederateNotExecutionMember | NotConnected | RTIinternalError | NameNotFound | InvalidObjectClassHandle e) {
			throw new TcInconclusiveIf(e.getMessage());
		}

		Thread t = new Thread (() -> {
			// wait until object is discovered and check if SuT owns it
			while (! sutDiscovered) {
				try {
					federateDiscovered.acquire();
					for (Munition aMunition : knownMunitionEntities.values()) {
						ObjectInstanceHandle objectHandle = aMunition.getObjectHandle();
						AttributeHandle entityIdentifierHandle;
						entityIdentifierHandle = aMunition.getAttributeHandle(BaseEntity.Attributes.EntityIdentifier.name());
						rtiAmbassador.queryAttributeOwnership(objectHandle, entityIdentifierHandle);
					}
				} catch (InterruptedException | NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError | AttributeNotDefined | ObjectInstanceNotKnown | SaveInProgress | RestoreInProgress e) {
					e.printStackTrace();
					return;
				}
			}
		});
		t.start();
		try {
			t.join(timeout);
		} catch (InterruptedException e) {
			logger.warn("timeout received for test {}", this.getClass().getName());
			e.printStackTrace();
		}
		if (t.isAlive()) {
			logger.warn("timeout received for test {}", this.getClass().getName());
			throw new TcFailedIf("timeout");
		}
        logger.info("test {} passed", this.getClass().getName());
    }

    @Override
    protected void postambleAction(Logger logger) throws TcInconclusiveIf {
        logger.info("placeholder for postamble action for test {}", this.getClass().getName());
    }
}
