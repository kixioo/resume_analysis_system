package com.baker.pojo

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.function.Consumer
import kotlin.coroutines.*
class CallableList<T> constructor(consumer:Consumer<T>) {
    private var list:List<T> = mutableListOf<T>()

    private var consumer:Consumer<T>


    init {
        this.consumer = consumer
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun execute(t:T){
        GlobalScope.launch {
            consumer.accept(t)
            list = list - t
        }
    }

    fun add(t:T){
        list = list + t
        execute(t);
    }

}
