package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CamouflageEnum32Test {
    @Test
    void testGetValue() {
        assertEquals(0, CamouflageEnum32.UniformPaintScheme.getValue());
        assertEquals(1, CamouflageEnum32.DesertCamouflage.getValue());
        assertEquals(2, CamouflageEnum32.WinterCamouflage.getValue());
        assertEquals(3, CamouflageEnum32.ForestCamouflage.getValue());
        assertEquals(4, CamouflageEnum32.GenericCamouflage.getValue());
        CamouflageEnum32 camouflageDesert = CamouflageEnum32.DesertCamouflage;
        assertTrue(camouflageDesert == CamouflageEnum32.DesertCamouflage);
        assertFalse(camouflageDesert == CamouflageEnum32.ForestCamouflage);
        assertEquals(1, camouflageDesert.getValue());
        CamouflageEnum32 camouflageForest = CamouflageEnum32.ForestCamouflage;
        assertFalse(camouflageDesert == camouflageForest);
    }
}
