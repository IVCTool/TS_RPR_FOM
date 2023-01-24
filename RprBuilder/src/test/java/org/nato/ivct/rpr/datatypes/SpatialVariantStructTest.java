package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.exceptions.RTIinternalError;

public class SpatialVariantStructTest {
    @Test
    void testGetSetSpatialStatic() throws RTIinternalError {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        SpatialStaticStruct spatialStatic = spatial.getSpatialStatic();
        assertNotNull(spatialStatic);
        spatialStatic.setIsFrozen(true);
        boolean fr = spatialStatic.getIsFrozen();
        assert(fr == true);
        spatialStatic.setIsFrozen(false);
        fr = spatialStatic.getIsFrozen();
        assert(fr == false);
        WorldLocationStruct wl = spatialStatic.getWorldLocation();
        wl.setX(1.1);
        wl.setY(2.2);
        wl.setZ(3.3);
        HLAfloat64BE x = wl.getX();
        HLAfloat64BE y = wl.getY();
        HLAfloat64BE z = wl.getZ();
        assert(x.getValue() == 1.1);
        assert(y.getValue() == 2.2);
        assert(z.getValue() == 3.3);
        OrientationStruct or = spatialStatic.getOrientation();
        or.setPsi(4.4f);
        or.setTheta(5.5f);
        or.setPhi(6.6f);
        HLAfloat32BE psi = or.getPsi();
        HLAfloat32BE theta = or.getTheta();
        HLAfloat32BE phi = or.getPhi();
        assert(psi.getValue() == 4.4f);
        assert(theta.getValue() == 5.5f);
        assert(phi.getValue() == 6.6f);
    }

    @Test
    void testSetSpatialFPW() throws Exception {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        SpatialFPStruct fpw = spatial.getSpatialFPW();
        assertNotNull(fpw);
        WorldLocationStruct wls = new WorldLocationStruct();
        wls.setX(1.1);
        wls.setY(1.2);
        wls.setZ(1.3);
        fpw.setWorldLocation(wls);
        spatial.setSpatialFPW(fpw);
        fpw.setIsFrozen(false);
        spatial.setSpatialFPW(fpw);
        OrientationStruct ors = new OrientationStruct();
        ors.setPhi(2.1f);
        ors.setPsi(2.2f);
        ors.setTheta(2.3f);
        fpw.setOrientation(ors);
        VelocityVectorStruct vel = new VelocityVectorStruct();
        fpw.setVelocityVector(vel);
        // get values back
        SpatialFPStruct fpw2 = spatial.getSpatialFPW();
        WorldLocationStruct wls2 = fpw2.getWorldLocation();
        assert(wls2.getX().getValue() == 1.1);
        assert(wls2.getY().getValue() == 1.2);
        assert(wls2.getZ().getValue() == 1.3);
        HLAboolean fr2 = fpw2.getIsFrozen();
        assert(fr2.getValue() == false);
        OrientationStruct ors2 = fpw.getOrientation();
        assert(ors2.getPhi().getValue() == 2.1f);
        assert(ors2.getPsi().getValue() == 2.2f);
        assert(ors2.getTheta().getValue() == 2.3f);
        VelocityVectorStruct vel2 = fpw.getVelocityVector();
        assertNotNull(vel2);

    }

    @Test
    void testGetSpatialFPW() throws Exception {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        SpatialFPStruct fpw = spatial.getSpatialFPW();
        WorldLocationStruct wls = fpw.getWorldLocation();
        wls.setX(1.1);
        wls.setY(1.2);
        wls.setZ(1.3);
        fpw.setIsFrozen(false);
        OrientationStruct ors = fpw.getOrientation();
        ors.setPhi(2.1f);
        ors.setPsi(2.2f);
        ors.setTheta(2.3f);
        VelocityVectorStruct vel = fpw.getVelocityVector();

        // test if values are correctly set
        assert(spatial.getSpatialFPW().getWorldLocation().getX().getValue() == 1.1);
        assert(spatial.getSpatialFPW().getWorldLocation().getY().getValue() == 1.2);
        assert(spatial.getSpatialFPW().getWorldLocation().getZ().getValue() == 1.3);
        assert(spatial.getSpatialFPW().getIsFrozen().getValue() == false);
        assert(spatial.getSpatialFPW().getOrientation().getPhi().getValue() == 2.1f);
        assert(spatial.getSpatialFPW().getOrientation().getPsi().getValue() == 2.2f);
        assert(spatial.getSpatialFPW().getOrientation().getTheta().getValue() == 2.3f);
        assertNotNull(spatial.getSpatialFPW().getVelocityVector());

    }

    @Test
    void testReset() throws RTIinternalError {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        SpatialStaticStruct spatialStatic = spatial.getSpatialStatic();
        assertNotNull(spatialStatic);
        SpatialFPStruct fpw = spatial.getSpatialFPW();
        assertNull(fpw);
        spatial.reset();
        fpw = spatial.getSpatialFPW();
        assertNotNull(fpw);
    }

    @Test
    void testSetSpatialStatic() {

    }
}
