# Основные компоненты
* ***Reducer* - работа с состоянием**
* ***Middleware* - асинхронные запросы**
* ***Action* - действие пользователя**
* ***Effect* - внутренний action**
* ***State* - состояние для отображения**
* ***Store* - основной компонент содержащий всю бизнес-логику компонента**



# JustReducerStore
## Задача
* Сохрание State
* Изменение State без асинхронной работы

```kotlin 
class CounterStore() : JustReducerStore<Action, State, Nothing>(
    initialState = State(),
    reducer = ReducerImpl()
) {

    sealed class Action {
        object Increment : Action()
        object Decrement : Action()
    }

    data class State(
        val count: Int = 0
    )

    class ReducerImpl : Reducer<State, Action> {
        override fun invoke(state: State, action: Action) = when (action) {
            is Action.Increment -> state.copy(count = state.count + 1)
            is Action.Decrement -> state.copy(count = state.count - 1)
        }
    }
}
```
> Самый простой кейс, который не предусматривет сложную логику.