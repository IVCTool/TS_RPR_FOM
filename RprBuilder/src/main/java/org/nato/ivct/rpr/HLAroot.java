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

public class HLAroot {

    private static final Logger log = LoggerFactory.getLogger(HLAroot.class);

    // reference object to manage common class specific settings
    private static HLAroot anchor;
    protected static RTIambassador rtiAmbassador;

    public static void initialize(RTIambassador rtiAmbassador2Use) {
        rtiAmbassador = rtiAmbassador2Use;
    }

    /**
     * Get class name in HLA-style as full path with '.' separators, using root
     * as the terminated top level class. The typical usage of this method is 
     * from with the HLAobjectRoot and the HLAinteractionRoot classes. 
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
