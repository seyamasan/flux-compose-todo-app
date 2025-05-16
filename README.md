# Flux Architecture + Jetpack Compose

This project is an Android application using Kotlin, Flux, Jetpack Compose, StateFlow, etc.

このプロジェクトは、Kotlin、Flux、Jetpack Compose、StateFlowなどを使用したAndroidアプリです。

<img width="1192" alt="flux" src="https://github.com/user-attachments/assets/cc5d9f8c-8abb-457f-b48f-78ba8d100f4e" />

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

Viewの各イベントに応じてActionsCreatorにてActionを作成しています。

Depending on the event, a callback function receives the value from the View and passes it to the ActionsCreator.

イベントによってはコールバック関数でViewから値受け取り、ActionsCreatorに渡しています。

## References

https://github.com/lgvalle/android-flux-todo-app
