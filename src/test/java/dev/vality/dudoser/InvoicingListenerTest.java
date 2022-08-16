package dev.vality.dudoser;

import dev.vality.damsel.payment_processing.Event;
import dev.vality.damsel.payment_processing.EventPayload;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.dudoser.exception.ParseException;
import dev.vality.dudoser.handler.poller.InvoicePaymentStatusChangedProcessedHandler;
import dev.vality.dudoser.listener.InvoicingKafkaListener;
import dev.vality.dudoser.service.HandlerManager;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


public class InvoicingListenerTest {

    @Mock
    private HandlerManager handlerManager;
    @Mock
    private MachineEventParser<EventPayload> eventParser;
    @Mock
    private Acknowledgment ack;
    @Mock
    private InvoicePaymentStatusChangedProcessedHandler handler;

    private InvoicingKafkaListener listener;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        listener = new InvoicingKafkaListener(handlerManager, eventParser);
    }

    @Test
    public void listenNonInvoiceChanges() {

        MachineEvent message = new MachineEvent();
        Event event = new Event();
        EventPayload payload = new EventPayload();
        payload.setCustomerChanges(List.of());
        event.setPayload(payload);
        Mockito.when(eventParser.parse(message)).thenReturn(payload);

        SinkEvent sinkEvent = new SinkEvent();
        sinkEvent.setEvent(message);

        listener.handle(sinkEvent, ack);

        Mockito.verify(handlerManager, Mockito.times(0)).getHandler(any());
        Mockito.verify(ack, Mockito.times(1)).acknowledge();
    }

    @Test
    public void listenEmptyException() {
        MachineEvent message = new MachineEvent();

        SinkEvent sinkEvent = new SinkEvent();
        sinkEvent.setEvent(message);

        Mockito.when(eventParser.parse(message)).thenThrow(new ParseException());

        assertThrows(ParseException.class, () -> listener.handle(sinkEvent, ack));

        Mockito.verify(ack, Mockito.times(0)).acknowledge();
    }

    @Test
    public void listenChanges() {
        Event event = new Event();
        EventPayload payload = new EventPayload();
        ArrayList<InvoiceChange> invoiceChanges = new ArrayList<>();
        invoiceChanges.add(new InvoiceChange());
        payload.setInvoiceChanges(invoiceChanges);
        event.setPayload(payload);
        MachineEvent message = new MachineEvent();
        Mockito.when(eventParser.parse(message)).thenReturn(payload);

        Mockito.when(handlerManager.getHandler(any())).thenReturn(Optional.of(handler));

        SinkEvent sinkEvent = new SinkEvent();
        sinkEvent.setEvent(message);

        listener.handle(sinkEvent, ack);

        Mockito.verify(handlerManager, Mockito.times(1)).getHandler(any());
        Mockito.verify(ack, Mockito.times(1)).acknowledge();
    }

}
