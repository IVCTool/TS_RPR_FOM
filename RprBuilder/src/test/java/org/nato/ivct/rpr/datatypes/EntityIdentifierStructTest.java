package org.nato.ivct.rpr.datatypes;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;


public class EntityIdentifierStructTest {
    public static final Logger log = LoggerFactory.getLogger(EntityIdentifierStructTest.class);
    RTIambassador rtiAmbassador = null;
    @BeforeEach
    void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        rtiAmbassador = rtiFactory.getRtiAmbassador();
    }

    @Test
    void testEncodeDecode() {
        try {
			EntityIdentifierStruct entityIdentifier = new EntityIdentifierStruct();
			entityIdentifier.setEntityNumber((short) 1);
            entityIdentifier.getFederateIdentifier().setApplicationID((short) 2);
            entityIdentifier.getFederateIdentifier().setSiteID((short) 3);

            byte[] pdu = entityIdentifier.toByteArray();           			
            
            EntityIdentifierStruct entityIdentifierReceived = new EntityIdentifierStruct();
            entityIdentifierReceived.decode(pdu);

            assert(entityIdentifierReceived.getEntityNumber() == 1);
            assert(entityIdentifierReceived.getFederateIdentifier().getApplicationID() == 2);
            assert(entityIdentifierReceived.getFederateIdentifier().getSiteID() == 3);
            
        } catch (RTIinternalError | DecoderException e) {
            fail(e.getMessage());
		}

    }
}
