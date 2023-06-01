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
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.HLAroot;
import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.interactions.HLAreportInteractionPublication;
import org.nato.ivct.rpr.interactions.HLAreportInteractionSubscription;
import org.nato.ivct.rpr.interactions.HLAreportObjectClassPublication;
import org.nato.ivct.rpr.interactions.HLAreportObjectClassSubscription;
import org.nato.ivct.rpr.interactions.HLArequestPublications;
import org.nato.ivct.rpr.interactions.HLArequestSubscriptions;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.HLAfederate;
import org.nato.ivct.rpr.objects.Munition;
import org.nato.ivct.rpr.objects.PhysicalEntity;

import hla.rti1516e.ResignAction;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.InteractionClassHandleFactory;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAbyte;
import hla.rti1516e.encoding.HLAvariableArray;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
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
    
	private boolean receivedHLAreportInteractionPublication = false;
	private boolean receivedHLAreportInteractionSubscription = false;
	private boolean receivedHLAreportObjectClassPublication = false;
	private boolean receivedHLAreportObjectClassSubscription = false;
    private Logger logger = null;
	private RTIambassador rtiAmbassador = null;
	private FederateAmbassador tcAmbassador = null;
	private Semaphore federateDiscovered = new Semaphore(0);
    private HashMap<ObjectInstanceHandle, Munition> knownMunitionEntities = new HashMap<>();
	private HashMap<ObjectInstanceHandle, Boolean> requestReport = new HashMap<>();
    private Munition munitionProxy;
	private boolean sutDiscovered = false;
	private int timeout;
	private HLArequestPublications reqPublications;
	private HLArequestSubscriptions reqSubscriptions;
	private FederateHandle federateHandle;

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
					logger.trace("discovered HLAfederate object : {}({})", theObject, receivedClass);
					requestReport.put(theObject, true);
					federateDiscovered.release(1);
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
						logger.info("Received HLAfederate update for System under test <{}[{}]> on host {} @ {}", 
							fed.getHLAfederateName(), 
							fed.getHLAfederateType(), 
							fed.getHLAfederateHost(), 
							fed.getHLARTIversion());
						federateDiscovered.release(1);
					}
				}
			} catch (RprBuilderException | DecoderException | NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

		@Override
		public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters,
				byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
				SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
			try {
				// Test for HLAreportInteractionPublication interaction
				HLAreportInteractionPublication reportPubs = HLAreportInteractionPublication.discover(interactionClass);
				if (reportPubs != null) {
					logger.info("HLAreportInteractionPublication received");
					reportPubs.clear();
					reportPubs.decode(theParameters);
					HLAvariableArray<HLAbyte> iH = reportPubs.getHLAinteractionClassList();
					for (HLAbyte i : iH) {
						logger.info("interaction: {}", i);
						// InteractionClassHandleFactory x = HLAroot.getRtiAmbassador().getInteractionClassHandleFactory(); 
						// x.(i);
						// InteractionClassHandle h = new InteractionClassHandle();
						// HLAroot.getRtiAmbassador().getInteractionClassName(i);
					}
					receivedHLAreportInteractionPublication = true;
					federateDiscovered.release(1);
					return;
				}
				// Test for HLAreportInteractionSubscription interaction
				HLAreportInteractionSubscription reportSubs = HLAreportInteractionSubscription.discover(interactionClass);
				if (reportSubs != null) {
					logger.info("HLAreportInteractionSubscription received");
					reportSubs.clear();
					reportSubs.decode(theParameters);
					receivedHLAreportInteractionSubscription = true;
					federateDiscovered.release(1);
					return;
				}
				// Test for HLAreportObjectClassPublication interaction
				HLAreportObjectClassPublication classPubs = HLAreportObjectClassPublication.discover(interactionClass);
				if (classPubs != null) {
					logger.info("HLAreportObjectClassPublication received");
					classPubs.clear();
					classPubs.decode(theParameters);
					receivedHLAreportObjectClassPublication = true;
					federateDiscovered.release(1);
					return;
				}
				// Test for HLAreportObjectClassSubscription interaction
				HLAreportObjectClassSubscription classSubs = HLAreportObjectClassSubscription.discover(interactionClass);
				if (classSubs != null) {
					logger.info("HLAreportObjectClassSubscription received");
					classSubs.clear();
					classSubs.decode(theParameters);
					receivedHLAreportObjectClassSubscription = true;
					federateDiscovered.release(1);
					return;
				}
			} catch (DecoderException e) {
				logger.error("unhandled decoder exception", e);
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
        logger.trace("preamble action for test {}", this.getClass().getName());
		timeout = Integer.parseInt(getTcParam("timeout"));
		try {
			rtiFactory = RtiFactoryFactory.getRtiFactory();
			rtiAmbassador = rtiFactory.getRtiAmbassador();
			tcAmbassador = new TestCaseAmbassador();

			// Loading FOM modules as temp file to comply to MAK RTI
			URL[] fomList = new FomFiles()
				.addTmpRPR_BASE()
				.addTmpRPR_Enumerations()
				.addTmpRPR_Foundation()
				.addTmpRPR_Physical()
				.addTmpRPR_Switches()
				.addTmpRPR_Warfare()
				.getArray();

			rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(federationName, fomList);
			} catch (FederationExecutionAlreadyExists ignored) { }
			federateHandle = rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName, fomList);
            HLAroot.initialize(rtiAmbassador);
		} catch (RTIinternalError | ConnectionFailed | InvalidLocalSettingsDesignator | UnsupportedCallbackModel 
				| AlreadyConnected | CallNotAllowedFromWithinCallback | CouldNotCreateLogicalTimeFactory 
				| FederationExecutionDoesNotExist | InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD 
				| SaveInProgress | RestoreInProgress | FederateAlreadyExecutionMember | NotConnected e) {
			throw new TcInconclusiveIf(e.getMessage());
		}
        logger.trace("Joined federation <{}>", federationName);
    }

    @Override
    protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
        logger.trace("perform test {}", this.getClass().getName());
		try {
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateHandle);
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateName);
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateType);
            HLAfederate.addSub(HLAfederate.Attributes.HLAfederateHost);
            HLAfederate.addSub(HLAfederate.Attributes.HLARTIversion);
			HLAfederate.sub();
			(new HLAreportObjectClassPublication()).subscribe();
			(new HLAreportObjectClassSubscription()).subscribe();
			(new HLAreportInteractionPublication()).subscribe();
			(new HLAreportInteractionSubscription()).subscribe();

			reqPublications = new HLArequestPublications();
			reqSubscriptions = new HLArequestSubscriptions();
			reqPublications.publish();
			reqSubscriptions.publish();

			PhysicalEntity.initialize(rtiAmbassador);
			munitionProxy = new Munition();
			munitionProxy.subscribeLauncherFlashPresent();
			munitionProxy.subscribeEntityIdentifier();
			munitionProxy.subscribe();
		} catch (RprBuilderException | AttributeNotDefined | ObjectClassNotDefined | SaveInProgress | RestoreInProgress | FederateNotExecutionMember | NotConnected | RTIinternalError | NameNotFound | InvalidObjectClassHandle | FederateServiceInvocationsAreBeingReportedViaMOM | InteractionClassNotDefined e) {
			throw new TcInconclusiveIf(e.getMessage());
		}

		Thread t = new Thread (() -> {
			// wait until object is discovered and check if SuT owns it
			while (! receivedHLAreportInteractionPublication && ! receivedHLAreportInteractionSubscription && ! receivedHLAreportObjectClassPublication && ! receivedHLAreportObjectClassSubscription ) {
				try {
					federateDiscovered.acquire();
					for (HLAfederate federate : HLAfederate.knownObjects.values()) {
						try {
							if (federate.isUpdated(HLAfederate.Attributes.HLAfederateHandle.name()) 
							&& (requestReport.get(federate.getObjectHandle()))
							&& (federate.getHLAfederateName().equalsIgnoreCase(getSutFederateName()))) {
								logger.info("SuT publications report requested for {}", federate.getHLAfederateName());
								reqPublications.setHLAfederate(federate.getHLAfederateHandle());
								reqPublications.send();
								logger.info("SuT subscription report requested for {}", federate.getHLAfederateName());
								reqSubscriptions.setHLAfederate(federate.getHLAfederateHandle());
								reqSubscriptions.send();
								requestReport.put(federate.getObjectHandle(), false);
							} else {
								rtiAmbassador.requestAttributeValueUpdate(federate.getObjectHandle(), federate.getSubscribedAttributes(), null);
							}
						} catch (FederateNotExecutionMember | NotConnected | RTIinternalError | RprBuilderException | InteractionClassNotDefined | SaveInProgress | RestoreInProgress | InteractionClassNotPublished | InteractionParameterNotDefined | InvalidInteractionClassHandle | EncoderException e) {
							logger.error(e.getMessage());
							return;
						}
					}
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
        logger.trace("postamble action for test {}", this.getClass().getName());
        try {
            rtiAmbassador.resignFederationExecution(ResignAction.NO_ACTION);
			rtiAmbassador.disconnect();
        } catch (InvalidResignAction | OwnershipAcquisitionPending | FederateOwnsAttributes | FederateNotExecutionMember
                | NotConnected | CallNotAllowedFromWithinCallback | RTIinternalError | FederateIsExecutionMember e) {
            throw new TcInconclusiveIf(e.getMessage());
		}		
	}
}
