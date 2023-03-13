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

import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

import org.nato.ivct.rpr.*;
import org.nato.ivct.rpr.datatypes.EntityIdentifierStruct;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;
import org.nato.ivct.rpr.datatypes.SpatialStaticStruct;
import org.nato.ivct.rpr.datatypes.SpatialVariantStruct;
import org.nato.ivct.rpr.objects.Aircraft;
import org.nato.ivct.rpr.objects.HLAobjectRoot;
import org.nato.ivct.rpr.objects.Munition;
import org.nato.ivct.rpr.objects.BaseEntity;
import org.nato.ivct.rpr.objects.PhysicalEntity;

public class AircraftApp extends NullFederateAmbassador {
    
    public enum CmdLineOptions {
        provokeFlyAircraft,
        provokeMissingMandatoryAttribute,
        provokeDeadReckoningError
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
	private String federateName = "RefFedAircraft";
	private int nrOfCycles = 4000;
	private RTIambassador rtiAmbassador;
	private FederateHandle fedHandle;

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

	private void run() {
		try {
			RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
			rtiAmbassador = rtiFactory.getRtiAmbassador();
			FederateAmbassador nullAmbassador = new NullFederateAmbassador();
			ArrayList<URL> fomList = new FomFiles()
				.addRPR_BASE()
				.addRPR_Enumerations()
				.addRPR_Foundation()
				.addRPR_Physical()
				.addRPR_Switches()
				.addRPR_Warfare()
				.get();
			rtiAmbassador.connect(nullAmbassador, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(this.federationName, fomList.toArray(new URL[fomList.size()]));
			} catch (FederationExecutionAlreadyExists ignored) { }
			fedHandle = rtiAmbassador.joinFederationExecution(this.federateName, this.federationName, fomList.toArray(new URL[fomList.size()]));

            HLAobjectRoot.initialize(rtiAmbassador);
            Aircraft aircraft = new Aircraft();
            aircraft.addPublish(BaseEntity.Attributes.EntityIdentifier);
            aircraft.addPublish(BaseEntity.Attributes.EntityType);
            aircraft.addPublish(BaseEntity.Attributes.Spatial);
            
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
                        
            aircraft.register();

			Munition munitionProxy = new Munition();
            munitionProxy.addPublish(BaseEntity.Attributes.EntityIdentifier);
			munitionProxy.publishLauncherFlashPresent();
			munitionProxy.publish();
			munitionProxy.register();			

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
					System.out.println("provokeFlyAircraft: " +i);  // Debug
				}
			}

		} catch (final Exception e) {
			logErrorAndExit("Exception: {}", e.toString());
		}
	}
}
