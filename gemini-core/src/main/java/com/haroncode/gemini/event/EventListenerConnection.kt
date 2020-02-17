package com.haroncode.gemini.event

import com.haroncode.gemini.connection.BaseConnectionRule
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
class EventListenerConnection<Event : Any>(
    eventPublisher: Publisher<Event>,
    eventListener: EventListener<Event>
) : BaseConnectionRule<Event, Event>(
    publisher = eventPublisher,
    consumer = Consumer { event -> eventListener.onEvent(event) },
    transformer = { input -> input }
)