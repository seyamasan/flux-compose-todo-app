package com.example.fluxcomposetodoapp.actions

/**
 * Action
 * A class that generates data structures (action objects) to convey operations from the View.
 * Viewからの操作を伝達するためのデータ構造（アクションオブジェクト）を生成するクラス
 **/
class Action private constructor(
    val type: TodoActionType,
    val data: HashMap<String, Any>
) {
    companion object {
        fun type(type: TodoActionType): Builder {
            return Builder().with(type)
        }
    }

    class Builder {
        private var type: TodoActionType? = null
        private var data: HashMap<String, Any> = HashMap()

        fun with(type: TodoActionType): Builder {
            this.type = type
            this.data = HashMap()
            return this
        }

        fun setData(key: String, value: Any) {
            data[key] = value
        }

        fun build(): Action {
            val finalType = type ?: throw IllegalArgumentException(
                "Type is null.\n" +
                "（typeがnullです。）"
            )

            return Action(finalType, data)
        }
    }
}