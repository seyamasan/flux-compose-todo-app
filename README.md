# Flux Architecture + Jetpack Compose

This project is an Android application using Kotlin, Flux, Jetpack Compose, StateFlow, etc.

このプロジェクトは、Kotlin、Flux、Jetpack Compose、StateFlowなどを使用したAndroidアプリです。

<img width="1192" alt="flux" src="https://github.com/user-attachments/assets/cc5d9f8c-8abb-457f-b48f-78ba8d100f4e" />

|<img width="400" alt="app_screen" src="https://github.com/user-attachments/assets/a948a738-af90-471a-b095-413e18ce6565" />|<img width="400" alt="app_screen_2" src="https://github.com/user-attachments/assets/8cf3b273-6884-4bed-807f-599a950a4a46" />|
|-|-|

|Unit Testing & UI Unit Testing|
|-|
|<img width="770" alt="unit_test" src="https://github.com/user-attachments/assets/a6e46a0f-9448-4d56-a561-a6b1f156dc51" />|

## Technology used.

- Jetpack Compose
- StateFlow
- JUnit4
- Truth
- MockK
- Robolectric

## Description

### View -> Action

<img width="1148" alt="view_and_action" src="https://github.com/user-attachments/assets/ff252573-76a1-4a6f-a005-751df3f91358" />

Actions are created in ActionsCreator for each event in the View.

Depending on the event, a callback function receives the value from the View and passes it to the ActionsCreator.

Viewの各イベントに応じてActionsCreatorにてActionを作成しています。

イベントによってはコールバック関数でViewから値受け取り、ActionsCreatorに渡しています。

### Action -> Dispatcher

<img width="1206" alt="action_and_dispatcher" src="https://github.com/user-attachments/assets/61e3501a-3270-4358-883d-b33ac2181ba8" />

The Action created by the ActionsCreator sends the Action to the Dispatcher to be passed to the Store.

The types of TodoActionType are as follows.

ActionsCreatorによって作成されたActionを、Storeに渡すためにDispatcherにActionを送ります。

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

### Dispatcher -> Store

<img width="1259" alt="dispatcher_and_store" src="https://github.com/user-attachments/assets/dda200ee-3798-47c6-bc88-93b753f3897f" />

The Dispatcher plays the role of an Emitter that outputs Action and Store events.

It passes those events to the Subscriber, Store.

This is achieved using StateFlow.

Store is pre-injected with Dispatcher.

Dispatcherは、ActionとStoreのイベントを出力するEmitterの役割を果たしています。

それらのイベントをSubscriberであるStoreに渡しています。

StateFlowを使って実現しています。

Storeには、あらかじめDispatcherを注入しています。

### Store -> View

<img width="1164" alt="store_and_view" src="https://github.com/user-attachments/assets/fbdf46e9-c15c-4393-b288-c35bf550f80f" />

Based on the Action received from the Dispatcher, the data managed in the Store is updated and an update event is output to the View.

The View that receives the update event calls the Store's get function to receive the updated data and reflect it in the View.

Dispatcherから受け取ったActionを元に、Storeで管理しているデータを更新してViewに対して更新イベントを出力しています。

更新イベントを受け取ったViewは、Storeのget関数を呼び出して更新後のデータを受け取りViewに反映します。

## References

https://github.com/lgvalle/android-flux-todo-app
