package org.nato.ivct.rpr.interactions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.exceptions.*;

public class HLAreportInteractionPublicationTest {

    RTIambassador rtiAmbassador = null;

    @BeforeEach
    void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember, FederateNameAlreadyInUse {
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
        rtiAmbassador.joinFederationExecution("BaseEntityTest", "UnitTest", "TestFederation");
        OmtBuilder.initialize(rtiAmbassador);
    }


    @Test
    void testSetParameter() {
        HLAreportInteractionPublication report;
        try {
            report = new HLAreportInteractionPublication();
            report.subscribe();
        } catch (NameNotFound | FederateNotExecutionMember | NotConnected | RTIinternalError | RprBuilderException | FederateServiceInvocationsAreBeingReportedViaMOM | InteractionClassNotDefined | SaveInProgress | RestoreInProgress e) {
            fail(e.getMessage());
        }
    }
}
