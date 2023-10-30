/**    Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.refFed;

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
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.FederateAmbassador.SupplementalReflectInfo;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.InvalidAttributeHandle;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;
import javax.naming.NotContextException;

import org.slf4j.LoggerFactory;

import org.nato.ivct.rpr.*;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.SpatialStaticStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;
import org.nato.ivct.rpr.interactions.WeaponFire;
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.AmphibiousVehicle;
import org.nato.ivct.rpr.objects.HLAobjectRoot;
import org.nato.ivct.rpr.objects.Munition;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;
import org.nato.ivct.rpr.objects.Platform;

public class AircraftApp extends NullFederateAmbassador {
    
    public enum CmdLineOptions {
        provokeFlyAircraft,                           // -pFA
        provokeMissingMandatoryAttribute,
        provokeDeadReckoningError,
        provokeReflectedAttributesReport            // -pRAR
    }

	public class Option {
		private String  optionNameLong;
		private String  optionNameShort;
		private boolean enabled;

		Option(String optionNameLong, String optionNameShort) {
			this.optionNameLong = optionNameLong;
			this.optionNameShort = optionNameShort;
			this.enabled = false;
		}
	}

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Aircraft.class);
	private static final Map<CmdLineOptions, Option> options = new LinkedHashMap<CmdLineOptions, Option>(); // LinkedHashMap preserves order

	private boolean provoke(CmdLineOptions optionId) { return options.get(optionId).enabled; }
    private String rtiHost = "localhost";
	private String federationName = "TestFederation";
	private String federateName = "Aircraft";
	private String federateType = "RefFed";
	private int nrOfCycles = 4000;   // 4000
    private int nrOfCyclesState;
	private RTIambassador rtiAmbassador;
	private FederateHandle fedHandle;
	
	
	HashMap<ObjectInstanceHandle, Aircraft> knownAircraftEntities = new HashMap<>();		
	HashMap<ObjectInstanceHandle, ObjectClassHandle> knownObjectInstanceObjectClassHandles = new HashMap<>();
	final HashMap<String, Integer> reflectedAttributeReport = new HashMap<>();

		
	public static void main(final String[] args) {
		AircraftApp aircraft = new AircraftApp(args);
		logger.info("AircraftApp running");
		aircraft.run();
		logger.info("AircraftApp terminated");
	}

	private static void logErrorAndExit(String errorMsg, Object... args) {
		logger.error(errorMsg, args);
		System.exit(1);
	}

	protected AircraftApp(final String[] args) {
		// Initialize provocation options
		for (CmdLineOptions optionId : CmdLineOptions.values()) {
			// Create optionNameLong from optionId
			String optionNameLong = "-" + optionId.toString();

			// Create optionNameShort from optionNameLong
			String optionNameShort = "-p";
			for (int i = 0; i < optionNameLong.length(); i++)
				if (Character.isUpperCase(optionNameLong.charAt(i)))
					optionNameShort += optionNameLong.charAt(i);

			// Check if no duplicate optionNameShort in options
			for (Map.Entry<CmdLineOptions, Option> tmpEntry : options.entrySet()) {
				Option tmpOption = tmpEntry.getValue();
				if (optionNameShort.equals(tmpOption.optionNameShort))
					logErrorAndExit("Duplicate short option {} for options {} and {}", optionNameShort, optionNameLong, tmpOption.optionNameLong);
			}

			// Store in options
			options.put(optionId, new Option(optionNameLong, optionNameShort));
		}

		// Print help info
		String helpString = "Syntax: Aircraft\n"
				+ "    [-rtiHost rtiHost], e.g. -rtiHost localhost or -rtiHost localhost:8989" + ", default: " + rtiHost + "\n"
				+ "    [-federationName federationName]" + ", default: " + federationName + "\n"
				+ "    [-federateName federateName]" + ", default: " + federateName + "\n";
		for (Map.Entry<CmdLineOptions, Option> entry : options.entrySet()) {
			Option option = entry.getValue();
			helpString += String.format("    [%-13s | %s]\n", option.optionNameShort, option.optionNameLong);
		}
		logger.info(helpString);

		// Get command line arguments
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-rtiHost")) {
				i++;
				rtiHost = args[i];
			} else if (args[i].equals("-federationName")) {
				i++;
				federationName = args[i];
			} else if (args[i].equals("-federateName")) {
				i++;
				federateName = args[i];
			} else {
				boolean optionRecognized = false;
				for (Map.Entry<CmdLineOptions, Option> entry : options.entrySet()) {
					Option option = entry.getValue();
					if (args[i].equals(option.optionNameShort) || args[i].equals(option.optionNameLong)) {
						option.enabled = true;
						optionRecognized = true;
					}
				}
				if (!optionRecognized)
					logErrorAndExit("Unknown command line argument: {}", args[i]);
			}
		}
    }
	
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
	
	@Override
    public void discoverObjectInstance(
            ObjectInstanceHandle theObjectInstanceH,
            ObjectClassHandle theObjectClassH,
            String objectName)
            throws FederateInternalError {
        logger.trace("discoverObjectInstance {}", theObjectInstanceH);
        logger.debug("### discoverObjectInstance without FederateHandle ");
        
        // notice the  received ObjectInstanceHandle, ObjectClassHandle in  knownObjectInstanceObjectClassHandles;
        knownObjectInstanceObjectClassHandles.put(theObjectInstanceH, theObjectClassH);   
     
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
    }
    
     @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
            byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
            LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle,
            SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        logger.trace("reflectAttributeValues with retractionHandle");
        reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
    }
    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
            byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
            LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo)
            throws FederateInternalError {
        logger.trace("reflectAttributeValues without  MessageRetractionHandle");
        reflectAttributeValues(theObject, theAttributes, userSuppliedTag, sentOrdering, theTransport, reflectInfo);
    }
    
    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObjectInstancH, AttributeHandleValueMap theAttributes,
            byte[] userSuppliedTag,  OrderType sentOrdering, TransportationTypeHandle theTransport,
            SupplementalReflectInfo reflectInfo)  throws FederateInternalError {
            logger.trace("reflectAttributeValues without LogicalTime, receivedOrdering,  MessageRetractionHandle ");       

        // Only get the names of Attributes  1 
        try { 
            ObjectClassHandle tempObjectClassHandle =  null;
            tempObjectClassHandle = knownObjectInstanceObjectClassHandles.get(theObjectInstancH);
        
            for (AttributeHandle atH : theAttributes.keySet()) {
                String tempAttributname=  rtiAmbassador.getAttributeName(tempObjectClassHandle, atH) ;            
                collectTestReport(tempAttributname);                
            }         
        } catch ( InvalidObjectClassHandle | AttributeNotDefined | InvalidAttributeHandle | FederateNotExecutionMember |   NotConnected | RTIinternalError e) {
            logger.error("reflectAttributeValues received Exception" ,e );
        }
        
        // Decode the received Attributes
        Aircraft  tempKnownAircraftEntity = knownAircraftEntities.get(theObjectInstancH);        
        if (tempKnownAircraftEntity != null) {
            tempKnownAircraftEntity.clear();
            try {
                tempKnownAircraftEntity.decode(theAttributes);      
                logger.debug("### reflectAttributeValues: get we sended Values for e.g. AfterburnerOn now ?" 
                              + tempKnownAircraftEntity.getAfterburnerOn() );      
            } catch (Exception e) {
                logger.error("reflectAttributeValues received Exception", e);
            }
        }        
    }

	private void run() {
       
		try {
			RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
			rtiAmbassador = rtiFactory.getRtiAmbassador();
			
			// That should be the normal normal loading procedure
			URL[] fomList = new FomFiles()
				.addTmpRPR_BASE()
				.addTmpRPR_Switches()
				.addTmpRPR_Enumerations()
				.addTmpRPR_Foundation()
				.addTmpRPR_Warfare()
				.addTmpRPR_Physical()
				.get();

			rtiAmbassador.connect(this, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(this.federationName, fomList);
			} catch (FederationExecutionAlreadyExists ignored) { }
			fedHandle = rtiAmbassador.joinFederationExecution(this.federateName, this.federateType, this.federationName, fomList);

            HLAobjectRoot.initialize(rtiAmbassador);
            Aircraft aircraft = new Aircraft();
            aircraft.addPublish(BaseEntity.Attributes.EntityIdentifier);
            aircraft.addPublish(BaseEntity.Attributes.EntityType);
            aircraft.addPublish(BaseEntity.Attributes.Spatial);
            
            // for e.g. Test TC_IR_RPR2_0012  we need to Publish physical attributes
            aircraft.addPublish(PhysicalEntity.Attributes.CamouflageType);
            aircraft.addPublish(PhysicalEntity.Attributes.DamageState);
            aircraft.addPublish(PhysicalEntity.Attributes.EngineSmokeOn);
            aircraft.addPublish(PhysicalEntity.Attributes.FirePowerDisabled);
            aircraft.addPublish(PhysicalEntity.Attributes.FlamesPresent);
            aircraft.addPublish(PhysicalEntity.Attributes.Immobilized);
            aircraft.addPublish(PhysicalEntity.Attributes.IsConcealed);
            aircraft.addPublish(PhysicalEntity.Attributes.PowerPlantOn);
            aircraft.addPublish(PhysicalEntity.Attributes.SmokePlumePresent);
            aircraft.addPublish(PhysicalEntity.Attributes.TentDeployed);
            aircraft.addPublish(PhysicalEntity.Attributes.TrailingEffectsCode);
            // aircraft.register();  // see below
            
            // change some 'simple' boolean Attributes, for other Attributes e.g. CamouflageType we may need  a Struct            
            aircraft.setEngineSmokeOn(true);            
            aircraft.setFirePowerDisabled(true);       
            aircraft.setFlamesPresent(true);           
            aircraft.setIsConcealed(true);          
            aircraft.setTentDeployed(true);
            // update is below            
            
            // for testing  we have to receive all attributes from physical entity  ( brf)
            aircraft.addSubscribe(PhysicalEntity.Attributes.AcousticSignatureIndex);
            aircraft.addSubscribe(PhysicalEntity.Attributes.AlternateEntityType);
            aircraft.addSubscribe(PhysicalEntity.Attributes.ArticulatedParametersArray);
            aircraft.addSubscribe(PhysicalEntity.Attributes.CamouflageType);
            aircraft.addSubscribe(PhysicalEntity.Attributes.DamageState);

            aircraft.addSubscribe(PhysicalEntity.Attributes.EngineSmokeOn);
            aircraft.addSubscribe(PhysicalEntity.Attributes.FirePowerDisabled);
            aircraft.addSubscribe(PhysicalEntity.Attributes.FlamesPresent);
            aircraft.addSubscribe(PhysicalEntity.Attributes.ForceIdentifier);
            aircraft.addSubscribe(PhysicalEntity.Attributes.HasAmmunitionSupplyCap);

            aircraft.addSubscribe(PhysicalEntity.Attributes.HasFuelSupplyCap);
            aircraft.addSubscribe(PhysicalEntity.Attributes.HasRecoveryCap);
            aircraft.addSubscribe(PhysicalEntity.Attributes.HasRepairCap);
            aircraft.addSubscribe(PhysicalEntity.Attributes.Immobilized);
            aircraft.addSubscribe(PhysicalEntity.Attributes.InfraredSignatureIndex);

            aircraft.addSubscribe(PhysicalEntity.Attributes.IsConcealed);
            aircraft.addSubscribe(PhysicalEntity.Attributes.LiveEntityMeasuredSpeed);
            aircraft.addSubscribe(PhysicalEntity.Attributes.Marking);
            aircraft.addSubscribe(PhysicalEntity.Attributes.PowerPlantOn);
            aircraft.addSubscribe(PhysicalEntity.Attributes.PropulsionSystemsData);

            aircraft.addSubscribe(PhysicalEntity.Attributes.RadarCrossSectionSignatureIndex);
            aircraft.addSubscribe(PhysicalEntity.Attributes.SmokePlumePresent);
            aircraft.addSubscribe(PhysicalEntity.Attributes.TentDeployed);
            aircraft.addSubscribe(PhysicalEntity.Attributes.TrailingEffectsCode);
            aircraft.addSubscribe(PhysicalEntity.Attributes.VectoringNozzleSystemData);
            
            
            // for testing  we have to receive all attributes from Platform entity  ( brf)            
            aircraft.addSubscribe(Platform.Attributes.AfterburnerOn);            
            aircraft.addSubscribe(Platform.Attributes.AntiCollisionLightsOn);
            aircraft.addSubscribe(Platform.Attributes.BlackOutBrakeLightsOn);
            aircraft.addSubscribe(Platform.Attributes.BlackOutLightsOn);
            aircraft.addSubscribe(Platform.Attributes.BrakeLightsOn);
            aircraft.addSubscribe(Platform.Attributes.FormationLightsOn);
            aircraft.addSubscribe(Platform.Attributes.HatchState);
            aircraft.addSubscribe(Platform.Attributes.HeadLightsOn);
            aircraft.addSubscribe(Platform.Attributes.InteriorLightsOn);
            aircraft.addSubscribe(Platform.Attributes.LandingLightsOn);
            aircraft.addSubscribe(Platform.Attributes.LauncherRaised);
            aircraft.addSubscribe(Platform.Attributes.NavigationLightsOn);
            aircraft.addSubscribe(Platform.Attributes.RampDeployed);
            aircraft.addSubscribe(Platform.Attributes.RunningLightsOn);
            aircraft.addSubscribe(Platform.Attributes.SpotLightsOn);
            aircraft.addSubscribe(Platform.Attributes.TailLightsOn);             
            
            aircraft.subscribe();
            
            // for e.g. Test TC_IR_RPR2_PHY_0004  we have to publish some platform attributes (brf)
            aircraft.addPublish(Platform.Attributes.AfterburnerOn);
            aircraft.addPublish(Platform.Attributes.AntiCollisionLightsOn);
            aircraft.addPublish(Platform.Attributes.BlackOutBrakeLightsOn);
            aircraft.addPublish(Platform.Attributes.BlackOutLightsOn);
            aircraft.addPublish(Platform.Attributes.BrakeLightsOn);
            aircraft.addPublish(Platform.Attributes.FormationLightsOn);
            aircraft.addPublish(Platform.Attributes.HatchState);
            aircraft.addPublish(Platform.Attributes.HeadLightsOn);
            aircraft.addPublish(Platform.Attributes.InteriorLightsOn);
            aircraft.addPublish(Platform.Attributes.LandingLightsOn);
            aircraft.addPublish(Platform.Attributes.LauncherRaised);
            aircraft.addPublish(Platform.Attributes.NavigationLightsOn);
            aircraft.addPublish(Platform.Attributes.RampDeployed);
            aircraft.addPublish(Platform.Attributes.RunningLightsOn);
            aircraft.addPublish(Platform.Attributes.SpotLightsOn);
            aircraft.addPublish(Platform.Attributes.TailLightsOn);
            aircraft.register(); 
        
            aircraft.setAfterburnerOn(true);
            aircraft.setAntiCollisionLightsOn(true);
            aircraft.setBlackOutBrakeLightsOn(true);  // non applicable for Aircraft
            aircraft.setBlackOutLightsOn(true);       // non applicable for Aircraft
            aircraft.setBrakeLightsOn(true);          // non applicable for Aircraft
            aircraft.setFormationLightsOn(true);
            aircraft.setHatchState(true);             // non applicable for Aircraft
            aircraft.setHeadLightsOn(true);           // non applicable for Aircraft
            aircraft.setInteriorLightsOn(true);
            aircraft.setLandingLightsOn(true);
            aircraft.setLauncherRaised(true);         // non applicable for Aircraft
            aircraft.setNavigationLightsOn(true);
            aircraft.setRampDeployed(true);           // non applicable for Aircraft        
            aircraft.setRunningLightsOn(true);        // non applicable for Aircraft
            aircraft.setSpotLightsOn(true);
            aircraft.setTailLightsOn(true);           // non applicable for Aircraft
            
   
			Munition munitionProxy = new Munition();
            munitionProxy.addPublish(BaseEntity.Attributes.EntityIdentifier);
            // try if we get this in TC_IR_RPR_PHY_0001
            //munitionProxy.addPublish(PhysicalEntity.Attributes.CamouflageType);  // DEBUG
            munitionProxy.publishLauncherFlashPresent();
			munitionProxy.publish();
			munitionProxy.register();
			
			WeaponFire wf = new WeaponFire();
			wf.publish();

			EntityIdentifierStruct entityIdentifier = aircraft.getEntityIdentifier();
			entityIdentifier.setEntityNumber((short) 1);
            entityIdentifier.getFederateIdentifier().setApplicationID((short) 2);
            entityIdentifier.getFederateIdentifier().setSiteID((short) 3);
			aircraft.setEntityIdentifier(entityIdentifier);
			
			EntityTypeStruct entityType = aircraft.getEntityType();
            entityType.setEntityKind((byte) 0xa);
            entityType.setDomain((byte) 0xb);
            entityType.setCountryCode((short) 3);
            entityType.setCategory((byte) 0xc);
            entityType.setSubcategory((byte) 0xd);
            entityType.setSpecific((byte) 0xe);
            entityType.setExtra((byte) 0xf);
			aircraft.setEntityType(entityType);
			aircraft.update();
			

			// e.g. for Tests IR-RPR-PHY-0001  and IR-RPR-PHY-0003
            if (provoke(CmdLineOptions.provokeReflectedAttributesReport)) {
                Thread printreflectedAttributReport = new Thread(() -> {                    
                    int threadSleepTime = 10000;        // report all 10 sec as long the main thread is running

                    // Ending somehow this Tasks,  maybe  as long as the aircraft flies (provokeFlyAircraft)
                    while (nrOfCyclesState < nrOfCycles -3  ) {
                        logger.info(" ----------------  print reflected attributes --------------  ");

                        for (String s : reflectedAttributeReport.keySet()) {
                            String ausgabe = "number of updats for  " + s + ":  " + reflectedAttributeReport.get(s);
                            logger.info(ausgabe);
                        }
                        try {
                            Thread.sleep(threadSleepTime);
                        } catch (InterruptedException e) {
                            logger.error("run  printreflectedAttributReport has a Problem ");
                            e.printStackTrace();
                        }                        
                        // Test if the Attributes are readable  without beeing changed e.g. DamageState, Status should be the default "No Damage" 
                        // Test if there are default Values for all  .....  ???                       
                        try {                            
                            logger.debug("Test to get defaultValue aircraft.getDamageState() gives out:  " + aircraft.getDamageState() );
                            logger.debug("Test to get defaultValue aircraft.AfterburnerOn() gives out:  " + aircraft.getAfterburnerOn() );
                        } catch (DecoderException | InvalidObjectClassHandle | NotConnected | FederateNotExecutionMember | NameNotFound | RTIinternalError  e) {
                            logger.error("printreflectedAttributReport: reading  a decoded Attribute  has a Problem ");
                            e.printStackTrace();
                        } 
                    }
                });
                printreflectedAttributReport.start();             
            }		        
			
			if (provoke(CmdLineOptions.provokeFlyAircraft)) {
				for (int i=0; i<nrOfCycles; i++) {
					double xPos = 1.0 + Math.sin(i);
					double yPos = 2.0 + Math.sin(i);
					double zPos = 3.0 + Math.sin(i);
					float phi = 4.0f + (float) Math.cos(i);
					float psi = 5.0f + (float) Math.cos(i);
					float theta = 6.0f + (float) Math.cos(i);
					SpatialVariantStruct spatial = aircraft.getSpatial();
					SpatialStaticStruct spatialStatic = spatial.getSpatialStatic();
					spatialStatic.setIsFrozen(false);
					spatialStatic.getWorldLocation().setX(xPos);
					spatialStatic.getWorldLocation().setY(yPos);
					spatialStatic.getWorldLocation().setZ(zPos);
					spatialStatic.getOrientation().setPhi(phi);
					spatialStatic.getOrientation().setPsi(psi);
					spatialStatic.getOrientation().setTheta(theta);
					aircraft.setSpatial(spatial);
					aircraft.update();
					Thread.sleep(1000);
					logger.debug("provokeFlyAircraft: " +i);
					nrOfCyclesState = i;
				}
			}		
			
		} catch (final Exception e) {
			logErrorAndExit("Exception: {}", e.toString());
		}
	}

    public void collectTestReport(String toTestAttribut) {
        String randomTestName = toTestAttribut;
        if (reflectedAttributeReport.get(randomTestName) == null) {
            reflectedAttributeReport.put(randomTestName, 1);
        } else {
            reflectedAttributeReport.put(randomTestName, (reflectedAttributeReport.get(randomTestName) + 1));
        }
    }
 	
}
