package org.nato.ivct.rpr.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.datatypes.CamouflageEnum32;
import org.nato.ivct.rpr.datatypes.DamageStatusEnum32;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.ForceIdentifierEnum8;
import org.nato.ivct.rpr.datatypes.TrailingEffectsCodeEnum32;

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


public class PhysicalEntityTest {

    public static final Logger log = LoggerFactory.getLogger(PhysicalEntityTest.class);
    static RTIambassador rtiAmbassador = null;

    @Test
    void testEncodeDecode() {
        HLAobjectRoot.initialize(rtiAmbassador);
        try {
            PhysicalEntity phy1 = new PhysicalEntity();
            phy1.setAcousticSignatureIndex((short) 3);
            // Struct types can not be set in that simple way because the struct doesn't know about the attribute changing
            EntityTypeStruct tempStruct= phy1.getAlternateEntityType();
            tempStruct.setCountryCode((short)3);
            phy1.setAlternateEntityType(tempStruct);
            
            //ArticulatedParametersArray,         // ArticulatedParameterStructLengthlessArray
            phy1.setCamouflageType(CamouflageEnum32.DesertCamouflage);
            phy1.setDamageState(DamageStatusEnum32.ModerateDamage);       
            phy1.setEngineSmokeOn(true);
            phy1.setFirePowerDisabled(true);
            phy1.setFlamesPresent(true);            
            phy1.setForceIdentifier(ForceIdentifierEnum8.Neutral_10);
            phy1.setHasAmmunitionSupplyCap(true);
            phy1.setHasFuelSupplyCap(true);
            phy1.setHasRecoveryCap(true);
            phy1.setHasRepairCap(true); 
            phy1.setImmobilized(true);            
            phy1.setInfraredSignatureIndex((short)2);      
            phy1.setIsConcealed(true);           
            phy1.setLiveEntityMeasuredSpeed((short)2);   //VelocityDecimeterPerSecondInteger16   corrrect Datatype ?  
            
            //Marking,                                    //  <dataType>MarkingStruct</dataType>       
            phy1.setPowerPlantOn(true);            
            //PropulsionSystemsData,                // <dataType>PropulsionSystemDataStructLengthlessArray</dataType>  
            phy1.setRadarCrossSectionSignatureIndex((short) 0);  
            phy1.setSmokePlumePresent(true);            
            phy1.setTentDeployed(true);        
            phy1.setTrailingEffectsCode(TrailingEffectsCodeEnum32.SmallTrail);   // TrailingEffectsCodeEnum32   
            //VectoringNozzleSystemData         //<dataType>VectoringNozzleSystemDataStructLengthlessArray</dataType>
            
            AttributeHandleValueMap pdu = phy1.getAttributeValues();
            

            PhysicalEntity phy2 = new PhysicalEntity();
            phy2.decode(pdu);
            assertTrue(phy2.getAcousticSignatureIndex() == (short) 3);             
            assertTrue(phy2.getAlternateEntityType().getCountryCode() == (short) 3);

            //ArticulatedParametersArray,         // ArticulatedParameterStructLengthlessArray
            assertTrue(phy2.getCamouflageType() == CamouflageEnum32.DesertCamouflage);
            assertTrue(phy2.getDamageState() == DamageStatusEnum32.ModerateDamage);                   
            assertTrue(phy2.getEngineSmokeOn() == true);
            assertTrue(phy2.getFirePowerDisabled() == true);
            assertTrue(phy2.getFlamesPresent() == true);            
            assertTrue(phy2.getForceIdentifier()  == ForceIdentifierEnum8.Neutral_10 );
            assertTrue(phy2.getHasAmmunitionSupplyCap()==true);      
            assertTrue(phy2.getHasFuelSupplyCap()==true);
            assertTrue(phy2.getHasRecoveryCap()==true);
            assertTrue(phy2.getHasRepairCap()==true);
            assertTrue(phy2.getImmobilized()==true);
            assertTrue(phy2.getInfraredSignatureIndex()==2) ;            
            assertTrue(phy2.getIsConcealed() == true);            
            assertTrue(phy2.getLiveEntityMeasuredSpeed() == 2);  
            
            //Marking,                                    //  <dataType>MarkingStruct</dataType>          
            assertTrue(phy2.getPowerPlantOn() == true);            
            //PropulsionSystemsData,                // <dataType>PropulsionSystemDataStructLengthlessArray</dataType>            
            assertTrue(phy2.getRadarCrossSectionSignatureIndex()==0);   //   <dataType>Integer16</dataType>           
            assertTrue(phy2.getSmokePlumePresent() == true);            
            assertTrue(phy2.getTentDeployed() == true);                   
            assertTrue(phy2.getTrailingEffectsCode() == TrailingEffectsCodeEnum32.SmallTrail ) ;   //<dataType>TrailingEffectsCodeEnum32</dataType> 
            //VectoringNozzleSystemData         //<dataType>VectoringNozzleSystemDataStructLengthlessArray</dataType>
            
            log.info("End of TestEncodingDecode after assertTrue Statements");                        
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
