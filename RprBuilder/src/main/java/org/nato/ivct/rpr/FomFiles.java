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

import java.net.URL;
import java.util.ArrayList;

public class FomFiles {
    ArrayList<URL> fomList = new ArrayList<>();
    
    public FomFiles() {
        fomList = new ArrayList<>();
    }

    public FomFiles addRPR_BASE() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Base_v2.0.xml"));
        return this;
    }
    
    public FomFiles addRPR_Enumerations() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Enumerations_v2.0.xml"));
        return this;
    }
    
    public FomFiles addRPR_Switches() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Switches_v2.0.xml"));
        return this;
    }
    
    public FomFiles addRPR_Foundation() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Foundation_v2.0.xml"));
        return this;
    }
    
    public FomFiles addRPR_Physical() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Physical_v2.0.xml"));
        return this;
    }

    public FomFiles addRPR_Warfare() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Warfare_v2.0.xml"));
        return this;
    }

    public ArrayList<URL> get() {
        return fomList;
    }
    
}
