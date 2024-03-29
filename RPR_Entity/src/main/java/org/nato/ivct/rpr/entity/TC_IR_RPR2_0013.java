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
import org.nato.ivct.rpr.objects.CulturalFeature;
import org.nato.ivct.rpr.objects.Expendables;
import org.nato.ivct.rpr.objects.GroundVehicle;
import org.nato.ivct.rpr.objects.Human;
import org.nato.ivct.rpr.objects.Lifeform;
import org.nato.ivct.rpr.objects.MultiDomainPlatform;
import org.nato.ivct.rpr.objects.Munition;
import org.nato.ivct.rpr.objects.NonHuman;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;
import org.nato.ivct.rpr.objects.Radio;
import org.nato.ivct.rpr.objects.Sensor;
import org.nato.ivct.rpr.objects.Spacecraft;
import org.nato.ivct.rpr.objects.SubmersibleVessel;
import org.nato.ivct.rpr.objects.Supplies;
import org.nato.ivct.rpr.objects.SurfaceVessel;
import org.nato.ivct.rpr.FomFiles;
import org.slf4j.Logger;

import de.fraunhofer.iosb.tc_lib_if.AbstractTestCaseIf;
import de.fraunhofer.iosb.tc_lib_if.TcFailedIf;
import de.fraunhofer.iosb.tc_lib_if.TcInconclusiveIf;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.TransportationTypeHandle;

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
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

