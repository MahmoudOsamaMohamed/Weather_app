package com.mahmoud.weatherapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.SmallTest
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.FavourateDao
import com.mahmoud.weatherapp.model.db.RoomDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@SmallTest
class FavourateDaoTest {
    lateinit var favourateDao: FavourateDao
    lateinit var database:RoomDB
    @get:Rule
    val mainCroutineRule = MainCoroutineRule()
    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RoomDB::class.java
        ).build()
        favourateDao = database.favourateDao()
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun insert_favourate_and_check_it() = runBlocking {
        //arrange
        val favourate = Favourate("cairo","20","30","القاهرة")
        //act
        favourateDao.insert(favourate)
        //assert
        val result = favourateDao.getAllFavourate().first()
        assertThat(result.last(), `is`(favourate))

        }
    @Test
    fun delete_favourate_and_check_it() = runBlocking {
        //arrange
        val favourate = Favourate("cairo","20","30","القاهرة")
        favourateDao.insert(favourate)
        //act
        favourateDao.delete(favourate)
        //assert
        val result = favourateDao.getAllFavourate().first()
        assertThat(result.size, `is`(0))
    }
    @Test
    fun getAllFavourate_insert_favourate_and_check_the_size() = runBlocking {
        //arrange
        val favourate = Favourate("cairo","20","30","القاهرة")
        favourateDao.insert(favourate)
        //act
        favourateDao.getAllFavourate()
        //assert
        val result = favourateDao.getAllFavourate().first()
        assertThat(result.size, `is`(1))
    }
}