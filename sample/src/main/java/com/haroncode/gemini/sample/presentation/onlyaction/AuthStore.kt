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
import org.reactivestreams.Publisher
import javax.inject.Inject

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

        data class ChangeLogin(val login: String) : Action()
        data class ChangePassword(val password: String) : Action()

        object LoginClick : Action()
        data class AuthResult(val resource: Resource<AuthResponse>) : Action()
    }

    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isButtonLoginEnable: Boolean = false,
        val loginErrorHint: String = "",
        val passwordErrorHint: String = ""
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
            is Action.ChangeLogin -> {
                val loginErrorHint = if (action.login.isEmpty()) {
                    "Must be fill"
                } else {
                    ""
                }
                val isButtonLoginEnable = state.passwordErrorHint.isEmpty() && loginErrorHint.isEmpty()
                state.copy(
                    login = action.login,
                    loginErrorHint = loginErrorHint,
                    isButtonLoginEnable = isButtonLoginEnable
                )
            }
            is Action.ChangePassword -> {
                val passwordErrorHint = if (action.password.isEmpty()) {
                    "Must be fill"
                } else {
                    ""
                }
                val isButtonLoginEnable = passwordErrorHint.isEmpty() && state.loginErrorHint.isEmpty()
                state.copy(
                    password = action.password,
                    passwordErrorHint = passwordErrorHint,
                    isButtonLoginEnable = isButtonLoginEnable
                )
            }
            is AuthResult -> state.copy(isLoading = action.resource is Resource.Loading)
            else -> state
        }

    }

    class MiddlewareImpl(
        private val authRepository: AuthRepository,
        private val schedulersProvider: SchedulersProvider
    ) : Middleware<Action, State, Action> {

        override fun invoke(action: Action, state: State): Publisher<Action> = when (action) {
            is Action.LoginClick -> authRepository.auth(state.login, state.password)
                .observeOn(schedulersProvider.computation())
                .map(::AuthResult)
            else -> action.asFlowable()
        }
    }

}