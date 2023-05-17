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

package org.nato.ivct.rpr.interactions;

import org.nato.ivct.rpr.OmtBuilder;
import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAbyte;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public class HLAfederate extends HLAmanager {

    public enum Attributes {
        HLAfederate
    }
    
    // private HLAbyte aHLAfederate;

    public HLAfederate () throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        super();
        // aHLAfederate = OmtBuilder.getEncoderFactory().createHLAbyte();
        addParameter(Attributes.HLAfederate.name(), OmtBuilder.getEncoderFactory().createHLAbyte());
    }
    
    public void setHLAfederate (byte[] value) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, EncoderException, RprBuilderException {
        setParameter2(Attributes.HLAfederate.name(), value);
    }

    // public void decode(ParameterHandleValueMap values)  {
    //     ByteWrapper value = values.getValueReference(getParameterHandle(Attributes.HLAfederate.name()));
    //     if (value != null) {
    //         try {
    //             aHLAfederate.decode(value);
    //         } catch (DecoderException e) {
    //             // TODO Auto-generated catch block
    //             e.printStackTrace();
    //         }
    //     }
    // }

}
