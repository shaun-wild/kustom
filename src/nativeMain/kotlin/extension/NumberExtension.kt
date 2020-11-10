package extension

operator fun Number.times(other: Number) = when (this) {
    is Int -> when (other) {
        is Int -> this * other
        is Float -> this * other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    is Float -> when (other) {
        is Int -> this * other
        is Float -> this * other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    else -> throw IllegalArgumentException("Undefined arithmetic operation")
}

operator fun Number.plus(other: Number) = when (this) {
    is Int -> when (other) {
        is Int -> this + other
        is Float -> this + other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    is Float -> when (other) {
        is Int -> this + other
        is Float -> this + other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    else -> throw IllegalArgumentException("Undefined arithmetic operation")
}

operator fun Number.minus(other: Number) = when (this) {
    is Int -> when (other) {
        is Int -> this - other
        is Float -> this - other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    is Float -> when (other) {
        is Int -> this - other
        is Float -> this - other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    else -> throw IllegalArgumentException("Undefined arithmetic operation")
}

operator fun Number.div(other: Number) = when (this) {
    is Int -> when (other) {
        is Int -> this / other
        is Float -> this / other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    is Float -> when (other) {
        is Int -> this / other
        is Float -> this / other
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    else -> throw IllegalArgumentException("Undefined arithmetic operation")
}

operator fun Number.compareTo(other: Number) = when (this) {
    is Int -> when (other) {
        is Int -> this.compareTo(other)
        is Float -> this.compareTo(other)
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    is Float -> when (other) {
        is Int -> this.compareTo(other)
        is Float -> this.compareTo(other)
        else -> throw IllegalArgumentException("Undefined arithmetic operation")
    }
    else -> throw IllegalArgumentException("Undefined arithmetic operation")
}
