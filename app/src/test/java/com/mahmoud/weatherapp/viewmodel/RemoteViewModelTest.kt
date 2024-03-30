package com.mahmoud.weatherapp.viewmodel

import com.mahmoud.weatherapp.IReposatory
import com.mahmoud.weatherapp.MainCoroutineRule
import com.mahmoud.weatherapp.fake.FakeRemote
import com.mahmoud.weatherapp.fake.FakeRepo
import com.mahmoud.weatherapp.model.Pojos.Current
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.result.ForecastResult
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class RemoteViewModelTest{
    @get:Rule
    val mainCroutineRule = MainCoroutineRule()
    lateinit var viewModel: RemoteViewModel
    lateinit var reposatory: IReposatory
    @Before
    fun setUp() {
        reposatory = FakeRepo()
        viewModel = RemoteViewModel(reposatory)
    }
    @Test
    fun `get weather  fatches weather`() = runBlockingTest {
        //act
        viewModel.getWeather("30.0","30.0","en")
        val result = viewModel.onlineWeather.value

        //assert
        assertThat(result,`is`(CoreMatchers.notNullValue()))
    }
    @Test
    fun `get cities fatches cities`() = runBlockingTest {
        //act
        viewModel.get_Cities("cairo","en")
        val result = viewModel.suggestions.value
        //assert
        assertThat(result,`is`(CoreMatchers.notNullValue()))
    }
    @Test
    fun `get locales fatches locales`() = runBlockingTest {
        //act
        viewModel.get_Locales("30.0","30.0","1")
        val result = viewModel.locales.value
        //assert
        assertThat(result,`is`(CoreMatchers.notNullValue()))
    }
    @Test
    fun `get cashed weather fatches weather`() = runBlockingTest {
        //act
        viewModel.getAllCashed()
        val result = viewModel.cashedWeather.value
        //assert
        assertThat(result,`is`(CoreMatchers.notNullValue()))
    }
    @Test
    fun `get favourates fatches favourates`() = runBlockingTest {
        //act
        viewModel.getAllFavourates()
        val result = viewModel.favourate.value
        //assert
        assertThat(result,`is`(CoreMatchers.notNullValue()))
    }

}