package org.nato.ivct.rpr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.BaseEntity;
import org.nato.ivct.rpr.HLAobjectRoot;
import org.nato.ivct.rpr.PhysicalEntity;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.util.ArrayList;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
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


public class BaseEntityTest {
    public static final Logger log = LoggerFactory.getLogger(BaseEntityTest.class);
    RTIambassador rtiAmbassador = null;

    @BeforeEach
    void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        rtiAmbassador = rtiFactory.getRtiAmbassador();
        FederateAmbassador nullAmbassador = new NullFederateAmbassador();
        ArrayList<URL> fomList = new FomFiles()
            .addRPR_BASE()
            .addRPR_Enumerations()
            .addRPR_Foundation()
            .addRPR_Physical()
            .addRPR_Switches()
            .get();

        rtiAmbassador.connect(nullAmbassador, CallbackModel.HLA_IMMEDIATE);
        try {
            rtiAmbassador.createFederationExecution("TestFederation", fomList.toArray(new URL[fomList.size()]));
        } catch (FederationExecutionAlreadyExists ignored) { }
        rtiAmbassador.joinFederationExecution("InteractionTest", "TestFederation", fomList.toArray(new URL[fomList.size()]));
    }

    @AfterEach
    void leaveFederation() throws InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateNotExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError, FederationExecutionDoesNotExist, FederateIsExecutionMember {
        rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS);
        try {
            rtiAmbassador.destroyFederationExecution("TestFederation");
        } catch (FederatesCurrentlyJoined ignored) {
            log.trace("leave federation open for remaining federates");
        }
        rtiAmbassador.disconnect();
    }

    @Test
    void testRegister() {
        try {
            HLAobjectRoot.initialize(rtiAmbassador);

            BaseEntity base1 = new BaseEntity();
            base1.subscribeEntityType();
            base1.subscribeEntityIdentifier();
            base1.subscribeIsPartOf();
            base1.subscribeSpatial();
            base1.subscribeRelativeSpatial();
            base1.publishEntityType();
            base1.publishEntityIdentifier();
            base1.publishIsPartOf();
            base1.publishSpatial();
            base1.publishRelativeSpatial();
            base1.register();
            HLAfixedRecord aEntityType = base1.getEntityType();
            HLAoctet entityKind = (HLAoctet) aEntityType.get(0);
            entityKind.setValue((byte) 0xa);
            base1.setEntityType(aEntityType);
            base1.update();            

            BaseEntity base2 = new BaseEntity();
            base2.addSubscribe(BaseEntity.Attributes.EntityType);
            base2.addPublish(BaseEntity.Attributes.EntityType);
            base2.register();            

            Aircraft aircraft = new Aircraft();
            aircraft.publishAfterburnerOn();
            aircraft.addSubscribe(Platform.Attributes.AfterburnerOn);
            aircraft.addSubscribe(PhysicalEntity.Attributes.AcousticSignatureIndex);
            aircraft.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
            aircraft.register();

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

}
