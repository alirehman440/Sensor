package codebuster.english.vocabulary.sensor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL: String = "https://api.gearupkit.com/"
    //private val BASE_URL: String = "https://jsonplaceholder.typicode.com/"


    val retrofitBuilder: Retrofit.Builder by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    }

    val apiService: SensorApi by lazy {
        retrofitBuilder
            .build()
            .create(SensorApi::class.java)

    }

}