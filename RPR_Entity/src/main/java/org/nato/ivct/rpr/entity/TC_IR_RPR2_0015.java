/**    Copyright 2022, brf (Fraunhofer IOSB)

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

import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.AmphibiousVehicle;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;
import org.nato.ivct.rpr.FomFiles;
import org.nato.ivct.rpr.RprBuilderException;
import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;

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
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NotConnected;

import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

/**
 * Interoperability Requirement : IR-RPR2-0015
 *
 * SuT shall assume default values for optional attributes on instances of
 * BaseEntity.PhysicalEntity and subclasses according to SISO-STD-001-2015."
 */

/* 
 * How to test what the Sut assumes ?   ################ maybe  only in the SUT ?
 * 
 * We can try to get here default values  from the RTI ??? .... has no use for the Questioning
 * 
 * "optional" seems to be a Attribut, if in SISO-STD-001-2015 not declared as
 * (not optional) ? ( like in Table 4 BaseEntity Attributes: EntityIdentifier,
 * EntityType, Spatial )
 * 
 * BaseEntity.PhysicalEntity and subclasses according to SISO-STD-001-2015  are:
 * PhysicalEntity
 *    Plattform
 *             Aircraft
 *             AmphibiousVehicle
 *             GroundVehicle
 *             MultiDomainPlatform
 *             Spacecraft
 *             SubmersibleVessel
 *             SurfaceVessel
 *    Lifeform
 *             Human
 *             NonHuman
 *    CulturalFeature
 *    Munition 
 *    Expendables
 *    Radio 
 *    Sensor
 *    Supplies
 * 
 * so we have to test all Attributes for PhysicalEntity and subclasses if
 * Sut assume default values for optional Attributes
 * 
* PhysicalEntity Attributes:  SISO-STD-001  P. 44  Table 6  25 attributes   all Optional
   Attribute Name                           Default Value
   AcousticSignatureIndex                    0
   AlternateEntityType                       BaseEntity.EntityType
   ArticulatedParametersArray                Empty
   CamouflageType                            Uniform Paint Scheme
   DamageState                               No Damage
   EngineSmokeOn                             False
   FirePowerDisabled                         False
   FlamesPresent                             False
   ForceIdentifier                           Other
   HasAmmunitionSupplyCap                    False
   HasFuelSupplyCap                          False
   HasRecoveryCap                            False
   HasRepairCap                              False
   Immobilized                               False
   InfraredSignatureIndex                     0
   IsConcealed                               False
   LiveEntityMeasuredSpeed                   0
   Marking                                   Empty
   PowerPlantOn                              False
   PropulsionSystemsData                     Empty
   RadarCrossSectionSignatureIndex           0
   SmokePlumePresent                         False                      
   TentDeployed                              False
   TrailingEffectsCode                      False
   VectoringNozzleSystemData                 Empty
 
 * 
 * 
* Plattform : Table 8 16 Attributes all Optional
  AfterburnerOn            False
  AntiCollisionLightsOn    False
  BlackOutBrakeLightsOn    False
  BlackOutLightsOn         False
  BrakeLightsOn            False
  FormationLightsOn        False
  HatchState               NotApplicable
  HeadLightsOn             False
  InteriorLightsOn         False
  LandingLightsOn          False
  LauncherRaised           False
  NavigationLightsOn       False
  RampDeployed             False
  RunningLightsOn          False
  SpotLightsOn             False
  TailLightsOn             False
 

 * Subclasses of Plattform 
 * Aircraft                   attributless
 * AmphibiousVehicle           attributless
 * GroundVehicle               attributless
 * MultiDomainPlatform         attributless
 * Spacecraft                  attributless
 * SubmersibleVessel           attributless
 * SurfaceVessel               attributless
 * 
 * Lifeform : Table 10 5 attributes all Optional
 *       Human      attributless
 *       NonHuman   attributless
 * 
 * CulturalFeature Table 11 3 Attributes all Optional
 * 
 * Munition Table 12 LauncherFlashPresent optional
 * 
 * Expendables no attributes
 * 
 * Radio attributless
 * 
 * Sensor Table 13 5 attributes all Optional
 * 
 * Sensor Supplies has no attributes
 * 
 * 
 * 
 *  1) we have to subscribe to all of the attributes ( to test we begin with quite a few of physical or Platform)
 * 
 *  2) make discoverObjectInstance ready to receive Informations about ObjectClasses and ObjectInstances
 *     and store the Information  in a Hashmap with reference-Classes
 * 
 * 
 * 
 * 
 * 
 */

public class TC_IR_RPR2_0015 extends AbstractTestCaseIf {

    RTIambassador rtiAmbassador = null;
    FederateAmbassador tcAmbassador = null;
    Logger logger = null;    
    
    Semaphore physicalEntityDiscovered = new Semaphore(0);
    HashMap<ObjectInstanceHandle, PhysicalEntity> knownPhysicalEntitys = new HashMap<>();
    
    PhysicalEntity toTestPhyEntity;
    PhysicalEntity[] physEntityWorkList;

    public TC_IR_RPR2_0015() {
        super();
        // TODO Auto-generated constructor stub
    }

    class TestCaseAmbassador extends NullFederateAmbassador {
        