/**
 * Interoperability Requirement :  R-RPR2-0013
 *
 * SuT updates of instance attributes shall, for BaseEntity.PhysicalEntity and subclasses,
 * be valid according to SISO-STD-001-2015 and SISO-STD-001.1-2015.
 */

 /* 
 * What  does "be valid"   mean,  it exist or has a  Default Value  ?
 *  has a correct Name ?
 *  
 *  May be we test here if the  type of the data Representation is 
 *  Float  Integer   Octed
 *  ( SISO-STD-001-205   chapter 6.8.1.1 ? )
 *  
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
 * 
 *   in SISO-STD-001  P. 44  Table 6   "PhysicalEntity Attributes"  they are listed
 *   
 *   
 * PhysicalEntity Attributes:  SISO-STD-001  P. 44  Table 6  25 attributes   all Optional
 *  Attribute Name                           Default Value             DataType
 *  AcousticSignatureIndex                    0                        Integer16
 *  AlternateEntityType                       BaseEntity.EntityType    EntityTypeStruct
 *  ArticulatedParametersArray                Empty                    ArticulatedParameterStructLengthlessArray
 *  CamouflageType                            Uniform Paint Scheme     CamouflageEnum32    
 *  DamageState                               No Damage
 *  EngineSmokeOn                             False
 *  FirePowerDisabled                         False
 *  FlamesPresent                             False
 *  ForceIdentifier                           Other
 *  HasAmmunitionSupplyCap                    False
 *  HasFuelSupplyCap                          False
 *  HasRecoveryCap                            False
 *  HasRepairCap                              False
 *  Immobilized                               False
 *  InfraredSignatureIndex                     0
 *  IsConcealed                               False
 *  LiveEntityMeasuredSpeed                   0
 *  Marking                                   Empty
 *  PowerPlantOn                              False
 *  PropulsionSystemsData                     Empty
 *  RadarCrossSectionSignatureIndex           0
 *  SmokePlumePresent                         False                      
 *  TentDeployed                              False
 *  TrailingEffectsCode                      False
 *  VectoringNozzleSystemData                 Empty
 *   
 *    
 *   the Attributes for SubClasses of PhysicalEntity 
    
* PhysicalEntity-Plattform : Table 8     16 Attributes all Optional all Boolean
* AfterburnerOn           False
* AntiCollisionLightsOn   False
* BlackOutBrakeLightsOn    False
* BlackOutLightsOn         False
* BrakeLightsOn            False
* FormationLightsOn        False
* HatchState               NotApplicable
* HeadLightsOn             False
* InteriorLightsOn         False
* LandingLightsOn          False
* LauncherRaised           False
* NavigationLightsOn       False
* RampDeployed             False
* RunningLightsOn          False
* SpotLightsOn             False
* TailLightsOn             False
* 
*
 * Subclasses of PhysicalEntity-Plattform
 * Aircraft                    attributless
 * AmphibiousVehicle           attributless
 * GroundVehicle               attributless
 * MultiDomainPlatform         attributless
 * Spacecraft                  attributless
 * SubmersibleVessel           attributless
 * SurfaceVessel               attributless
 *   
 * 
 * PhysicalEntity-Lifeform :            5 attributes      Table 10
 * FlashLightsOn            False
 * StanceCode               NotApplicable
 * PrimaryWeaponState       NoWeapon
 * SecondaryWeaponState     NoWeapon
 * ComplianceState          Other
 * 
 * Subclasses of PhysicalEntity-Lifeform
 *      Human               attributless
 *      NonHuman            attributless
 *   
 * PhysicalEntity-CulturalFeature       3 Attributes      Table 11
 *  ExternalLightsOn         False          RPRboolean
 *  InternalHeatSourceOn     False          RPRboolean
 *  InternalLightsOn         False         RPRboolean
 *     
 * PhysicalEntity-Munition   Table 12           
 *  LauncherFlashPresent     False
 *   
 * PhysicalEntity-Expendables   has no attributes
 *   
 * PhysicalEntity-Radio           attributless
 *   
 * PhysicalEntity-Sensor     Table 13      5 attributes
  * Attribute Name         Default Value
 *  AntennaRaised        False
 *  BlackoutLightsOn   False
 *  LightsOn                False
 *  InteriorLightsOn     False
 *  MissionKill             False
 * 
 * PhysicalEntity-Supplies     has no attributes  
 *   
 */


 public class TC_IR_RPR2_0013 extends AbstractTestCaseIf {

     RTIambassador rtiAmbassador = null;
     FederateAmbassador tcAmbassador = null;
     Logger logger = null;

     HashMap<ObjectInstanceHandle, PhysicalEntity> knownPhysicalEntitys = new HashMap<>();

     PhysicalEntity phyEntity;
     private FederateHandle sutHandle;

     class TestCaseAmbassador extends NullFederateAmbassador {

         @Override
         public void discoverObjectInstance(ObjectInstanceHandle theObjectInstanceH, ObjectClassHandle theObjectClassH,
             String objectName) throws FederateInternalError {
             logger.trace("discoverObjectInstance {}", theObjectInstanceH);
             
             try {
                 
             logger.debug("# discoverObjectInstance: reveived ObjectClassHandle with rti-ObjectClassName:  "
                           + rtiAmbassador.getObjectClassName(theObjectClassH)); // Debug
             } catch (Exception e) {
                 logger.error("discoverObjectInstance received Exception", e);
             }
         }

         @Override
         public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass,
                 String objectName, FederateHandle producingFederate) throws FederateInternalError {
             logger.trace("discoverObjectInstance {} with producingFederate {}", theObject, producingFederate);
             discoverObjectInstance(theObject, theObjectClass, objectName);
         }
     }
	
     @Override
     protected void logTestPurpose(Logger logger) {
         String msg = "Test Case Purpose: ";
         msg += "SuT updates of instance attributes shall, for BaseEntity.PhysicalEntity and subclasses, ";
         msg += "be valid according to SISO-STD-001-2015 and SISO-STD-001.1-2015. ";
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
             URL[] fomList = new FomFiles()
                 .addRPR_BASE()
                 .addRPR_Enumerations()
                 .addRPR_Foundation()
                 .addRPR_Physical()
                 .addRPR_Switches()
                 .get();

             rtiAmbassador.connect(tcAmbassador, CallbackModel.HLA_IMMEDIATE);
             try {
                 rtiAmbassador.createFederationExecution(federationName, fomList);
             } catch (FederationExecutionAlreadyExists ignored) {
             }
             rtiAmbassador.joinFederationExecution(this.getClass().getSimpleName(), federationName, fomList);

         } catch (RTIinternalError | ConnectionFailed | InvalidLocalSettingsDesignator | UnsupportedCallbackModel
                 | AlreadyConnected | CallNotAllowedFromWithinCallback | CouldNotCreateLogicalTimeFactory
                 | FederationExecutionDoesNotExist | InconsistentFDD | ErrorReadingFDD | CouldNotOpenFDD
                 | SaveInProgress | RestoreInProgress | FederateAlreadyExecutionMember | NotConnected e) {
             throw new TcInconclusiveIf(e.getMessage());
         }
     }
	
		
     @Override
     protected void performTest(Logger logger) throws TcInconclusiveIf, TcFailedIf {
         logger.info("perform test {}", this.getClass().getName());
     
      // we have to subscribe to all Attributes of al   Classes  BaseEntity.PhysicalEntity and subclasses
         
      try {
          PhysicalEntity.initialize(rtiAmbassador);

          // use a Array to collect all needed Entities
          ArrayList<BaseEntity> entityToTestList = new ArrayList<BaseEntity>();

          // TODO brf now subscribe to all Attributes
          phyEntity = new PhysicalEntity();
          entityToTestList.add(phyEntity);

          phyEntity.addSubscribe(BaseEntity.Attributes.EntityIdentifier);
          phyEntity.addSubscribe(BaseEntity.Attributes.EntityType);
          phyEntity.addSubscribe(BaseEntity.Attributes.Spatial);

          phyEntity.addSubscribe(PhysicalEntity.Attributes.CamouflageType);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.DamageState);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.Immobilized);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.PowerPlantOn);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.SmokePlumePresent);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.TentDeployed);
          phyEntity.addSubscribe(PhysicalEntity.Attributes.TrailingEffectsCode);
         
         // TODO    PhysicalEntity further  attributes to  addSubscribe
         /* 
         AcousticSignatureIndex                    0
         *  AlternateEntityType                       BaseEntity.EntityType
         *  ArticulatedParametersArray                Empty
         *  CamouflageType                            Uniform Paint Scheme
         *  DamageState                               No Damage
         *  EngineSmokeOn                             False
         *  FirePowerDisabled                         False
         *  FlamesPresent                             False
         *  ForceIdentifier                           Other
         *  HasAmmunitionSupplyCap                    False
         *  HasFuelSupplyCap                          False
         *  HasRecoveryCap                            False
         *  HasRepairCap                              False
         *  Immobilized                               False
         *  InfraredSignatureIndex                     0
         *  IsConcealed                               False
         *  LiveEntityMeasuredSpeed                   0
         *  Marking                                   Empty
         *  PowerPlantOn                              False
         *  PropulsionSystemsData                     Empty
         *  RadarCrossSectionSignatureIndex           0
         *  SmokePlumePresent                         False                      
         *  TentDeployed                              False
         *  TrailingEffectsCode                      False
         *  VectoringNozzleSystemData                 Empty
         */
         
         // Plattform  16 attributes
         Platform platfEntity = new Platform();
         entityToTestList.add(platfEntity);
         platfEntity.addSubscribe(Platform.Attributes.AfterburnerOn);
         platfEntity.addSubscribe(Platform.Attributes.AntiCollisionLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.BlackOutBrakeLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.BlackOutLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.BrakeLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.FormationLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.HatchState);
         platfEntity.addSubscribe(Platform.Attributes.HeadLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.InteriorLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.LandingLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.LauncherRaised);
         platfEntity.addSubscribe(Platform.Attributes.NavigationLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.RampDeployed);
         platfEntity.addSubscribe(Platform.Attributes.RunningLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.SpotLightsOn);
         platfEntity.addSubscribe(Platform.Attributes.TailLightsOn);
         
         // Lifeform 5 attr
         Lifeform lifeFormEntity = new Lifeform();
         entityToTestList.add(lifeFormEntity);
         lifeFormEntity.addSubscribe(Lifeform.Attributes.FlashLightsOn);
         // TODO 4 further attributes
         /*
          * StanceCode,             // StanceCodeEnum32
          * PrimaryWeaponState,    // WeaponStateEnum32
          * SecondaryWeaponState, // WeaponStateEnum32
          * ComplianceState        //ComplianceStateEnum32
          */
 
         
         // cultural Feature   3 attr
         CulturalFeature cultFeatEntity = new CulturalFeature();
         entityToTestList.add(cultFeatEntity);         
         cultFeatEntity.addSubscribe(CulturalFeature.Attributes.ExternalLightsOn);
         cultFeatEntity.addSubscribe(CulturalFeature.Attributes.InternalHeatSourceOn);
         cultFeatEntity.addSubscribe(CulturalFeature.Attributes.InternalLightsOn);         
         
         // Munition           1 attr
         Munition munitionEntity = new Munition();
         entityToTestList.add(munitionEntity);
         munitionEntity.addSubscribe(Munition.Attributes.LauncherFlashPresent);         
         
         // Expendables      0 attributes
         Expendables expendabEntity = new Expendables();
         entityToTestList.add(expendabEntity);
         
         // Radio            0 attributes
         Radio radioEntity = new Radio();
         entityToTestList.add(radioEntity);
        
        // Sensor  5 attributes
        Sensor  sensoreEntity = new Sensor();
        entityToTestList.add(sensoreEntity);        
        sensoreEntity.addSubscribe(Sensor.Attributes.AntennaRaised);
        sensoreEntity.addSubscribe(Sensor.Attributes.BlackoutLightsOn);
        sensoreEntity.addSubscribe(Sensor.Attributes.LightsOn);
        sensoreEntity.addSubscribe(Sensor.Attributes.InteriorLightsOn);
        sensoreEntity.addSubscribe(Sensor.Attributes.MissionKill);
         
        // Supplies  0 attributes
        Supplies suppliesEntity = new Supplies();
        entityToTestList.add(suppliesEntity);
        
        // we need to subscribe to all Objects
        for (BaseEntity bE : entityToTestList) {
            logger.debug("# in performTest subscribing all Elements of PhysicalEntityToTestList now "
                    + bE.getHlaClassName()); // Debug
            bE.subscribe();
        }
             
             for (int i = 0; i < 10; i++) {  // Testing for 10 Sec
              //DOTO Testing something here
              // ...
             Thread.sleep(1000);
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
