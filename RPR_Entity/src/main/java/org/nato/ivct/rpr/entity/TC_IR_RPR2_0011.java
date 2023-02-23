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

import org.nato.ivct.rpr.PhysicalEntity;
import org.nato.ivct.rpr.BaseEntity;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.datatypes.SpatialStaticStruct;
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
 * IR-RPR2-0011:
 * 
 * SuT shall update the following required attributes for PhysicalEntity subclass object instances 
 * registered by SuT: EntityIdentifier, EntityType, Spatial.
 */
public class TC_IR_RPR2_0011 extends AbstractTestCaseIf {
    
    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;
	Semaphore physicalEntityDiscovered = new Semaphore(0);
    Semaphore receivedEntityIdentifier = new Semaphore(0);
    Semaphore receivedEntityType = new Semaphore(0);
    Semaphore receivedSpatial = new Semaphore(0);
    HashMap<ObjectInstanceHandle, PhysicalEntity> knownPhysicalEntitys = new HashMap<>();
    HashMap<ObjectInstanceHandle, BaseEntity> knownEntities = new HashMap<>();
    PhysicalEntity phyEntity;
    BaseEntity entity;
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
                    knownPhysicalEntitys.put(theObject, obj);
                    physicalEntityDiscovered.release(1);
                } else if (receivedClass.equals(entity.getHlaClassName())) {
                    BaseEntity newEntity = new BaseEntity();
                    newEntity.setObjectHandle(theObject);
                    knownEntities.put(theObject, newEntity);
                    physicalEntityDiscovered.release(1);
                }

            } catch (Exception e) {
                logger.error("discoverObjectInstance received Exception", e);
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
			testSutHandle(producingFederate, theObject);
        }

		@Override
		public void informAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute,
				FederateHandle theOwner) throws FederateInternalError {
			testSutHandle(theOwner, theObject);
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
            PhysicalEntity phyEntity = knownPhysicalEntitys.get(theObject);
            if (phyEntity != null) {
                phyEntity.clear();
                try {
                    phyEntity.decode(theAttributes);
                } catch (Exception e) {
                    logger.error("reflectAttributeValues received Exception", e);
                }
                if (phyEntity.isSetEntityType()) {
                    receivedEntityIdentifier.release(1);
                }
                if (phyEntity.isSetEntityType()) {
                    receivedEntityType.release(1);
                }
                if (phyEntity.isSetSpatial()) {
                    receivedSpatial.release(1);
                }
            }
            BaseEntity entity = knownEntities.get(theObject);
            if (entity != null) {
                entity.clear();
                try {
                    entity.decode(theAttributes);
                    SpatialStaticStruct spatial = entity.getSpatial().getSpatialStatic();
                    logger.info("BaseEntity update received at x={}, y={}, z={}", 
                        spatial.getWorldLocation().getX(),
                        spatial.getWorldLocation().getY(),
                        spatial.getWorldLocation().getZ()
                    );
                } catch (NameNotFound e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidObjectClassHandle e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FederateNotExecutionMember e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NotConnected e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (RTIinternalError e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (DecoderException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }            
        }
    }
    
	private boolean testSutHandle(FederateHandle theFederate, ObjectInstanceHandle theObject) {
		try {
			PhysicalEntity phyEntity = knownPhysicalEntitys.get(theObject);
			sutHandle = rtiAmbassador.getFederateHandle(getSutFederateName());
			if ((sutHandle == theFederate) &&  (phyEntity != null)){
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
        logger.info("perform test {}", this.getClass().getName());
        try {
            PhysicalEntity.initialize(rtiAmbassador);
            phyEntity = new PhysicalEntity();
            phyEntity.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
            phyEntity.addSubscribe(BaseEntity.Attributes.EntityType);
            phyEntity.addSubscribe(BaseEntity.Attributes.Spatial);
            phyEntity.subscribe();

            receivedEntityIdentifier.acquire();
            receivedEntityType.acquire();
            receivedSpatial.acquire();
			// wait until physical entity object is discovered and check if SuT owns it
            while (!phyEntityFromSutFound) {
				physicalEntityDiscovered.acquire();
				for (PhysicalEntity aPhysicalEntity : knownPhysicalEntitys.values()) {
					ObjectInstanceHandle objectHandle = aPhysicalEntity.getObjectHandle();
					AttributeHandle entityIdentifierHandle = aPhysicalEntity.getAttributeHandle(BaseEntity.Attributes.EntityIdentifier.name());
					rtiAmbassador.queryAttributeOwnership(objectHandle, entityIdentifierHandle);
				}
            }
            for (PhysicalEntity phyEnt : knownPhysicalEntitys.values()) {
                logger.trace("received entity identifier {}({}): EntityNumber={}, FederateIdentifier[SiteID={}, ApplicationID={}]",
                    phyEnt.getHlaClassName(), phyEnt.getObjectHandle(), 
                    phyEnt.getEntityIdentifier().getEntityNumber(),
                    phyEnt.getEntityIdentifier().getFederateIdentifier().getSiteID(),
                    phyEnt.getEntityIdentifier().getFederateIdentifier().getApplicationID()
                );
                logger.trace("received entity type {}({}): entityKind={}, countryCode={}, category={}, subcategory={}, specific={}, extra={}", 
                    phyEnt.getHlaClassName(), phyEnt.getObjectHandle(), 
                    phyEnt.getEntityType().getEntityKind(), 
                    phyEnt.getEntityType().getCountryCode(),
                    phyEnt.getEntityType().getCategory(),
                    phyEnt.getEntityType().getSubcategory(),
                    phyEnt.getEntityType().getSpecific(),
                    phyEnt.getEntityType().getExtra()
                );
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
