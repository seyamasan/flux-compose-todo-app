# Flux Architecture + Jetpack Compose

This project is an Android application using Kotlin, Flux, Jetpack Compose, StateFlow, etc.

このプロジェクトは、Kotlin、Flux、Jetpack Compose、StateFlowなどを使用したAndroidアプリです。

<img width="1192" alt="flux" src="https://github.com/user-attachments/assets/cc5d9f8c-8abb-457f-b48f-78ba8d100f4e" />

<img width="400" alt="app_screen" src="https://github.com/user-attachments/assets/a948a738-af90-471a-b095-413e18ce6565" />

<img width="400" alt="app_screen_2" src="https://github.com/user-attachments/assets/8cf3b273-6884-4bed-807f-599a950a4a46" />

## Technology used.

- Jetpack Compose
- StateFlow
- JUnit4
- Truth
- MockK
- Robolectric

## Description

### View -> Action

<img width="1201" alt="view_and_action" src="https://github.com/user-attachments/assets/4ddba92e-a781-4cd7-b9e3-3eab705f358c" />

Actions are created in ActionsCreator for each event in the View.

Depending on the event, a callback function receives the value from the View and passes it to the ActionsCreator.

Viewの各イベントに応じてActionsCreatorにてActionを作成しています。

イベントによってはコールバック関数でViewから値受け取り、ActionsCreatorに渡しています。

### Action -> Dispatcher

<img width="1206" alt="action_and_dispatcher" src="https://github.com/user-attachments/assets/61e3501a-3270-4358-883d-b33ac2181ba8" />

The Action created by the ActionsCreator sends the Action to the Dispatcher for notification to the Store.

The types of TodoActionType are as follows.

ActionsCreatorによって作成されたActionを、Storeに通知するためにDispatcherにActionを送ります。

TodoActionTypeの種類は以下の通りです。

| Type | Description |
| --- | --- |
| `TODO_CREATE` | Create todo |
| `TODO_DESTROY` | Destroy todo |
| `TODO_COMPLETE` | Make todo complete |
| `TODO_UNCOMPLETE` | Uncompleted todo |
| `TODO_TOGGLE_COMPLETE_ALL` | Complete all todo |
| `TODO_DESTROY_COMPLETED` | Toggle all todo completion states |
| `TODO_UNDO_DESTROY` | Undo the discarded todo |

Data is HashMap<String, Any> or null.

データはHashMap<String, Any>またはnullです。

| String(Key) | Any(String or Long) |
| --- | --- |
| `key-text` | Text(String) |
| `key-id` | Id(Long) |


## References

https://github.com/lgvalle/android-flux-todo-app
