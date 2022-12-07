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
public class CollisionBuilder extends InteractionBuilder {

    public static String InteractionClassName      = "HLAinteractionRoot.Collision";

    protected HLAbyte eventId = null;
    protected HLAunicodeChar federate = null;
    protected HLAoctet aggregateUnit = null;
    protected HLAinteger32BE removeSubunits = null;

    public CollisionBuilder(RTIambassador rtiAmbassador)
            throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidInteractionClassHandle, EncoderException {
        super(rtiAmbassador, InteractionClassName);
    }

    public CollisionBuilder addEventId() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        eventId = encoderFactory.createHLAbyte();
        addParameter(Collision.EventId, eventId.toByteArray());
        return this;
    }

    public CollisionBuilder addFederate() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        federate = encoderFactory.createHLAunicodeChar();
        addParameter(Collision.Federate, federate.toByteArray());
        return this;
    }

    public CollisionBuilder addRemoveSubunits() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        removeSubunits = encoderFactory.createHLAinteger32BE();
        addParameter(Collision.RemoveSubunits, removeSubunits.toByteArray());
        return this;
    }

    public CollisionBuilder addAggregateUnit() throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        aggregateUnit = encoderFactory.createHLAoctet();
        addParameter(Collision.AggregateUnit, aggregateUnit.toByteArray());
        return this;
    }

    public void setAggregateUnit(byte value) throws NameNotFound, InvalidInteractionClassHandle, FederateNotExecutionMember, NotConnected, RTIinternalError {
        ParameterHandle handle = this.rtiAmbassador.getParameterHandle(this.classHandle, "AggregateUnit");
        ByteWrapper wrapper = parameters.getValueReference(handle);
        wrapper.put(value);
    }

    public Collision build () {
        Collision collision = new Collision(classHandle, parameters, parameterHandles);
        collision.eventId = encoderFactory.createHLAbyte();
        collision.federate = encoderFactory.createHLAunicodeChar();
        collision.aggregateUnit = encoderFactory.createHLAoctet();
        collision.removeSubunits = encoderFactory.createHLAinteger32BE();

        return collision;
    }
}

