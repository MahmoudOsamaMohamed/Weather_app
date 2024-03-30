package com.mahmoud.weatherapp.model.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mahmoud.weatherapp.MainCoroutineRule
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest{
    lateinit var localDataSource:ILocalDataSource
    lateinit var database:RoomDB
    @get:Rule
    val mainCroutineRule = MainCoroutineRule()
    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RoomDB::class.java
        ).allowMainThreadQueries()
            .build()
        localDataSource = LocalDataSourceForTest(database.favourateDao(),database.alarmItemDao(),database.cashWeatherDao(),
            getApplicationContext()
        )
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun `insertToFavourate_retreiveFavourate`()= runBlocking {
        //arrange
        val favourate = Favourate("cairo","20","30","القاهرة")
        //act
        localDataSource.insertToFavourate(favourate)
        //assert
        val result = localDataSource.getAllFavourate().first()
        assertThat(result.last(), `is`(favourate))
    }
    @Test
    fun `deleteFromFavourate_retreiveFavourate`()= runBlocking {
        //arrange
        val favourate = Favourate("cairo","20","30","القاهرة")
        localDataSource.insertToFavourate(favourate)
        //act
        localDataSource.deleteFromFavourate(favourate)
        //assert
        val result = localDataSource.getAllFavourate().first()
        assertThat(result.size, `is`(0))
    }
    @Test
    fun insertToAlarmItem_retreiveAlarmItem()= runBlocking {
        //arrange
        val alarmItem = AlarmItem(1,0,0,0,"masseage","cairo")
        //act
        localDataSource.insertToAlarm(alarmItem)
        //assert
        val result = localDataSource.getAllAlarm().first()
        assertThat(result.last(), `is`(alarmItem))
    }
    @Test
    fun deleteFromAlarmItem_retreiveAlarmItem()= runBlocking {
        //arrange
        val alarmItem =  AlarmItem(1,0,0,0,"masseage","cairo")
        localDataSource.insertToAlarm(alarmItem)
        //act
        localDataSource.deleteFromAlarm(1)
        //assert
        val result = localDataSource.getAllAlarm().first()
        assertThat(result.size, `is`(0))
    }
    @Test
    fun getAllFavourate_insert_some_Favourates_and_check_the_size()= runBlocking {
        //arrange
        val favourate = Favourate("cairo","20","30","القاهرة")
        val favourate2 = Favourate("alex","20","35","اسكندرية")
        val favourate3 = Favourate("aswan","25","30","اسوان")
        localDataSource.insertToFavourate(favourate)
        localDataSource.insertToFavourate(favourate2)
        localDataSource.insertToFavourate(favourate3)

        //act
        localDataSource.getAllFavourate()
        //assert
        val result = localDataSource.getAllFavourate().first()
        assertThat(result.size, `is`(3))
    }
}