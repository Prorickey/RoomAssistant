package tech.bedson.roomassistant.data.model

data class GoveeDeviceListResponse(
    val data: GoveeDeviceData?,
    val message: String?,
    val code: Int?
)

data class GoveeDeviceData(
    val devices: List<GoveeDevice>
)

data class GoveeDevice(
    val device: String,
    val model: String,
    val deviceName: String,
    val controllable: Boolean,
    val retrievable: Boolean
)
