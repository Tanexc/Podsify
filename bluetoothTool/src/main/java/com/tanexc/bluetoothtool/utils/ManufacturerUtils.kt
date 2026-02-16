package com.tanexc.bluetoothtool.utils

import android.Manifest
import android.media.AudioDeviceInfo
import androidx.annotation.RequiresPermission
import com.tanexc.bluetoothtool.domain.model.Manufacturer

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun AudioDeviceInfo.getManufacturer(): Manufacturer {
    val name = this.productName.toString().uppercase()

    return when {
        isApple(name) -> Manufacturer.Apple
        isSamsung(name) -> Manufacturer.Samsung
        isXiaomi(name) -> Manufacturer.Xiaomi
        isMarshall(name) -> Manufacturer.Marshall
        isMeizu(name) -> Manufacturer.Meizu
        isHuawei(name) -> Manufacturer.Huawei
        isSony(name) -> Manufacturer.Sony
        else -> Manufacturer.Unknown
    }
}

private fun isApple(name: String): Boolean {
    return name.contains("AIRPODS") ||
            name.contains("BEATS") ||
            name.contains("APPLE")
}

private fun isSamsung(name: String): Boolean {
    return name.contains("SAMSUNG") ||
            name.contains("GALAXY BUDS") ||
            name.contains("GALAXYBUDS")
}

private fun isXiaomi(name: String): Boolean {
    return name.contains("XIAOMI") ||
            name.contains("REDMI") ||
            name.contains("HAYL") ||
            name.contains("AIRDOTS")
}

private fun isMarshall(name: String): Boolean {
    return name.contains("MARSHALL") ||
            name.contains("MAJOR") ||
            name.contains("MINOR")
}

private fun isMeizu(name: String): Boolean {
    return name.contains("POP") ||
            (name.contains("EP") && name.matches(".*EP\\d+.*".toRegex())) ||
            name.contains("HD50")
}

private fun isHuawei(name: String): Boolean {
    return name.contains("HUAWEI") ||
            name.contains("HONOR") ||
            name.contains("FREEBUDS") ||
            name.contains("CM-HD") ||
            (name.contains("TALK") && name.contains("BAND"))
}

private fun isSony(name: String): Boolean {
    return name.contains("SONY") ||
            name.contains("WH-") ||
            name.contains("WF-") ||
            name.contains("WI-") ||
            name.contains("XB")
}
