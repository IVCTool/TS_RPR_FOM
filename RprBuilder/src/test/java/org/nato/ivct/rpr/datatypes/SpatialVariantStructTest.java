package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.RTIinternalError;

public class SpatialVariantStructTest {

    @Test
    void testSetGetSpatialStatic() throws RTIinternalError {
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
    void testSetGetSpatialFPW() throws Exception {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        spatial.getSpatialFPW().getWorldLocation().setX(1.1);
        spatial.getSpatialFPW().getWorldLocation().setY(1.2);
        spatial.getSpatialFPW().getWorldLocation().setZ(1.3);
        spatial.getSpatialFPW().setIsFrozen(false);
        spatial.getSpatialFPW().getOrientation().setPhi(2.1f);
        spatial.getSpatialFPW().getOrientation().setPsi(2.2f);
        spatial.getSpatialFPW().getOrientation().setTheta(2.3f);
        VelocityVectorStruct vel = spatial.getSpatialFPW().getVelocityVector();
        // test if values are correctly set
        assert(spatial.getSpatialFPW().getWorldLocation().getX() == 1.1);
        assert(spatial.getSpatialFPW().getWorldLocation().getY() == 1.2);
        assert(spatial.getSpatialFPW().getWorldLocation().getZ() == 1.3);
        assert(spatial.getSpatialFPW().getIsFrozen() == false);
        assert(spatial.getSpatialFPW().getOrientation().getPhi() == 2.1f);
        assert(spatial.getSpatialFPW().getOrientation().getPsi() == 2.2f);
        assert(spatial.getSpatialFPW().getOrientation().getTheta() == 2.3f);
        assertNotNull(vel);

    }

    @Test
    void testEncodeDecodeSpatialVariantStruct () throws RTIinternalError, DecoderException {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        spatial.getSpatialStatic().setIsFrozen(false);
        spatial.getSpatialStatic().getWorldLocation().setX(1.1);
        spatial.getSpatialStatic().getWorldLocation().setY(2.2);
        spatial.getSpatialStatic().getWorldLocation().setZ(3.3);
        spatial.getSpatialStatic().getOrientation().setPsi(4.4f);
        spatial.getSpatialStatic().getOrientation().setTheta(5.5f);
        spatial.getSpatialStatic().getOrientation().setPhi(6.6f);
        //  encode SpatialVariantStruct
        byte[] bytes = spatial.toByteArray();
        assert(bytes.length > 0);
        // decode SpatialVariantStruct
        SpatialVariantStruct received = new SpatialVariantStruct();
        received.decode(bytes);
        // test if values are correctly set
        assert(received.getSpatialStatic().getWorldLocation().getX() == 1.1);
        assert(received.getSpatialStatic().getWorldLocation().getY() == 2.2);
        assert(received.getSpatialStatic().getWorldLocation().getZ() == 3.3);
        assert(received.getSpatialStatic().getIsFrozen() == false);
        assert(received.getSpatialStatic().getOrientation().getPsi() == 4.4f);
        assert(received.getSpatialStatic().getOrientation().getTheta() == 5.5f);
        assert(received.getSpatialStatic().getOrientation().getPhi() == 6.6f);
    }

    @Test
    void testEncodeDecodeSpatialFPStruct () throws RTIinternalError, DecoderException {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        spatial.getSpatialFPW().setIsFrozen(false);
        spatial.getSpatialFPW().getWorldLocation().setX(1.1);
        spatial.getSpatialFPW().getWorldLocation().setY(2.2);
        spatial.getSpatialFPW().getWorldLocation().setZ(3.3);
        spatial.getSpatialFPW().getOrientation().setPsi(4.4f);
        spatial.getSpatialFPW().getOrientation().setTheta(5.5f);
        spatial.getSpatialFPW().getOrientation().setPhi(6.6f);
        spatial.getSpatialFPW().getVelocityVector().setXVelocity(7.7f);
        spatial.getSpatialFPW().getVelocityVector().setYVelocity(8.8f);
        spatial.getSpatialFPW().getVelocityVector().setZVelocity(9.9f);
        //  encode SpatialVariantStruct
        byte[] bytes = spatial.toByteArray();
        assert(bytes.length > 0);
        // decode SpatialVariantStruct
        SpatialVariantStruct received = new SpatialVariantStruct();
        received.decode(bytes);
        // test if values are correctly set
        assert(received.getSpatialFPW().getWorldLocation().getX() == 1.1);
        assert(received.getSpatialFPW().getWorldLocation().getY() == 2.2);
        assert(received.getSpatialFPW().getWorldLocation().getZ() == 3.3);
        assert(received.getSpatialFPW().getIsFrozen() == false);
        assert(received.getSpatialFPW().getOrientation().getPsi() == 4.4f);
        assert(received.getSpatialFPW().getOrientation().getTheta() == 5.5f);
        assert(received.getSpatialFPW().getOrientation().getPhi() == 6.6f);
        assert(received.getSpatialFPW().getVelocityVector().getXVelocity() == 7.7f);
        assert(received.getSpatialFPW().getVelocityVector().getYVelocity() == 8.8f);
        assert(received.getSpatialFPW().getVelocityVector().getZVelocity() == 9.9f);
    }

    
    @Test
    void testEncodeDecodeSpatialRPStruct () throws RTIinternalError, DecoderException {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        spatial.getSpatialRPW().setIsFrozen(false);
        spatial.getSpatialRPW().getWorldLocation().setX(1.1);
        spatial.getSpatialRPW().getWorldLocation().setY(2.2);
        spatial.getSpatialRPW().getWorldLocation().setZ(3.3);
        spatial.getSpatialRPW().getOrientation().setPsi(4.4f);
        spatial.getSpatialRPW().getOrientation().setTheta(5.5f);
        spatial.getSpatialRPW().getOrientation().setPhi(6.6f);
        spatial.getSpatialRPW().getVelocityVector().setXVelocity(7.7f);
        spatial.getSpatialRPW().getVelocityVector().setYVelocity(8.8f);
        spatial.getSpatialRPW().getVelocityVector().setZVelocity(9.9f);
        //  encode SpatialVariantStruct
        byte[] bytes = spatial.toByteArray();
        assert(bytes.length > 0);
        // decode SpatialVariantStruct
        SpatialVariantStruct received = new SpatialVariantStruct();
        received.decode(bytes);
        // test if values are correctly set
        assert(received.getSpatialRPW().getWorldLocation().getX() == 1.1);
        assert(received.getSpatialRPW().getWorldLocation().getY() == 2.2);
        assert(received.getSpatialRPW().getWorldLocation().getZ() == 3.3);
        assert(received.getSpatialRPW().getIsFrozen() == false);
        assert(received.getSpatialRPW().getOrientation().getPsi() == 4.4f);
        assert(received.getSpatialRPW().getOrientation().getTheta() == 5.5f);
        assert(received.getSpatialRPW().getOrientation().getPhi() == 6.6f);
        assert(received.getSpatialRPW().getVelocityVector().getXVelocity() == 7.7f);
        assert(received.getSpatialRPW().getVelocityVector().getYVelocity() == 8.8f);
        assert(received.getSpatialRPW().getVelocityVector().getZVelocity() == 9.9f);
    }

   
    @Test
    void testEncodeDecodeSpatialFVStruct () throws RTIinternalError, DecoderException {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        spatial.getSpatialFVW().setIsFrozen(false);
        spatial.getSpatialFVW().getWorldLocation().setX(1.1);
        spatial.getSpatialFVW().getWorldLocation().setY(2.2);
        spatial.getSpatialFVW().getWorldLocation().setZ(3.3);
        spatial.getSpatialFVW().getOrientation().setPsi(4.4f);
        spatial.getSpatialFVW().getOrientation().setTheta(5.5f);
        spatial.getSpatialFVW().getOrientation().setPhi(6.6f);
        spatial.getSpatialFVW().getVelocityVector().setXVelocity(7.7f);
        spatial.getSpatialFVW().getVelocityVector().setYVelocity(8.8f);
        spatial.getSpatialFVW().getVelocityVector().setZVelocity(9.9f);
        spatial.getSpatialFVW().getAccelerationVector().setXAcceleration(1.2f);
        spatial.getSpatialFVW().getAccelerationVector().setYAcceleration(1.3f);
        spatial.getSpatialFVW().getAccelerationVector().setZAcceleration(1.4f);
        //  encode SpatialVariantStruct
        byte[] bytes = spatial.toByteArray();
        assert(bytes.length > 0);
        // decode SpatialVariantStruct
        SpatialVariantStruct received = new SpatialVariantStruct();
        received.decode(bytes);
        // test if values are correctly set
        assert(received.getSpatialFVW().getWorldLocation().getX() == 1.1);
        assert(received.getSpatialFVW().getWorldLocation().getY() == 2.2);
        assert(received.getSpatialFVW().getWorldLocation().getZ() == 3.3);
        assert(received.getSpatialFVW().getIsFrozen() == false);
        assert(received.getSpatialFVW().getOrientation().getPsi() == 4.4f);
        assert(received.getSpatialFVW().getOrientation().getTheta() == 5.5f);
        assert(received.getSpatialFVW().getOrientation().getPhi() == 6.6f);
        assert(received.getSpatialFVW().getVelocityVector().getXVelocity() == 7.7f);
        assert(received.getSpatialFVW().getVelocityVector().getYVelocity() == 8.8f);
        assert(received.getSpatialFVW().getVelocityVector().getZVelocity() == 9.9f);
        assert(received.getSpatialFVW().getAccelerationVector().getXAcceleration() == 1.2f);
        assert(received.getSpatialFVW().getAccelerationVector().getYAcceleration() == 1.3f);
        assert(received.getSpatialFVW().getAccelerationVector().getZAcceleration() == 1.4f);
    }

    @Test
    void testRandomSetVariant() throws RTIinternalError {
        SpatialVariantStruct spatial = new SpatialVariantStruct();
        SpatialStaticStruct sp_vs = new SpatialStaticStruct();
        SpatialFPStruct sp_fp = new SpatialFPStruct();
        SpatialRPStruct sp_rp = new SpatialRPStruct();
        SpatialRVStruct sp_rv = new SpatialRVStruct();
        SpatialFVStruct sp_fv = new SpatialFVStruct();
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

}
