package com.tanexc.bluetoothtool.domain
sealed class Battery {
    data class HeadPhoneBattery(
        val batteryLevel: Int = -1
    ) : Battery() {
        override fun toString(): String {
            return "Battery: ${batteryLevel.takeIf { it != -1 }?: " - "} %"
        }
    }

    data class EarBudsBattery(
        val leftBatteryLevel: Int = -1,
        val rightBatteryLevel: Int = -1,
        val caseBatteryLevel: Int = -1
    ) : Battery() {
        override fun toString(): String {
            return """
                Left: ${leftBatteryLevel.takeIf { it != -1 } ?: " - "} % Case: ${caseBatteryLevel.takeIf { it != -1 } ?: " - "} % Right: ${rightBatteryLevel.takeIf { it != -1 } ?: " - "} %
                """.trimIndent()
        }
    }

    object Undefined : Battery() {
        override fun toString(): String {
            return "Battery level is undefined"
        }
    }
}