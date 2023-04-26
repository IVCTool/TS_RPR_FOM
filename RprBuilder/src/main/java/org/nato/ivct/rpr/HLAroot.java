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

import java.util.HashMap;
import java.util.Map.Entry;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;


public class HLAroot {

    // reference object to manage common class specific settings
    private static HLAroot anchor;

    private HashMap<String, DataElement> attributeMap = new HashMap<>();
    public void addAttribute (String name, DataElement data) {
        attributeMap.put(name, data);
    }
    public DataElement getAttribute (String name) {
        return attributeMap.get(name);
    }
    public void decode(AttributeHandleValueMap theAttributes) throws NameNotFound, InvalidObjectClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError, DecoderException {
        for (Entry<String, DataElement> entry: attributeMap.entrySet()) {
            entry.getKey();
            entry.getValue();
        }
    }



    /**
     * Get class name in HLA-style as full path with '.' separators. 
     * 
     * @return
     */
    public String getHlaClassName(String root) {
        String hlaClassName = null;
        if (this.getClass().getSimpleName().equalsIgnoreCase(root)) {
            hlaClassName = root;
        } else {
            Class cls = this.getClass();
            do {
                if (hlaClassName == null) {
                    hlaClassName = cls.getSimpleName();
                } else {
                    hlaClassName = cls.getSimpleName() + "." + hlaClassName;
                }
                cls = cls.getSuperclass();
            }
            while (!cls.getSimpleName().equals(root));
            hlaClassName = cls.getSimpleName() + "." + hlaClassName;
        }
        return hlaClassName;
    }
}
