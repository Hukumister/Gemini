package com.haroncode.gemini.routing

import com.haroncode.gemini.connection.BaseConnectionRule
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher

class EventListenerConnection<Event : Any>(
    eventPublisher: Publisher<Event>,
    eventListener: EventListener<Event>
) : BaseConnectionRule<Event, Event>(
    publisher = eventPublisher,
    consumer = Consumer { event -> eventListener.onEvent(event) },
    transformer = { input -> input }
)