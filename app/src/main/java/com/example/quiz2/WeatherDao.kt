import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache WHERE cityName = :city")
    suspend fun getCity(city: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity)
}