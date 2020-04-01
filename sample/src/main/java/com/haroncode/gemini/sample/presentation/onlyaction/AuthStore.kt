package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.core.elements.EventProducer
import com.haroncode.gemini.core.elements.Middleware
import com.haroncode.gemini.core.elements.Reducer
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.domain.model.Resource
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import com.haroncode.gemini.sample.domain.repository.AuthRepository
import com.haroncode.gemini.sample.domain.system.SchedulersProvider
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.*
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.Action.AuthResult
import com.haroncode.gemini.sample.util.asFlowable
import com.haroncode.gemini.store.OnlyActionStore
import javax.inject.Inject
import org.reactivestreams.Publisher

@PerFragment
class AuthStore @Inject constructor(
    authRepository: AuthRepository,
    schedulersProvider: SchedulersProvider
) : OnlyActionStore<Action, State, Event>(
    initialState = State(),
    reducer = ReducerImpl(),
    eventProducer = EventProducerImpl(),
    middleware = MiddlewareImpl(authRepository, schedulersProvider)
) {

    sealed class Action {

        data class ChangeEmail(val login: String) : Action()
        data class ChangePassword(val password: String) : Action()

        object LoginClick : Action()
        data class AuthResult(val resource: Resource<AuthResponse>) : Action()
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isButtonLoginEnable: Boolean = false,
        val emailErrorHint: String? = null,
        val passwordErrorHint: String? = null
    )

    sealed class Event {

        object Success : Event()
        data class Fail(val throwable: Throwable) : Event()
    }

    class EventProducerImpl : EventProducer<State, Action, Event> {

        override fun invoke(state: State, action: Action): Event? {
            return when {
                action is AuthResult && action.resource is Resource.Error -> Event.Fail(action.resource.throwable)
                action is AuthResult && action.resource is Resource.Data -> Event.Success
                else -> null
            }
        }
    }

    class ReducerImpl : Reducer<State, Action> {

        override fun invoke(state: State, action: Action): State = when (action) {
            is Action.ChangeEmail -> {
                val emailErrorHint = emptyHint(action.login)
                val isButtonLoginEnable = state.passwordErrorHint.isNullOrEmpty() && emailErrorHint.isEmpty()
                state.copy(
                    email = action.login,
                    emailErrorHint = emailErrorHint,
                    isButtonLoginEnable = isButtonLoginEnable
                )
            }
            is Action.ChangePassword -> {
                val passwordErrorHint = emptyHint(action.password)
                val isButtonLoginEnable = state.emailErrorHint.isNullOrEmpty() && passwordErrorHint.isEmpty()
                state.copy(
                    password = action.password,
                    passwordErrorHint = passwordErrorHint,
                    isButtonLoginEnable = isButtonLoginEnable
                )
            }
            is AuthResult -> state.copy(isLoading = action.resource is Resource.Loading)
            else -> state
        }

        private fun emptyHint(value: String) = if (value.isEmpty()) {
            "Must be fill"
        } else {
            ""
        }
    }

    class MiddlewareImpl(
        private val authRepository: AuthRepository,
        private val schedulersProvider: SchedulersProvider
    ) : Middleware<Action, State, Action> {

        override fun invoke(action: Action, state: State): Publisher<Action> = when (action) {
            is Action.LoginClick -> authRepository.auth(state.email, state.password)
                .observeOn(schedulersProvider.computation())
                .map(::AuthResult)
            else -> action.asFlowable()
        }
    }
}
