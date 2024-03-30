package com.mahmoud.weatherapp

import com.mahmoud.weatherapp.fake.FakeLocal
import com.mahmoud.weatherapp.fake.FakeRemote
import com.mahmoud.weatherapp.model.Pojos.CityResponse
import com.mahmoud.weatherapp.model.Pojos.Current
import com.mahmoud.weatherapp.model.Pojos.Daily
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.Hourly
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.Pojos.Minutely
import com.mahmoud.weatherapp.model.db.CashWeather
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.ILocalDataSource
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.remote.IRemoteDataSource
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class ReposatoryTest{
    //arrange
    private val cashWeather: CashWeather = CashWeather(
        ForecastResponse(30.0,30.0,"time zone",0,
            Current(0,0,0,0.9,0.0,0,0,0.0,
                0.0,0,0,0.0,0,0.0, listOf()
            )
            ,listOf(),listOf(), listOf()
        ),"cairo","القاهرة")
    private val forecastResponse: ForecastResponse = ForecastResponse(30.0,30.0,"time zone",0,
        Current(0,0,0,0.9,0.0,0,0,0.0,
            0.0,0,0,0.0,0,0.0, listOf()
        )
        ,listOf(),listOf(), listOf()
    )
    private val cityResponse: CityResponse = CityResponse(
        listOf(),"type"
    )
    private val localeResponse: LocaleResponse = LocaleResponse(

    )
    private val favourate: Favourate = Favourate("cairo","20","30","القاهرة")

    private val alarmItem: AlarmItem = AlarmItem(1,0,0,0,"m","cairo",)
    private val localWeather = listOf(cashWeather)
    private val remoteWeather = listOf(forecastResponse)
    private val favourates = listOf(favourate)
    private val alarmList = listOf(alarmItem)
    lateinit var reposatory: Reposatory
    lateinit var localDataSource: ILocalDataSource
    lateinit var remoteDataSource: IRemoteDataSource
    @Before
    fun setUp() {

        localDataSource = FakeLocal(localWeather.toMutableList(),
            favourates.toMutableList(),
            alarmList.toMutableList())
        remoteDataSource = FakeRemote(forecastResponse,cityResponse,localeResponse)
        reposatory = Reposatory(localDataSource, remoteDataSource)
    }
    @Test
    fun `deleteAllCash should delete all cash weather`()= runBlocking {
        //act
        reposatory.deleteAllCash()
        val result = localDataSource.getAllCash().first()
        //assert
        assertThat(result, `is`(emptyList<CashWeather>()))
    }
    @Test
    fun `insertToFavourate should insert to favourate`()= runBlocking {
        //act
        reposatory.insertToFavourate(favourate)
        val result = localDataSource.getAllFavourate().first()
        //assert
        assertThat(result.last(), `is`(favourate))
    }
    @Test
    fun `deleteFromFavourate should delete from favourate`()= runBlocking {
        //act
        reposatory.deleteFromFavourate(favourate)
        val result = localDataSource.getAllFavourate().first()
        //assert
        assertThat(result, `is`(emptyList<Favourate>()))
    }
    @Test
    fun `insertToAlarm should insert to alarm`()= runBlocking {
        //act
        reposatory.insertToAlarm(alarmItem)
        val result = localDataSource.getAllAlarm().first()
        //assert
        assertThat(result.last(), `is`(alarmItem))
    }
    @Test
    fun `getWeather should return  result success of forecast`()= runBlocking {
        //act
        val result = reposatory.getWeather("30.0","30.0","cairo")
        //assert
        assertThat(result.first(), `is`(ForecastResult.Success(forecastResponse)))
    }

}