package falafel_stand

import format


sealed class FalafelBalls {
    fun price(): Float = 2.0f

    object FriedBalls : FalafelBalls(){
        override fun toString(): String = "fried"
    }

    object OvenCookedBalls : FalafelBalls(){
        override fun toString(): String = "oven cooked"
    }
}


sealed class Salad {
    abstract fun price(): Float

    object RedOnion : Salad(){
        override fun price(): Float = 0.5f
        override fun toString(): String = "red onion"
    }

    object TomatoSalad : Salad(){
        override fun price(): Float = 0.6f
        override fun toString(): String = "tomato Salad"
    }

    object SpicySalad : Salad(){
        override fun price(): Float = 0.5f
        override fun toString(): String = "spicy salad"
    }

}

class  MixedSalad (val salads : List<Salad>, val price:Float) {
    fun price(): Float = price
    override fun toString(): String = "mixed Salad: ${salads.joinToString {it.toString() }}"
}



sealed class Menu {
    abstract fun price(): Float

    data class FalafelInPita(val salads: List<Salad>, val falafel: FalafelBalls): Menu() {
        override fun price() = 0.0f + salads.sumByDouble { it.price().toDouble()}.toFloat() + falafel.price()
        override fun toString() = "FalafelInPita(falafel=$falafel, salads=${salads.joinToString {it.toString() }}, price=$${price().format(2)}) "
    }

    data class FalafelInAPlate(val salads: List<Salad>, val falafel: FalafelBalls): Menu() {
        override fun price() =  0.0f + salads.sumByDouble { it.price().toDouble() * 2 }.toFloat() + falafel.price()
        override fun toString() = "FalafelInAPlate(falafel=$falafel, salads=${salads.joinToString {it.toString() }}, price=$${price().format(2)}) "
    }
}

sealed class Dish {
    data class FalafelInPitta(val order:Menu.FalafelInPita, val salads: MixedSalad, val falafel: FalafelBalls) :Dish()
    data class FalafelInAPlate(val order:Menu.FalafelInAPlate, val salads: MixedSalad, val falafel: FalafelBalls) :Dish()
}