        @Override
        public void discoverObjectInstance(
                ObjectInstanceHandle theObjectInstanceH,
                ObjectClassHandle theObjectClassH,
                String objectName,
                FederateHandle producingFederate)
                throws FederateInternalError {
            logger.trace("discoverObjectInstance {} with producingFederate {}", theObjectInstanceH, producingFederate);
             logger.info("# discoverObjectInstance with FederateHandle ");  
            discoverObjectInstance(theObjectInstanceH, theObjectClassH, objectName);
        }        
        
        public void discoverObjectInstance(
                ObjectInstanceHandle theObjectInstanceH,
                ObjectClassHandle theObjectClassH,
                String objectName)
                throws FederateInternalError {
            logger.trace("discoverObjectInstance {}", theObjectInstanceH);
            logger.debug("### discoverObjectInstance without FederateHandle ");
            
            
            
            // let's see what are the Classnames in our workList     Debug
            logger.debug("Classnames out of the to testList: ");
            for (PhysicalEntity phyEnt : physEntityWorkList) {
                String classnameFromWorklist = phyEnt.getHlaClassName();
                logger .debug(classnameFromWorklist);                
            }            
            
            try {
             // Tests and Debug   let's see what we get
                String receivedClassName = rtiAmbassador.getObjectClassName(theObjectClassH);
                logger.debug("## discoverObjectInstance:reveived ObjectClassHandle with rti-ObjectClassName: "
                              + receivedClassName + "\n");                
                
                // End of Tests and Debug
            } catch (Exception e) {
                logger.error("discoverObjectInstance received Exception", e);
            }
            
            
            // now we have to store the received Objects to work with it in perform Test
            /*
            try {
                // store a new aircraft Object with the received ObjectInstanceHandle
                if (rtiAmbassador.getObjectClassName(theObjectClassH).equals("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.Aircraft")) {
                    String tempObjectClassName = rtiAmbassador.getObjectClassName(theObjectClassH); // Debug
                    logger.debug("### discoverObjectInstance: got this ObjectKlassName ##### :   " + tempObjectClassName);
                    Aircraft aircr = new Aircraft();
                    aircr.setObjectHandle(theObjectInstanceH);
                    knownAircraftEntities.put(theObjectInstanceH, aircr);
                }        
            } catch (RTIinternalError| NotConnected |  InvalidObjectClassHandle | FederateNotExecutionMember |  RprBuilderException e) {
                logger.error("discoverObjectInstance  received Exception  ", e);
            }
            */    
            
        }
    }


    @Override
    protected void logTestPurpose(Logger logger) {
        String msg = "Test Case Purpose: ";
        msg += "SuT shall assume default values for optional attributes on instances of ";
        msg += "BaseEntity.PhysicalEntity  and subclasses according to SISO-STD-001-2015.";

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
             URL[] fomList = new FomFiles().addRPR_BASE().addRPR_Enumerations().addRPR_Foundation()
                    .addRPR_Physical().addRPR_Switches().get();

            rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);
            try {
                rtiAmbassador.createFederationExecution(federationName, fomList);
            } catch (FederationExecutionAlreadyExists ignored) {
            }

            rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName, fomList);

        } catch (RTIinternalError | ConnectionFailed | InvalidLocalSettingsDesignator | UnsupportedCallbackModel
                | AlreadyConnected | CallNotAllowedFromWithinCallback | CouldNotCreateLogicalTimeFactory
                | FederationExecutionDoesNotExist | InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD | SaveInProgress
                | RestoreInProgress | FederateAlreadyExecutionMember | NotConnected e) {
            throw new TcInconclusiveIf(e.getMessage());
        }
    }

    @Override
    protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
        logger.info("perform test {}", this.getClass().getName());
        
        BaseEntity.initialize(rtiAmbassador); // to adjust

        try {
          
            //PhysicalEntity toTestPhyEntity;
            //PhysicalEntity[] physEntityWorkList;
            
            toTestPhyEntity = new PhysicalEntity() ;  
            physEntityWorkList = new Platform[] { new Aircraft(), new AmphibiousVehicle() };
            // platformWorkList = new Platform[] {new Aircraft() , new AmphibiousVehicle(),
            // new GroundVehicle(), new Spacecraft() ,
            // new SurfaceVessel() , new SubmersibleVessel(), new MultiDomainPlatform() };
                    
            
            // for testing  we have to receive all attributes from physical entity  ( brf)
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.AcousticSignatureIndex);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.AlternateEntityType);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.ArticulatedParametersArray);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.CamouflageType);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.DamageState);

            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.ForceIdentifier);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.HasAmmunitionSupplyCap);

            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.HasFuelSupplyCap);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.HasRecoveryCap);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.HasRepairCap);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.Immobilized);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.InfraredSignatureIndex);

            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.LiveEntityMeasuredSpeed);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.Marking);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.PowerPlantOn);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.PropulsionSystemsData);

            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.RadarCrossSectionSignatureIndex);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.SmokePlumePresent);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.TentDeployed);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.TrailingEffectsCode);
            toTestPhyEntity.addSubscribe(PhysicalEntity.Attributes.VectoringNozzleSystemData);
            
            for (PhysicalEntity phyEnt : physEntityWorkList) {
                phyEnt.subscribe();
            }
              

            boolean gotEnoughAtttributes = true;
            while (!gotEnoughAtttributes) {
                // the Test ......
            }

        } catch (Exception e) {
            throw new TcInconclusiveIf(e.getMessage());
        }

        logger.info("test {} passed", this.getClass().getName());
    }

    @Override
    protected void postambleAction(Logger logger) throws TcInconclusiveIf {
        // TODO Auto-generated method stub

    }
}
