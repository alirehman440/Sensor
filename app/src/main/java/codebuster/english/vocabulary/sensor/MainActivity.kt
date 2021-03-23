package codebuster.english.vocabulary.sensor

import android.app.AlertDialog
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_GYROSCOPE
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var sensorName: String
    private var mGyroPresent: Boolean = false
    private var mAccelerometerPresent: Boolean = false
    private var sensorStarted: Boolean = false
    private lateinit var sensorLayout: LinearLayout
    private lateinit var emptyLayout: LinearLayout
    private lateinit var apiLayout: LinearLayout
    private lateinit var sensorButton: Button
    private lateinit var apiButton: Button
    private lateinit var accelerometerValuesx: TextView
    private lateinit var accelerometerValuesy: TextView
    private lateinit var accelerometerValuesz: TextView
    private lateinit var gyroscopeValuesx: TextView
    private lateinit var gyroscopeValuesy: TextView
    private lateinit var gyroscopeValuesz: TextView

    private lateinit var gestureValue: TextView
    private lateinit var deviceNameValue: TextView
    private lateinit var appVersionValue: TextView
    private lateinit var x: TextView
    private lateinit var y: TextView
    private lateinit var z: TextView
    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        sensorLayout = findViewById(R.id.sensorLayout)
        apiLayout = findViewById(R.id.apiLayout)
        emptyLayout = findViewById(R.id.emptyLayout)

        sensorButton = findViewById(R.id.sensorButton)
        apiButton = findViewById(R.id.apiButton)

        accelerometerValuesx = findViewById(R.id.accelerometerx)
        accelerometerValuesy = findViewById(R.id.accelerometery)
        accelerometerValuesz = findViewById(R.id.accelerometerz)
        gyroscopeValuesx = findViewById(R.id.gyroscopex)
        gyroscopeValuesy = findViewById(R.id.gyroscopey)
        gyroscopeValuesz = findViewById(R.id.gyroscopez)

        gestureValue = findViewById(R.id.gestureValue)
        deviceNameValue = findViewById(R.id.deviceNameValue)
        appVersionValue = findViewById(R.id.appVersionValue)


        x = findViewById(R.id.x)
        y = findViewById(R.id.y)
        z = findViewById(R.id.z)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mGyroPresent = mSensorManager.getDefaultSensor(TYPE_GYROSCOPE) != null
        mAccelerometerPresent = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null



        if (mGyroPresent) {
            mGyroscope = mSensorManager.getDefaultSensor(TYPE_GYROSCOPE)

        }
        if (mAccelerometerPresent) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        }

        sensorButton.setOnClickListener {
            sensorLayout.visibility = View.VISIBLE
            if (mAccelerometerPresent)
                mSensorManager.registerListener(
                    this,
                    mAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            else {
                accelerometerValuesy.text = "No Hardware Found"
            }
            if (mGyroPresent)
                mSensorManager.registerListener(
                    this,
                    mGyroscope,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            else {
                gyroscopeValuesy.text = "No Hardware Found"
            }
        }

        apiButton.setOnClickListener {

            stopSensors()
            ChangeLayoutVisibility(apiLayout)
            ApiCallMethod()

        }

    }


    fun ChangeLayoutVisibility(layout: LinearLayout) {

        if (layout == apiLayout) {
            apiLayout.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
            sensorLayout.visibility = View.GONE
        } else if (layout == emptyLayout) {
            apiLayout.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
            sensorLayout.visibility = View.GONE
        } else {
            apiLayout.visibility = View.GONE
            emptyLayout.visibility = View.GONE
            sensorLayout.visibility = View.VISIBLE
        }
    }

    private fun stopSensors() {
        if (mGyroPresent)
            mSensorManager.unregisterListener(this)
        else if (mAccelerometerPresent)
            mSensorManager.unregisterListener(this)
    }

    override fun onPause() {
        super.onPause()
        stopSensors()
        emptyLayout.visibility = View.VISIBLE
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        sensorName = p0!!.sensor!!.getName();

        if (sensorLayout.isVisible) {

            if (p0.sensor.type == TYPE_GYROSCOPE) {
                gyroscopeValuesx.text = "X: ${p0.values[0]} "
                gyroscopeValuesy.text = "Y: ${p0.values[1]} "
                gyroscopeValuesz.text = "Z: ${p0.values[2]}"
            }
            if (p0.sensor.type == TYPE_ACCELEROMETER) {
                accelerometerValuesx.text = "X: ${p0.values[0]}"
                accelerometerValuesy.text = "Y: ${p0.values[1]}"
                accelerometerValuesz.text = "Z: ${p0.values[2]}"
            }
        }

        Log.d(
            sensorName + ": X: " + p0.values[0],
            "; Y: " + p0.values[1] + "; Z: " + p0.values[2] + ";"
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }


    fun FillData(data: Waqa) {
        gestureValue.text = data.gesture_type
        deviceNameValue.text = data.deviceUserName
        appVersionValue.text = data.app_version
        x.text = data.acceX.toString()
        y.text = data.acceY.toString()
        z.text = data.acceZ.toString()

    }


    private fun ApiJsonMap(): JsonObject {
        var gsonObject = JsonObject()
        try {
            val jsonObj_ = JSONObject()
            jsonObj_.put("testkey", "AndroidGuru")
            val jsonParser = JsonParser()
            gsonObject = jsonParser.parse(jsonObj_.toString()) as JsonObject

            //print parameter
            Log.d("Response12:", "AS PARAMETER  $gsonObject")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return gsonObject
    }


    private fun ApiCallMethod() {
        try {
            showDilaog()
            val registerCall: Call<DataClass> =
                RetrofitClient.apiService.ApiName(ApiJsonMap())
            registerCall.enqueue(object : Callback<DataClass> {
                override fun onResponse(
                    registerCall: Call<DataClass>,
                    response: Response<DataClass>
                ) {
                    try {
                        hideDialog()

                        if (response.isSuccessful()) {
                            Log.d("ResponseApi", "success")
                            //Data of just first list item
                            FillData(response.body()!!.testing[0])
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        try {

                            Toast.makeText(this@MainActivity, "Error$e", Toast.LENGTH_SHORT).show()
                        } catch (e1: Resources.NotFoundException) {
                            e1.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DataClass>, t: Throwable) {
                    try {
                        Log.e("Tag", "error$t")
                        Toast.makeText(this@MainActivity, "Error$t", Toast.LENGTH_SHORT).show()

                    } catch (e: Resources.NotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(this@MainActivity, "Error${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })


        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

    }


    fun showDilaog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,

        builder.setView(R.layout.loading_dialog)
        dialog = builder.create()
        dialog.show()
    }

    fun hideDialog() {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}