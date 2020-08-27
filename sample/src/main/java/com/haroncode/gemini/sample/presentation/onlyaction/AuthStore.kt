package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.element.EventProducer
import com.haroncode.gemini.element.Middleware
import com.haroncode.gemini.element.Reducer
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.domain.model.Resource
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import com.haroncode.gemini.sample.domain.repository.AuthRepository
import com.haroncode.gemini.sample.presentation.onlyaction.AuthStore.Action.AuthResult
import com.haroncode.gemini.store.OnlyActionStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@PerFragment
class AuthStore @Inject constructor(
    authRepository: AuthRepository
) : OnlyActionStore<AuthStore.Action, AuthStore.State, AuthStore.Event>(
    initialState = State(),
    reducer = ReducerImpl(),
    eventProducer = EventProducerImpl(),
    middleware = MiddlewareImpl(authRepository),
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

        override fun produce(state: State, effect: Action): Event? {
            return when {
                effect is AuthResult && effect.resource is Resource.Error -> Event.Fail(effect.resource.throwable)
                effect is AuthResult && effect.resource is Resource.Data -> Event.Success
                else -> null
            }
        }
    }

    class ReducerImpl : Reducer<State, Action> {

        override fun reduce(state: State, effect: Action): State = when (effect) {
            is Action.ChangeEmail -> {
                val emailErrorHint = emptyHint(effect.login)
                val isButtonLoginEnable = state.passwordErrorHint.isNullOrEmpty() && emailErrorHint.isEmpty()
                state.copy(
                    email = effect.login,
                    emailErrorHint = emailErrorHint,
                    isButtonLoginEnable = isButtonLoginEnable
                )
            }
            is Action.ChangePassword -> {
                val passwordErrorHint = emptyHint(effect.password)
                val isButtonLoginEnable = state.emailErrorHint.isNullOrEmpty() && passwordErrorHint.isEmpty()
                state.copy(
                    password = effect.password,
                    passwordErrorHint = passwordErrorHint,
                    isButtonLoginEnable = isButtonLoginEnable
                )
            }
            is AuthResult -> state.copy(isLoading = effect.resource is Resource.Loading)
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
    ) : Middleware<Action, State, Action> {

        override fun execute(action: Action, state: State): Flow<Action> = when (action) {
            is Action.LoginClick ->
                authRepository
                    .auth(state.email, state.password)
                    .map { AuthResult(it) }
            else -> flowOf(action)
        }
    }
}
