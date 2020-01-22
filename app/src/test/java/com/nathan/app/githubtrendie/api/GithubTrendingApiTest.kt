package com.nathan.app.githubtrendie.api

import com.nathan.app.githubtrendie.util.RxSchedulerRule
import com.nathan.app.githubtrendie.vo.Repo
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
class GithubTrendingApiTest {

    @Rule
    @JvmField
    val testSchedulerRule = RxSchedulerRule()

    private lateinit var api: GithubTrendingApi

    private lateinit var mockWebServer: MockWebServer

    private lateinit var testScheduler: TestScheduler

    private lateinit var testObserver: TestObserver<List<Repo>>

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        testScheduler = TestScheduler()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubTrendingApi::class.java)
        testObserver = TestObserver()
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
        RxJavaPlugins.setIoSchedulerHandler(null)
    }

    @Test
    fun getRepositories() {
        enqueueResponse("repositories.json")

        api.getRepositories().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertComplete()

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0) { list ->
            assertThat(list.size, `is`(2))
            val repo = list[0]
            assertThat(repo.author, `is`("kbariotis"))
            assertThat(repo.name, `is`("templates"))
            val repo2 = list[1]
            assertThat(repo2.name, `is`("ApplicationInspector"))
            list.size == 2
        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}