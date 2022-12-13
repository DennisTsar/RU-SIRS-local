package data.soc


import general.Campus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable()
data class School(
    @SerialName("campus")
    @Serializable(with = CampusesAsStringSerializer::class)
    val campuses: List<Campus>, // seems to always be either one or all 3
    val code: String,
    val description: String,
    val homeCampus: Campus,
    val level: String,
)

object CampusesAsStringSerializer : KSerializer<List<Campus>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CampusesAsStringSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: List<Campus>) {
        encoder.encodeString(value.joinToString(","))
    }

    override fun deserialize(decoder: Decoder): List<Campus> {
        return decoder.decodeString().split(", ").map { Campus.valueOf(it) }
    }
}