package falafel_stand_2



import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*
import falafel_stand.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

import log
import kotlin.coroutines.CoroutineContext

fun main(args: Array<String>) {

    val orders = listOf(
        Menu.FalafelInAPlate(listOf(Salad.RedOnion,Salad.SpicySalad),FalafelBalls.OvenCookedBalls),
        Menu.FalafelInAPlate(listOf(Salad.RedOnion,Salad.SpicySalad),FalafelBalls.OvenCookedBalls),
        Menu.FalafelInPita(listOf(Salad.RedOnion,Salad.RedOnion),FalafelBalls.FriedBalls),
        Menu.FalafelInPita(listOf(Salad.TomatoSalad,Salad.SpicySalad),FalafelBalls.FriedBalls)
    )
    val start = System.currentTimeMillis()
    makeFalafel(orders)
    log( " ###### Make falafel without threads or coroutines ${System.currentTimeMillis() - start} ")


    runBlocking {
        var time = measureTimeMillis {

            falafelWithCoroutine(orders)
        }
        log(" ###### falafel with coroutine $time")
        time = measureTimeMillis {
            falafelWithCoroutineAndDispatcher(orders)
        }

        log(" ###### falafel with coroutine and dispatcher $time")

        time = measureTimeMillis {
            falafelWithChannel(orders)
        }
        log(" ###### falafel with channel $time" )

    }



    // ### popular types of channels:

    val channel1 = Channel<Menu>(capacity = Channel.RENDEZVOUS)
    val channel2 = Channel<Menu>(capacity = Channel.CONFLATED)
    val channel3 = Channel<Menu>(capacity = 10)
    val channel4 = Channel<Menu>(capacity = Channel.UNLIMITED)

    // ### Context examples
    Dispatchers.Default
    Dispatchers.IO
    Dispatchers.Main
    Dispatchers.Unconfined





}



private suspend fun CoroutineScope.falafelWithChannel(orders: List<Menu>) {
    val ordersChannel = Channel<Menu>()
    val job = launch {
        for (order in orders) {
            ordersChannel.send(order)
        }
        ordersChannel.close()
    }
    coroutineScope {
        launch(CoroutineName("falafel_routine_channel")) { makeFalafelWithCoroutineAndChannel(ordersChannel) }
        launch(CoroutineName("falafel_routine_channel2")) { makeFalafelWithCoroutineAndChannel(ordersChannel) }
    }
}


private suspend fun makeFalafelWithCoroutineAndChannel(ordersChannel :ReceiveChannel<Menu>){


    for(order in ordersChannel){
        log("processing order: ${order}")
        val dish = when(order){
            is Menu.FalafelInPita -> {
                val salads = getSalads(order.salads)
                val flafelBalls = getFalafelBalls(order.falafel)
                assembleDishInPita(order, salads, flafelBalls)
            }
            is Menu.FalafelInAPlate -> {
                val salads = getSalads(order.salads)
                val falafelBalls = getFalafelBalls(order.falafel)
                assemblePlate(order, salads, falafelBalls)
            }
        }

        log("SERVE: $dish")
    }

}

private suspend fun falafelWithCoroutine(orders: List<Menu>) {
    coroutineScope {
        launch(CoroutineName("falafel_routine")) { makeFalafel(orders) }
    }
}

private suspend fun falafelWithCoroutineAndDispatcher(orders: List<Menu>) {
    coroutineScope {
        launch(Dispatchers.Default + CoroutineName("falafel_routine_with_thread_pool")) {
            makeFalafel(orders)
        }
    }
}




