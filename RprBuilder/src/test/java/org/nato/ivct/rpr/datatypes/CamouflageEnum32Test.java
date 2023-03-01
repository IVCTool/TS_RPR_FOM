package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class CamouflageEnum32Test {
    @Test
    void testGetValue() {
        CamouflageEnum32 camouflage = CamouflageEnum32.DesertCamouflage;
        assertTrue(camouflage == CamouflageEnum32.DesertCamouflage);
        assertFalse(camouflage == CamouflageEnum32.ForestCamouflage);
    }
}
