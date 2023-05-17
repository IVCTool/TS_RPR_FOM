package org.nato.ivct.rpr.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.objects.Platform.Attributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.URL;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
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
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
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
    static RTIambassador rtiAmbassador = null;

    @BeforeAll
    static void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember, FederateNameAlreadyInUse {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        rtiAmbassador = rtiFactory.getRtiAmbassador();
        FederateAmbassador nullAmbassador = new NullFederateAmbassador();
        URL[] fomList = new FomFiles()
            .addTmpRPR_BASE()
            .addTmpRPR_Enumerations()
            .addTmpRPR_Foundation()
            .addTmpRPR_Physical()
            .addTmpRPR_Switches()
            .getArray();

        rtiAmbassador.connect(nullAmbassador, CallbackModel.HLA_IMMEDIATE);
        try {
            rtiAmbassador.createFederationExecution("TestFederation", fomList);
        } catch (FederationExecutionAlreadyExists ignored) { }
        rtiAmbassador.joinFederationExecution("BaseEntityTest", "UnitTest", "TestFederation");
        HLAobjectRoot.initialize(rtiAmbassador);
    }

    @AfterAll
    static void leaveFederation() throws InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateNotExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError, FederationExecutionDoesNotExist, FederateIsExecutionMember {
        rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS);
        try {
            rtiAmbassador.destroyFederationExecution("TestFederation");
        } catch (FederatesCurrentlyJoined ignored) {
            log.trace("leave federation open for remaining federates");
        }
        rtiAmbassador.disconnect();
    }

    @Test
    void testHlaClassName() throws Exception {
        Aircraft aircraft1 = new Aircraft();
        String name = aircraft1.getHlaClassName();
        assertEquals(name, "HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Aircraft");
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

            Aircraft aircraft1 = new Aircraft();
            aircraft1.addSubscribe(Platform.Attributes.AfterburnerOn);
            aircraft1.addSubscribe(PhysicalEntity.Attributes.AcousticSignatureIndex);
            aircraft1.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
            aircraft1.register();

            // use the inherited method from Platform
            aircraft1.publishAfterburnerOn();    
            // use the type safe attribute
            aircraft1.addPublish(Platform.Attributes.AntiCollisionLightsOn);
            // use protected generic method
            aircraft1.addPubAttribute("RampDeployed");
            // use class method for subscription
            Platform.addPub(Attributes.BlackOutBrakeLightsOn);

            aircraft1.publish();

            AttributeHandle handle = aircraft1.getAttributeHandle("AfterburnerOn");
            String handleName = aircraft1.getHandleString(handle);
            assertEquals(handleName, "AfterburnerOn");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testEntityTypeEncoding() {
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

            EntityIdentifierStruct aEntityIdentifier = base1.getEntityIdentifier();
            aEntityIdentifier.setEntityNumber((short) 1);
            aEntityIdentifier.getFederateIdentifier().setApplicationID((short) 2);
            aEntityIdentifier.getFederateIdentifier().setSiteID((short) 3);

            EntityTypeStruct aEntityType = base1.getEntityType();
            aEntityType.setEntityKind((byte) 0xa);
            aEntityType.setDomain((byte) 0xb);
            aEntityType.setCountryCode((short) 3);
            aEntityType.setCategory((byte) 0xc);
            aEntityType.setSubcategory((byte) 0xd);
            aEntityType.setSpecific((byte) 0xe);
            aEntityType.setExtra((byte) 0xf);
            
            byte k = aEntityType.getEntityKind();
            byte t = aEntityType.getDomain();
            short cc = aEntityType.getCountryCode();
            byte c = aEntityType.getCategory();
            byte sc = aEntityType.getSubcategory();
            byte sp = aEntityType.getSpecific();
            byte ex = aEntityType.getExtra();

            assertTrue(k == 0xa);
            assertTrue(t == 0xb);
            assertTrue(cc == (short)3);
            assertTrue(c == 0xc);
            assertTrue(sc == 0xd);
            assertTrue(sp == 0xe);
            assertTrue(ex == 0xf);
            
            base1.setEntityType(aEntityType);
            // emulate the base1.update() logic;
            AttributeHandleValueMap values = base1.getAttributeValues();

            BaseEntity base2 = new BaseEntity();
            base2.decode(values);
            assertTrue(base2.getEntityType().getEntityKind() == 0xa);
            assertTrue(base2.getEntityType().getDomain() == 0xb);
            assertTrue(base2.getEntityType().getCountryCode() == (short)3);
            assertTrue(base2.getEntityType().getCategory() == 0xc);
            assertTrue(base2.getEntityType().getSubcategory() == 0xd);
            assertTrue(base2.getEntityType().getSpecific() == 0xe);
            assertTrue(base2.getEntityType().getExtra() == 0xf);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
