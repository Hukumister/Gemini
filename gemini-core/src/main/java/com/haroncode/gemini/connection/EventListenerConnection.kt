package com.haroncode.gemini.connection

import com.haroncode.gemini.connection.dsl.identityFlowableTransformer
import com.haroncode.gemini.core.EventListener
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
    transformer = identityFlowableTransformer()
)
