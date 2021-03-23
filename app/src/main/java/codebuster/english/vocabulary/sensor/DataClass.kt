package codebuster.english.vocabulary.sensor

data class DataClass(
    val testing: List<Waqa>
)

data class Waqa(
    val acceX: Double,
    val acceY: Double,
    val acceZ: Double,
    val accuracy_level: Int,
    val activity_type: String,
    val altitude: Double,
    val app_version: String,
    val battery_level: Int,
    val climb: Double,
    val deviceUserName: String,
    val device_type: String,
    val direction: Double,
    val gesture_type: String,
    val gyroX: Double,
    val gyroY: Double,
    val gyroZ: Double,
    val heartRate: Double,
    val horizontal: Double,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val speed: Double,
    val timeStamp: String,
    val vertical: Double
)