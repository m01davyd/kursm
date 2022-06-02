import kotlinx.serialization.Serializable

@Serializable
class Teachers(
    val lastname: String,
    val firstname: String,
    val middleName: String
) {
    val fullname: String = "$lastname $firstname $middleName"
}