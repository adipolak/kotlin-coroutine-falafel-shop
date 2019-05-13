package falafel_stand

import log
import java.lang.Thread.sleep

fun main(args: Array<String>) {
    val orders = listOf(
        Menu.FalafelInAPlate(listOf(Salad.RedOnion,Salad.SpicySalad),FalafelBalls.OvenCookedBalls),
        Menu.FalafelInAPlate(listOf(Salad.RedOnion,Salad.SpicySalad),FalafelBalls.OvenCookedBalls),
        Menu.FalafelInPita(listOf(Salad.RedOnion,Salad.RedOnion),FalafelBalls.FriedBalls),
        Menu.FalafelInPita(listOf(Salad.TomatoSalad,Salad.SpicySalad),FalafelBalls.FriedBalls)
    )
    val start = System.currentTimeMillis()
    makeFalafel(orders)
    log( " make falafel ${System.currentTimeMillis() - start} ")


}


fun makeFalafel(orders :List<Menu>){

    for(order in orders){
        makeFalafel(order)
    }
}

fun makeFalafel(order :Menu){

        log("processing order: ${order}")
        val dish = when(order) {
            is Menu.FalafelInPita -> {
                val salads = getSalads(order.salads)
                val flafelBalls = getFalafelBalls(order.falafel)
                assembleDishInPita(order, salads, flafelBalls)
            }
            is Menu.FalafelInAPlate -> {
                val salads = getSalads(order.salads)
                val flafelBalls = getFalafelBalls(order.falafel)
                assemblePlate(order, salads, flafelBalls)
            }
        }

        log("SERVE: $dish")
}




fun assembleDishInPita(order:Menu.FalafelInPita,salads: MixedSalad, falafelBalls: FalafelBalls): Dish.FalafelInPitta {
    log(" adding salads and falafel to pita")
    sleep(10)
    return Dish.FalafelInPitta(order, salads,falafelBalls)
}

fun assemblePlate(order:Menu.FalafelInAPlate, salads: MixedSalad, falafelBalls: FalafelBalls): Dish.FalafelInAPlate {
    log(" adding salads and falafel to the plate")
    sleep(10)
    return Dish.FalafelInAPlate(order, salads,falafelBalls)
}

fun getFalafelBalls(falafel: FalafelBalls): FalafelBalls {
    log(" make  ${falafel.toString()}")
    sleep(5)

    return falafel
}

fun getSalads(salads: List<Salad>): MixedSalad {
    log(" cut salads for ${salads.toString()}")
    sleep(2 * salads.size.toLong())

    return MixedSalad(salads, salads.sumByDouble { it.price().toDouble() }.toFloat())
}




