import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): WeatherResponse
}

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
) {
    data class Main(val temp: Double)
    data class Weather(val description: String)
}