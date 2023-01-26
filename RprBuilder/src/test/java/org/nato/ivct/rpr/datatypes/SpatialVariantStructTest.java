package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

import hla.rti1516e.encoding.DecoderException;
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
        double x = wl.getX();
        double y = wl.getY();
        double z = wl.getZ();
        assert(x == 1.1);
        assert(y == 2.2);
        assert(z == 3.3);
        OrientationStruct or = spatialStatic.getOrientation();
        or.setPsi(4.4f);
        or.setTheta(5.5f);
        or.setPhi(6.6f);
        float psi = or.getPsi();
        float theta = or.getTheta();
        float phi = or.getPhi();
        assert(psi == 4.4f);
        assert(theta == 5.5f);
        assert(phi == 6.6f);
    }


    @Test
    void testEncodeDecode () throws RTIinternalError, DecoderException {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        spatial.getSpatialStatic().setIsFrozen(false);
        spatial.getSpatialStatic().getWorldLocation().setX(1.1);
        spatial.getSpatialStatic().getWorldLocation().setY(2.2);
        spatial.getSpatialStatic().getWorldLocation().setZ(3.3);
        spatial.getSpatialStatic().getOrientation().setPsi(4.4f);
        spatial.getSpatialStatic().getOrientation().setTheta(5.5f);
        spatial.getSpatialStatic().getOrientation().setPhi(6.6f);

        byte[] bytes = spatial.toByteArray();
        assert(bytes.length > 0);

        SpatialVariantStruct received = new SpatialVariantStruct();
        received.decode(bytes);
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
        assert(wls2.getX() == 1.1);
        assert(wls2.getY() == 1.2);
        assert(wls2.getZ() == 1.3);
        boolean fr2 = fpw2.getIsFrozen();
        assert(fr2 == false);
        OrientationStruct ors2 = fpw.getOrientation();
        assert(ors2.getPhi() == 2.1f);
        assert(ors2.getPsi() == 2.2f);
        assert(ors2.getTheta() == 2.3f);
        VelocityVectorStruct vel2 = fpw.getVelocityVector();
        assertNotNull(vel2);

    }

    @Test
    void testRandomSetVariant() throws RTIinternalError {
        SpatialVariantStruct spatial = new SpatialVariantStruct();

        SpatialStaticStruct sp_vs = new SpatialStaticStruct();
        SpatialFPStruct sp_fp = new SpatialFPStruct();
        SpatialRPStruct sp_rp = new SpatialRPStruct();
        SpatialRVStruct sp_rv = new SpatialRVStruct();
        SpatialFVStruct sp_fv = new SpatialFVStruct();
        SpatialRPStruct sp_rs = new SpatialRPStruct();

        spatial.setSpatialStatic(sp_vs);
        spatial.setSpatialFPW(sp_fp);
        spatial.setSpatialRPW(sp_rp);
        spatial.setSpatialRVW(sp_rv);
        spatial.setSpatialFVW(sp_fv);
        spatial.setSpatialFPB(sp_fp);
        spatial.setSpatialRPB(sp_rp);
        spatial.setSpatialRVB(sp_rv);
        spatial.setSpatialFVB(sp_fv);

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
        assert(spatial.getSpatialFPW().getWorldLocation().getX() == 1.1);
        assert(spatial.getSpatialFPW().getWorldLocation().getY() == 1.2);
        assert(spatial.getSpatialFPW().getWorldLocation().getZ() == 1.3);
        assert(spatial.getSpatialFPW().getIsFrozen() == false);
        assert(spatial.getSpatialFPW().getOrientation().getPhi() == 2.1f);
        assert(spatial.getSpatialFPW().getOrientation().getPsi() == 2.2f);
        assert(spatial.getSpatialFPW().getOrientation().getTheta() == 2.3f);
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
