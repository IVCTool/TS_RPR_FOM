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
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FomFiles {
    private static final Logger log = LoggerFactory.getLogger(FomFiles.class);
    private static final String prefix = "ivct/";
    ArrayList<URL> fomList = new ArrayList<>();
    
    public FomFiles() {
        fomList = new ArrayList<>();
    }

    /** 
     * This method extracts OMT files from the resource archive and
     * creates temporary file copies in the user tmp directory. This
     * is required as some RTIs are not supporting the "jar" access
     * protocol to the module files.
      */
    private URL createTempFile (String resource, String suffix) {
        try {
            log.trace("using {}{}", resource, suffix);
            URL fom = FomFiles.class.getResource("/" + resource + suffix);
            File temp = new File(resource + suffix);
            if (!temp.exists()) {
                temp.createNewFile();
                byte[] resourceFileBytes = fom.openStream().readAllBytes();
                Files.write(temp.toPath(), resourceFileBytes);
            }
            return temp.toURI().toURL();
        } catch (IOException e) {
            log.error("error while creating temporary OMT files", e);
        }
        return null;
    }

    /** 
     * thats the way, it should be done - but the temporary file names causes issues with the MAK RTI ?!?
     */
    private URL createTempFileTheStandardWay (String resource, String suffix) {
        try {
            log.trace("using {}{}", resource, suffix);
            URL fom = FomFiles.class.getResource("/" + resource + suffix);
            File temp = File.createTempFile(prefix + resource, suffix);
            byte[] resourceFileBytes = fom.openStream().readAllBytes();
            Files.write(temp.toPath(), resourceFileBytes);
            temp.deleteOnExit();
            return temp.toURI().toURL();
        } catch (IOException e) {
            log.error("error while creating temporary OMT files", e);
        }
        return null;
    }

    public FomFiles addFomFile (String fileName) {
        File fom = new File(fileName);
        URL fUri = null;
        try {
            fUri = fom.toURI().toURL();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fomList.add(fUri);
        return this;
    }

    public FomFiles addRPR_BASE() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Base_v2.0.xml"));
        return this;
    }
    
    public FomFiles addTmpRPR_BASE() {
        fomList.add(createTempFile("RPR-FOM-v2.0/RPR-Base_v2.0",".xml"));
        return this;
    }
    
    public FomFiles addRPR_Enumerations() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Enumerations_v2.0.xml"));
        return this;
    }
    
    public FomFiles addTmpRPR_Enumerations() {
        fomList.add(createTempFile("RPR-FOM-v2.0/RPR-Enumerations_v2.0", ".xml"));
        return this;
    }
    
    public FomFiles addRPR_Switches() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Switches_v2.0.xml"));
        return this;
    }
    
    public FomFiles addTmpRPR_Switches() {
        fomList.add(createTempFile("RPR-FOM-v2.0/RPR-Switches_v2.0", ".xml"));
        return this;
    }
    
    public FomFiles addRPR_Foundation() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Foundation_v2.0.xml"));
        return this;
    }
    
    public FomFiles addTmpRPR_Foundation() {
        fomList.add(createTempFile("RPR-FOM-v2.0/RPR-Foundation_v2.0", ".xml"));
        return this;
    }
    
    public FomFiles addRPR_Physical() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Physical_v2.0.xml"));
        return this;
    }

    public FomFiles addTmpRPR_Physical() {
        fomList.add(createTempFile("RPR-FOM-v2.0/RPR-Physical_v2.0", ".xml"));
        return this;
    }

    public FomFiles addRPR_Warfare() {
        fomList.add(FomFiles.class.getResource("/RPR-FOM-v2.0/RPR-Warfare_v2.0.xml"));
        return this;
    }

    public FomFiles addTmpRPR_Warfare() {
        fomList.add(createTempFile("RPR-FOM-v2.0/RPR-Warfare_v2.0", ".xml"));
        return this;
    }

    public ArrayList<URL> get() {
        return fomList;
    }
    
}
