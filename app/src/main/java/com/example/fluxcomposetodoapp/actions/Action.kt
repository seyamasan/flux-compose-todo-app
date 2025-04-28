package com.example.fluxcomposetodoapp.actions

class Action private constructor(
    val type: String,
    val data: HashMap<String, Any>
) {
    companion object {
        fun type(type: String): Builder {
            return Builder().with(type)
        }
    }

    class Builder {
        private var type: String? = null
        private var data: HashMap<String, Any> = HashMap()

        fun with(type: String): Builder {
            this.type = type
            this.data = HashMap()
            return this
        }

        fun bundle(key: String, value: Any): Builder {
            data[key] = value
            return this
        }

        fun build(): Action {
            val exception = IllegalArgumentException(
                "At least one key is required.\n" +
                "（少なくとも1つのキーが必要です。）"
            )
            val finalType = type ?: throw exception

            if (finalType.isEmpty()) {
                throw exception
            }

            return Action(finalType, data)
        }
    }
}