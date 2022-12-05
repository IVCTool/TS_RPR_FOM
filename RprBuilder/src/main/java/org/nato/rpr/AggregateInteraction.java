package org.nato.rpr;

import hla.rti1516e.encoding.HLAbyte;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAunicodeChar;
import java.util.HashMap;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;

public class AggregateInteraction extends Interaction {

    public static String MessageId      = "MRM_Interaction.Request.Aggregate";
    public static String EventId        = "EventId";        // HLAbyte
    public static String Federate       = "Federate";       // HLAunicodeChar
    public static String AggregateUnit  = "AggregateUnit";  // HLAoctet
    public static String RemoveSubunits = "RemoveSubunits"; // HLAinteger32BE

    HLAbyte eventId;
    HLAunicodeChar federate;
    HLAoctet aggregateUnit;
    HLAinteger32BE removeSubunits;

    protected AggregateInteraction(InteractionClassHandle messageId, ParameterHandleValueMap parameters, HashMap<String,ParameterHandle> handles) {
        super(messageId, parameters, handles);
    }

    public void setValueEventId(byte value) throws Exception {
        eventId.setValue(value);
        setParameter(EventId, eventId);
    }

    public void setValueFederate(short value) throws Exception {
        federate.setValue(value);
        setParameter(Federate, federate);
    }

    public void setValueAggregateUnit(byte value) throws Exception {
        aggregateUnit.setValue(value);
        setParameter(AggregateUnit, aggregateUnit);
    }

    public void setValueRemoveSubunits(int value) throws Exception {
        removeSubunits.setValue(value);
        setParameter(RemoveSubunits, removeSubunits);
    }
}

