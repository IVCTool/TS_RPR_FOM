/**        Copyright 2022, Reinhard Herzog (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

        http: //www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package org.nato.ivct.rpr.datatypes;

import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAinteger16BE;
import hla.rti1516e.exceptions.RTIinternalError;

/**
 * Unique identification of the simulation application (federate) in an exercise, or a symbolic
 * group address referencing multiple simulation applications. Based on the Simulation Address
 * record as specified in IEEE 1278.1-1995 section 5.2.14.1.
 */
public class FederateIdentifierStruct extends HLAfixedRecordStruct {

  enum AttributeName {
    /**
     * Each site shall be assigned a unique site identification. No individual site shall be assigned
     * an identification number containing NO_SITE (0) or ALL_SITES (0xFFFF). An identification number
     * equal to ALL_SITES (0xFFFF) shall mean all sites; this may be used to initialize or start all
     * sites. The mechanism by which Site Identification numbers are assigned is part of federation
     * agreements.
     */
    SiteID,

    /**
     * Each simulation application (federate) at a site shall be assigned an identification number
     * unique within that site. No simulation application shall be assigned an identification number
     * containing NO_APPLIC (0) or ALL_APPLIC (0xFFFF). An application identification number equal to
     * ALL_APPLIC (0xFFFF) shall mean all applications; this may be used to start all applications
     * within a site. One or more simulation applications may reside in a single host computer. The
     * mechanism by which application identification numbers are assigned is part of federation
     * agreements.
     */
    ApplicationID
  }


  public FederateIdentifierStruct () throws RTIinternalError {
    super();
    add(AttributeName.SiteID.name(), encoderFactory.createHLAinteger16BE());
    add(AttributeName.ApplicationID.name(), encoderFactory.createHLAinteger16BE());
  }

  public void decode (byte[] data) throws DecoderException {
    ByteWrapper bw = new ByteWrapper(data);
    (get(AttributeName.SiteID.name())).decode(bw);
    (get(AttributeName.ApplicationID.name())).decode(bw);
}

  public short getSiteID() {
    return ((HLAinteger16BE) get(AttributeName.SiteID.name())).getValue();
  }

  public void setSiteID(short SiteID) {
    ((HLAinteger16BE) get(AttributeName.SiteID.name())).setValue(SiteID);
  }

  public short getApplicationID() {
    return ((HLAinteger16BE) get(AttributeName.ApplicationID.name())).getValue();
  }

  public void setApplicationID(short ApplicationID) {
    ((HLAinteger16BE) get(AttributeName.ApplicationID.name())).setValue(ApplicationID);
  }
}
