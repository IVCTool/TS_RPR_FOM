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

package org.nato.ivct.rpr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.exceptions.RTIinternalError;

/**
 * RPR Builder Initialization
 */
public class Builder {
    private static final Logger log = LoggerFactory.getLogger(Builder.class);
    private static RTIambassador rtiAmbassador = null;
    private static EncoderFactory encoderFactory = null;
    
    public static void initialize(RTIambassador rtiAmbassador2Use) {
        rtiAmbassador = rtiAmbassador2Use;
    }

    public static RTIambassador getRtiAmbassador() {
        if (rtiAmbassador == null) {
            log.error("RPR Builder not initialized");
        }
        return rtiAmbassador;
    }

    public static EncoderFactory getEncoderFactory() throws RTIinternalError {
        if (encoderFactory == null) {
            encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
        }
        return encoderFactory;
    }
}
