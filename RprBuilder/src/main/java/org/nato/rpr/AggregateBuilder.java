package org.nato.rpr;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAbyte;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAoctet;
import hla.rti1516e.encoding.HLAunicodeChar;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.ParameterHandle;


/**
 * Model specific  builder for Aggregate interactions
 */
public class AggregateBuilder extends InteractionBuilder {

    protected HLAbyte eventId = null;
    protected HLAunicodeChar federate = null;
    protected HLAoctet aggregateUnit = null;
    protected HLAinteger32BE removeSubunits = null;

    public AggregateBuilder(RTIambassador rtiAmbassador)
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidInteractionClassHandle, EncoderException {
        super(rtiAmbassador, "MRM_Interaction.Request.Aggregate");
    }

    public AggregateBuilder addEventId() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        eventId = encoderFactory.createHLAbyte();
        addParameter(AggregateInteraction.EventId, eventId.toByteArray());
        return this;
    }

    public AggregateBuilder addFederate() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        federate = encoderFactory.createHLAunicodeChar();
        addParameter(AggregateInteraction.Federate, federate.toByteArray());
        return this;
    }

    public AggregateBuilder addRemoveSubunits() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        removeSubunits = encoderFactory.createHLAinteger32BE();
        addParameter(AggregateInteraction.RemoveSubunits, removeSubunits.toByteArray());
        return this;
    }

    public AggregateBuilder addAggregateUnit() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        aggregateUnit = encoderFactory.createHLAoctet();
        addParameter(AggregateInteraction.AggregateUnit, aggregateUnit.toByteArray());
        return this;
    }

    public void setAggregateUnit(byte value) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        ParameterHandle handle = this.rtiAmbassador.getParameterHandle(this.messageId, "AggregateUnit");
        ByteWrapper wrapper = parameters.getValueReference(handle);
        wrapper.put(value);
    }

    public AggregateInteraction build () {
        AggregateInteraction aggregate = new AggregateInteraction(messageId, parameters, parameterHandles);
        aggregate.eventId = encoderFactory.createHLAbyte();
        aggregate.federate = encoderFactory.createHLAunicodeChar();
        aggregate.aggregateUnit = encoderFactory.createHLAoctet();
        aggregate.removeSubunits = encoderFactory.createHLAinteger32BE();

        return aggregate;
    }
}

