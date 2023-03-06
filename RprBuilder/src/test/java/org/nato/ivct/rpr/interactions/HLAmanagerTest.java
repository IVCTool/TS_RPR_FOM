package org.nato.ivct.rpr.interactions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.nato.ivct.rpr.RprBuilderException;

import hla.rti1516e.exceptions.*;

public class HLAmanagerTest {

    @Test
    void testGetHlaClassName() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, RprBuilderException {
        HLAmanager manager = new HLAmanager();
        String name = manager.getHlaClassName();
        assertEquals(name, "HLAinteractionRoot.HLAmanager");
    }

}
