package org.nato.ivct.rpr.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.FomFiles;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;

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

public class PlatformTest {

    public static final Logger log = LoggerFactory.getLogger(PlatformTest.class);
    static RTIambassador rtiAmbassador = null;

    @Test
    void testEncodeDecode() {
        HLAobjectRoot.initialize(rtiAmbassador);
        try {
            Platform p1 = new Platform();
            // set PhysicalEntity inherited attributes
            p1.setEngineSmokeOn(true);
            p1.setFirePowerDisabled(true);
            p1.setFlamesPresent(true);
            p1.setIsConcealed(true);
            p1.setTentDeployed(true);
            // set Platform native attributes
            p1.setAfterburnerOn(true);
            p1.setAntiCollisionLightsOn(true);
            p1.setBlackOutBrakeLightsOn(true);
            p1.setBlackOutLightsOn(true);
            p1.setBrakeLightsOn(true);
            p1.setFormationLightsOn(true);
            p1.setHatchState(true);
            p1.setInteriorLightsOn(true);
            p1.setLandingLightsOn(true);
            p1.setLauncherRaised(true);
            p1.setNavigationLightsOn(true);
            p1.setRampDeployed(true);
            p1.setRunningLightsOn(true);
            p1.setSpotLightsOn(true);
            p1.setTailLightsOn(true);

            AttributeHandleValueMap pdu = p1.getAttributeValues();

            Platform p2 = new Platform();
            p2.decode(pdu);
            assertTrue(p2.getEngineSmokeOn() == true);
            assertTrue(p2.getFirePowerDisabled() == true);
            assertTrue(p2.getFlamesPresent() == true);
            assertTrue(p2.getIsConcealed() == true);
            assertTrue(p2.getTentDeployed() == true);

            assertTrue(p2.getAfterburnerOn() == true);
            assertTrue(p2.getAntiCollisionLightsOn() == true);
            assertTrue(p2.getBlackOutBrakeLightsOn() == true);
            assertTrue(p2.getBlackOutLightsOn() == true);
            assertTrue(p2.getBrakeLightsOn() == true);
            assertTrue(p2.getFormationLightsOn() == true);
            assertTrue(p2.getHatchState() == true);
            assertTrue(p2.getInteriorLightsOn() == true);
            assertTrue(p2.getLandingLightsOn() == true);
            assertTrue(p2.getLauncherRaised() == true);
            assertTrue(p2.getNavigationLightsOn() == true);
            assertTrue(p2.getRampDeployed() == true);
            assertTrue(p2.getRunningLightsOn() == true);
            assertTrue(p2.getSpotLightsOn() == true);
            assertTrue(p2.getTailLightsOn() == true);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @BeforeAll
    static void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        rtiAmbassador = rtiFactory.getRtiAmbassador();
        FederateAmbassador nullAmbassador = new NullFederateAmbassador();
         URL[] fomList = new FomFiles()
            .addRPR_BASE()
            .addRPR_Enumerations()
            .addRPR_Foundation()
            .addRPR_Physical()
            .addRPR_Switches()
            .get();        
        rtiAmbassador.connect(nullAmbassador, CallbackModel.HLA_IMMEDIATE);
        try {
            rtiAmbassador.createFederationExecution("TestFederation", fomList);
        } catch (FederationExecutionAlreadyExists ignored) { }
        rtiAmbassador.joinFederationExecution("InteractionTest", "TestFederation", fomList);
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
}
