package com.haroncode.gemini.connection

import com.haroncode.gemini.connection.util.identityFlowableTransformer
import com.haroncode.gemini.core.StoreEventListener
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
class StoreEventListenerConnectionRule<Event : Any>(
    eventPublisher: Publisher<Event>,
    eventListener: StoreEventListener<Event>
) : BaseConnectionRule<Event, Event>(
    publisher = eventPublisher,
    consumer = Consumer { event -> eventListener.onEvent(event) },
    transformer = identityFlowableTransformer()
)
