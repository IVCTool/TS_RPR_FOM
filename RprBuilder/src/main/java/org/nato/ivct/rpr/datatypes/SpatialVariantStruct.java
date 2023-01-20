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

package org.nato.ivct.rpr.datatypes;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.exceptions.RTIinternalError;

public class SpatialVariantStruct extends HLAvariantRecordStruct<HLAoctet> {

    /** Discriminant */
    DeadReckoningAlgorithmEnum8 DeadReckoningAlgorithm;

    public enum AttributeName {
        SpatialStatic,
        SpatialFPW,
        SpatialRPW,
        DRM_RVW,
        SpatialFVW,
        SpatialFPB,
        SpatialRPB,
        SpatialRVB,
        SpatialFVB
    }

    public SpatialVariantStruct () throws RTIinternalError {
        EncoderFactory encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

        // add(AttributeName.SpatialStatic.name(), new SpatialStaticStruct());
        // add(AttributeName.SpatialFPW.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.SpatialRPW.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.DRM_RVW.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.SpatialFVW.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.SpatialFPB.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.SpatialRPB.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.SpatialRVB.name(), encoderFactory.createHLAoctet());
        // add(AttributeName.SpatialFVB.name(), encoderFactory.createHLAoctet());

    }

    /** TODO: attribute getter, setter, and structures to be completed
     * 
    public DeadReckoningAlgorithmEnum8 getDeadReckoningAlgorithm() {
        return this.DeadReckoningAlgorithm;
    }

    public void setDeadReckoningAlgorithm(DeadReckoningAlgorithmEnum8 DeadReckoningAlgorithm) {
        this.DeadReckoningAlgorithm = DeadReckoningAlgorithm;
    }

    public SpatialStaticStruct getSpatialStatic() {
        return this.SpatialStatic;
    }

    public void setSpatialStatic(SpatialStaticStruct SpatialStatic) {
        this.SpatialStatic = SpatialStatic;
    }

    public SpatialFPStruct getSpatialFPW() {
        return this.SpatialFPW;
    }

    public void setSpatialFPW(SpatialFPStruct SpatialFPW) {
        this.SpatialFPW = SpatialFPW;
    }

    public SpatialRPStruct getSpatialRPW() {
        return this.SpatialRPW;
    }

    public void setSpatialRPW(SpatialRPStruct SpatialRPW) {
        this.SpatialRPW = SpatialRPW;
    }

    public SpatialRVStruct getSpatialRVW() {
        return this.SpatialRVW;
    }

    public void setSpatialRVW(SpatialRVStruct SpatialRVW) {
        this.SpatialRVW = SpatialRVW;
    }

    public SpatialFVStruct getSpatialFVW() {
        return this.SpatialFVW;
    }

    public void setSpatialFVW(SpatialFVStruct SpatialFVW) {
        this.SpatialFVW = SpatialFVW;
    }

    public SpatialFPStruct getSpatialFPB() {
        return this.SpatialFPB;
    }

    public void setSpatialFPB(SpatialFPStruct SpatialFPB) {
        this.SpatialFPB = SpatialFPB;
    }

    public SpatialRPStruct getSpatialRPB() {
        return this.SpatialRPB;
    }

    public void setSpatialRPB(SpatialRPStruct SpatialRPB) {
        this.SpatialRPB = SpatialRPB;
    }

    public SpatialRVStruct getSpatialRVB() {
        return this.SpatialRVB;
    }

    public void setSpatialRVB(SpatialRVStruct SpatialRVB) {
        this.SpatialRVB = SpatialRVB;
    }

    public SpatialFVStruct getSpatialFVB() {
        return this.SpatialFVB;
    }

    public void setSpatialFVB(SpatialFVStruct SpatialFVB) {
        this.SpatialFVB = SpatialFVB;
    }
    */
    
}
