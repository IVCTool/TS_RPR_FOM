package org.nato.ivct.rpr.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.RprBuilderException;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.SpatialStaticStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.URL;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
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
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;


public class Platform_AttributesTest {

    public static final Logger log = LoggerFactory.getLogger(Platform_AttributesTest.class);
    public static RTIambassador rtiAmbassador = null;

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
        rtiAmbassador.joinFederationExecution("Platform_AttributesTest", "UnitTest", "TestFederation");
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
    void testAttributeSetterAndGetter() {
        try {
            // create Platform object and set test data
            Platform p1 = new Platform();
            EntityIdentifierStruct aEntityIdentifier = p1.getEntityIdentifier();
            aEntityIdentifier.setEntityNumber((short) 1);
            aEntityIdentifier.getFederateIdentifier().setApplicationID((short) 2);
            aEntityIdentifier.getFederateIdentifier().setSiteID((short) 3);
            p1.setEntityIdentifier(aEntityIdentifier);
            SpatialVariantStruct spatial = p1.getSpatial();
            SpatialStaticStruct spatialStatic = spatial.getSpatialStatic();
            spatialStatic.setIsFrozen(true);
            spatialStatic.getWorldLocation().setX(1.1);
            spatialStatic.getWorldLocation().setY(2.2);
            spatialStatic.getWorldLocation().setZ(3.3);
            p1.setSpatial(spatial);
            p1.setAfterburnerOn(true);
            p1.setAntiCollisionLightsOn(true);
            p1.setBlackOutBrakeLightsOn(true);
            p1.setBlackOutLightsOn(true);
            p1.setBrakeLightsOn(true);
            p1.setFormationLightsOn(true);
            p1.setHatchState(true);
            p1.setHeadLightsOn(true);
            p1.setInteriorLightsOn(true);
            p1.setLandingLightsOn(true);
            p1.setLauncherRaised(true);
            p1.setNavigationLightsOn(true);
            p1.setRampDeployed(true);
            p1.setRunningLightsOn(true);
            p1.setSpotLightsOn(true);
            p1.setTailLightsOn(true);

            // encode attributes
            AttributeHandleValueMap value = p1.getAttributeValues();
            // decode and validate attributes
            Platform p2 = new Platform();
            p2.decode(value);
            assertEquals(p2.getEntityIdentifier().getEntityNumber(), (short) 1);
            assertEquals(p2.getEntityIdentifier().getFederateIdentifier().getApplicationID(), (short) 2);
            assertEquals(p2.getEntityIdentifier().getFederateIdentifier().getSiteID(), (short) 3);
            assertEquals(p2.getSpatial().getSpatialStatic().getWorldLocation().getX(), 1.1);
            assertEquals(p2.getSpatial().getSpatialStatic().getWorldLocation().getY(), 2.2);
            assertEquals(p2.getSpatial().getSpatialStatic().getWorldLocation().getZ(), 3.3);
            assertEquals(p2.getAfterburnerOn().getValue(), true);
            assertEquals(p2.getAntiCollisionLightsOn().getValue(), true);
            assertEquals(p2.getBlackOutBrakeLightsOn().getValue(), true);
            assertEquals(p2.getBlackOutLightsOn().getValue(), true);
            assertEquals(p2.getBrakeLightsOn().getValue(), true);
            assertEquals(p2.getFormationLightsOn().getValue(), true);
            assertEquals(p2.getHatchState().getValue(), true);
            assertEquals(p2.getHeadLightsOn().getValue(), true);
            assertEquals(p2.getInteriorLightsOn().getValue(), true);
            assertEquals(p2.getLandingLightsOn().getValue(), true);
            assertEquals(p2.getLauncherRaised().getValue(), true);
            assertEquals(p2.getNavigationLightsOn().getValue(), true);
            assertEquals(p2.getRampDeployed().getValue(), true);
            assertEquals(p2.getRunningLightsOn().getValue(), true);
            assertEquals(p2.getSpotLightsOn().getValue(), true);
            assertEquals(p2.getTailLightsOn().getValue(), true);

        } catch ( DecoderException | RprBuilderException | RTIinternalError | NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | EncoderException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testMunitionSetterAndGetter() {
        try {
            // create Munition object and set test data
            Munition m1 = new Munition();
            EntityIdentifierStruct aEntityIdentifier = m1.getEntityIdentifier();
            aEntityIdentifier.setEntityNumber((short) 1);
            aEntityIdentifier.getFederateIdentifier().setApplicationID((short) 2);
            aEntityIdentifier.getFederateIdentifier().setSiteID((short) 3);
            m1.setEntityIdentifier(aEntityIdentifier);
            SpatialVariantStruct spatial = m1.getSpatial();
            SpatialStaticStruct spatialStatic = spatial.getSpatialStatic();
            spatialStatic.setIsFrozen(true);
            spatialStatic.getWorldLocation().setX(1.1);
            spatialStatic.getWorldLocation().setY(2.2);
            spatialStatic.getWorldLocation().setZ(3.3);
            m1.setSpatial(spatial);
            m1.setLauncherFlashPresent(true);

            // encode attributes
            AttributeHandleValueMap value = m1.getAttributeValues();
            // decode and validate attributes
            Munition m2 = new Munition();
            m2.decode(value);
            assertEquals(m2.getEntityIdentifier().getEntityNumber(), (short) 1);
            assertEquals(m2.getEntityIdentifier().getFederateIdentifier().getApplicationID(), (short) 2);
            assertEquals(m2.getEntityIdentifier().getFederateIdentifier().getSiteID(), (short) 3);
            assertEquals(m2.getSpatial().getSpatialStatic().getWorldLocation().getX(), 1.1);
            assertEquals(m2.getSpatial().getSpatialStatic().getWorldLocation().getY(), 2.2);
            assertEquals(m2.getSpatial().getSpatialStatic().getWorldLocation().getZ(), 3.3);
            assertEquals(m2.getLauncherFlashPresent().getValue(), true);

        } catch ( DecoderException | RprBuilderException | RTIinternalError | NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected | EncoderException e) {
            fail(e.getMessage());
        }
    }

}
