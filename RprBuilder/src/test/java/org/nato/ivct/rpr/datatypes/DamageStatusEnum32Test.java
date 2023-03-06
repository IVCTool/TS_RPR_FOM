package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hla.rti1516e.encoding.DecoderException;

public class DamageStatusEnum32Test {
    @Test
    void testEncodeDecode() throws DecoderException {
        DamageStatusEnum32 value = DamageStatusEnum32.NoDamage;
        byte[] bytes = value.getDataElement().toByteArray();
        DamageStatusEnum32 decodedValue = DamageStatusEnum32.decode(bytes);
        assertEquals(value, decodedValue);
    }

    @Test
    void testGetEncodedLength() {
        DamageStatusEnum32 value = DamageStatusEnum32.ModerateDamage;
        assertTrue(4 == value.getDataElement().getEncodedLength());
    }

    @Test
    void testGetOctetBoundary() {
        DamageStatusEnum32 value = DamageStatusEnum32.SlightDamage;
        assertTrue(4 == value.getDataElement().getOctetBoundary());
    }

    @Test
    void testToByteArray() {
        DamageStatusEnum32 value = DamageStatusEnum32.Destroyed;
        byte[] bytes = value.getDataElement().toByteArray();
        assertTrue(0 == bytes[0]);
        assertTrue(0 == bytes[1]);
        assertTrue(0 == bytes[2]);
        assertTrue(3 == bytes[3]);
    }
}
