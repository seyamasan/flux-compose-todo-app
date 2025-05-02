package com.example.fluxcomposetodoapp.actions

import com.example.fluxcomposetodoapp.dispatcher.Dispatcher
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ActionsCreatorTest {
    @Test
    fun instantiation_test() {
        // インスタンス化
        val actionsCreator = ActionsCreator.get(Dispatcher.get())

        // nullではないことを確認
        assertThat(actionsCreator).isNotNull()
    }
}