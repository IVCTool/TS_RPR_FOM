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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.nato.ivct.rpr.BaseEntity;
import org.nato.ivct.rpr.Aircraft;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
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
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.DecoderException;
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
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;


/**
 * IR_RPR2_0010:
 * SuT shall update the following required attributes for PhysicalEntity subclass object instances 
 * registered by SuT: EntityIdentifier, EntityType, Spatial.
 */
public class TC_IR_RPR2_0010 extends AbstractTestCaseIf {
    
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
    Semaphore receivedEntityIdentifier = new Semaphore(0);
    Semaphore receivedEntityType = new Semaphore(0);
    Semaphore receivedSpatial = new Semaphore(0);
    HashMap<ObjectInstanceHandle, Aircraft> knownAircrafts = new HashMap<>();
    Aircraft aircraft;

    class TestCaseAmbassador extends NullFederateAmbassador {
        @Override
        public void discoverObjectInstance(
                ObjectInstanceHandle theObject, 
                ObjectClassHandle theObjectClass,
                String objectName) throws FederateInternalError {
            logger.trace("discoverObjectInstance {}", theObject);
            try {
                String receivedClass = rtiAmbassador.getObjectClassName(theObjectClass);
                if (receivedClass.equals(aircraft.getHlaClassName())) {
                    // create the helper object
                    Aircraft obj = new Aircraft();
                    obj.register(theObject);
                    knownAircrafts.put(theObject, obj);
                }

            } catch (InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | RTIinternalError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void discoverObjectInstance(
                ObjectInstanceHandle theObject,
                ObjectClassHandle theObjectClass,
                String objectName,
                FederateHandle producingFederate) throws FederateInternalError {
            logger.trace("discoverObjectInstance {} with producingFederate {}", theObject, producingFederate);
            this.discoverObjectInstance(theObject, theObjectClass, objectName);
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
        public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
                byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
                SupplementalReflectInfo reflectInfo) throws FederateInternalError {
            logger.trace("reflectAttributeValues without time");
            Aircraft aircraft = knownAircrafts.get(theObject);
            if (aircraft != null) {
                aircraft.clear();
                try {
                    aircraft.decode(theAttributes);
                } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected
                        | RTIinternalError e) {
                    throw new FederateInternalError(e.getLocalizedMessage());
                } catch (DecoderException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (aircraft.isSetEntityType()) {
                    receivedEntityIdentifier.release(1);
                }
                if (aircraft.isSetEntityType()) {
                    receivedEntityType.release(1);
                }
                // if (aircraft.isSetSpatial()) {
                //     receivedSpatial.release(1);
                // }
            }
            
        }
    }
    
    @Override
    protected void logTestPurpose(Logger logger) {
        String msg =    "Test Case Purpose: The test case verifies that the SuT registers and publishes ";
        msg +=          "PhysicalEntity subclass object instances. Namely these attributes are: ";
        msg +=          "`EntityIdentifier`, `EntityType`, and `Spatial`.";
        logger.info(msg);
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
            rtiAmbassador.joinFederationExecution(sutFederateName, federationName, fomList.toArray(new URL[fomList.size()]));
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
            Aircraft.initialize(rtiAmbassador);
            aircraft = new Aircraft();
            aircraft.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
            aircraft.addSubscribe(BaseEntity.Attributes.EntityType);
            aircraft.addSubscribe(BaseEntity.Attributes.Spatial);
            aircraft.subscribe();

            receivedEntityIdentifier.acquire();
            for (Aircraft a1 : knownAircrafts.values()) {
                EntityTypeStruct et = a1.getEntityType();
                byte k = et.getEntityKind();
                short cc = et.getCountryCode();
                byte c = et.getCategory();
                byte sc = et.getSubcategory();
                byte sp = et.getSpecific();
                byte ex = et.getExtra();
            }

            receivedEntityType.acquire();
            // receivedSpatial.acquire();

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
