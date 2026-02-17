package tech.bedson.roomassistant.data.model

data class GoveeCommandRequest(
    val device: String,
    val model: String,
    val cmd: GoveeCmd
)

data class GoveeCmd(
    val name: String,
    val value: Any
)
