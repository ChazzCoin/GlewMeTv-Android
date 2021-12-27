package io.aokihome.glewmetv.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

fun main(func: () -> Unit) {
    CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
        func()
    }
}

//fun io(func: (CoroutineScope) -> Unit) {
//    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
//        scope.launch {
//            func(this)
//        }
//}