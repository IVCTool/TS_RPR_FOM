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
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

import org.nato.ivct.rpr.*;
import org.nato.ivct.rpr.datatypes.EntityTypeStruct;

public class AircraftApp extends NullFederateAmbassador {
    
    public enum CmdLineOptions {
        FlyAircraft,
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
			this.enabled = true;
		}
	}

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Aircraft.class);
	private static final Map<CmdLineOptions, Option> options = new LinkedHashMap<CmdLineOptions, Option>(); // LinkedHashMap preserves order

	private boolean provoke(CmdLineOptions optionId) { return options.get(optionId).enabled; }
    private String rtiHost = "localhost";
	private String federationName = "TestFederation";
	private String federateName = "RefFedAircraft";
	private int nrOfCycles = 40;
	private RTIambassador rtiAmbassador;

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

	private AircraftApp(final String[] args) {
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
				.get();
			rtiAmbassador.connect(nullAmbassador, CallbackModel.HLA_IMMEDIATE);
			try {
				rtiAmbassador.createFederationExecution(this.federationName, fomList.toArray(new URL[fomList.size()]));
			} catch (FederationExecutionAlreadyExists ignored) { }
			rtiAmbassador.joinFederationExecution(this.federateName, this.federationName, fomList.toArray(new URL[fomList.size()]));

            HLAobjectRoot.initialize(rtiAmbassador);
            Aircraft aircraft = new Aircraft();
            aircraft.addPublish(BaseEntity.Attributes.EntityIdentifier);
            aircraft.addPublish(BaseEntity.Attributes.EntityType);
            aircraft.addPublish(BaseEntity.Attributes.Spatial);
            aircraft.register();

            HLAfixedRecord aEntityIdentifier = aircraft.getEntityIdentifier();
            HLAoctet entityKind = (HLAoctet) aEntityIdentifier.get(1);
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
			
			if (provoke(CmdLineOptions.FlyAircraft)) {
				for (int i=0; i<nrOfCycles; i++) {
					byte b = (byte) i;
					entityKind.setValue(b);
					aircraft.setEntityIdentifier(aEntityIdentifier);
					aircraft.update();  
					Thread.sleep(1000);
				}
			}

		} catch (final Exception e) {
			logErrorAndExit("Exception: {}", e.toString());
		}
	}
}
