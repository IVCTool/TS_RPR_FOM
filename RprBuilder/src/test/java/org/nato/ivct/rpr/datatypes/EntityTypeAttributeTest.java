package org.nato.ivct.rpr.datatypes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
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


public class EntityTypeAttributeTest {
    public static final Logger log = LoggerFactory.getLogger(EntityTypeAttributeTest.class);
    RTIambassador rtiAmbassador = null;
    @BeforeEach
    void createRtiAmbassador() throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, AlreadyConnected, CallNotAllowedFromWithinCallback, RTIinternalError, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress, FederateAlreadyExecutionMember {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        rtiAmbassador = rtiFactory.getRtiAmbassador();
    }

    @Test
    void testGetter() {
        try {
            EntityTypeStruct entity = new EntityTypeStruct();
            assertNotNull(entity);
            byte kind = entity.getEntityKind();
            assertNotNull(kind);
            byte domain = entity.getDomain();
            assertNotNull(domain);
            short code = entity.getCountryCode();
            assertNotNull(code);
            byte cat = entity.getCategory();
            assertNotNull(cat);
            byte sub = entity.getSubcategory();
            assertNotNull(sub);
            byte specfic = entity.getSpecific();
            assertNotNull(specfic);
            byte ext = entity.getExtra();
            assertNotNull(ext);
            
        } catch (RTIinternalError e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testDecode() {

    }

    @Test
    void testEncode() {

    }

    @Test
    void testGet() {

    }
}
