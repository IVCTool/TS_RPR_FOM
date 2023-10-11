package org.nato.ivct.rpr.datatypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.RTIinternalError;

public class MarkingEncodingEnum8Test {
    @Test
    void testGetValue() {
        assertEquals(0, MarkingEncodingEnum8.Other.getValue());
        assertEquals(1, MarkingEncodingEnum8.ASCII.getValue());
        assertEquals(2, MarkingEncodingEnum8.ArmyMarkingCCTT.getValue());
        assertEquals(3, MarkingEncodingEnum8.DigitChevron.getValue());      
        
        MarkingEncodingEnum8 other_ = MarkingEncodingEnum8.Other;
        assertTrue(other_ == MarkingEncodingEnum8.Other);
        assertFalse(other_ == MarkingEncodingEnum8.ASCII);
        assertEquals(0, other_.getValue()  );        
        MarkingEncodingEnum8 chevronDigit = MarkingEncodingEnum8.DigitChevron;
        assertFalse( other_ == chevronDigit );        
    }

    @Test
    void testEncoding() throws RTIinternalError, DecoderException {
        
        MarkingEncodingEnum8 other_ = MarkingEncodingEnum8.Other;
        byte[] bytes = other_.getDataElement().toByteArray();
        assert(bytes.length > 0);
        MarkingEncodingEnum8 otherReceived = MarkingEncodingEnum8.decode(bytes);
        assertTrue(other_ == otherReceived);
        
        /*
        CamouflageEnum32 camouflageDesert = CamouflageEnum32.DesertCamouflage;
        byte[] bytes = camouflageDesert.getDataElement().toByteArray();
        assert(bytes.length > 0);
        CamouflageEnum32 camouflageReceived = CamouflageEnum32.decode(bytes);
        assertTrue(camouflageDesert == camouflageReceived);
        */
        
        
    }
}
