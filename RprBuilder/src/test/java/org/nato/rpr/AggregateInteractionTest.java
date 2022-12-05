package org.nato.rpr;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


/**
 *
 * @author hzg
 */
public class AggregateInteractionTest {

    public static final org.slf4j.Logger log = LoggerFactory.getLogger(AggregateInteractionTest.class);
    RTIambassador rtiAmbassador = null;

    @BeforeEach
    void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        rtiAmbassador = rtiFactory.getRtiAmbassador();
        FederateAmbassador nullAmbassador = new NullFederateAmbassador();
        ArrayList<URL> foms = new ArrayList<>();
        foms.add(AggregateInteractionTest.class.getResource("/RPR-FOM-v2.0/RPR-Base_v2.0.xml"));
        foms.add(AggregateInteractionTest.class.getResource("/RPR-FOM-v2.0/RPR-Base_v2.0.xml"));
        foms.add(AggregateInteractionTest.class.getResource("/RPR-FOM-v2.0/RPR-Aggregate_v2.0.xml"));
        foms.add(AggregateInteractionTest.class.getResource("/RPR-FOM-v2.0/RPR-Switches_v2.0.xml"));
        rtiAmbassador.connect(nullAmbassador, CallbackModel.HLA_IMMEDIATE);
        try {
            rtiAmbassador.createFederationExecution("TestFederation", foms.toArray(new URL[foms.size()]));
        } catch (FederationExecutionAlreadyExists ignored) { }
        rtiAmbassador.joinFederationExecution("InteractionTest", "TestFederation", foms.toArray(new URL[foms.size()]));

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
    void setParameterTest () throws Exception {
        AggregateInteraction aggregateInteration = new AggregateBuilder(rtiAmbassador)
            .addEventId()
            .addFederate()
            .addRemoveSubunits()
            .addAggregateUnit()
            .build();
        aggregateInteration.publish(rtiAmbassador);

        aggregateInteration.clear();
        aggregateInteration.setValueEventId((byte) 0x01);
        aggregateInteration.setValueFederate((short) 1);
        aggregateInteration.send(rtiAmbassador);
        assertTrue(true);
    }

    @Test
    void simpleTest2 () {
        assertTrue (1 + 1 == 3);
    }
}
