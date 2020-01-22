package com.nathan.app.githubtrendie.repository

import android.content.Context
import android.content.SharedPreferences
import com.nathan.app.githubtrendie.api.GithubTrendingApi
import com.nathan.app.githubtrendie.db.AppDatabase
import com.nathan.app.githubtrendie.db.RepoDao
import com.nathan.app.githubtrendie.util.RxSchedulerRule
import com.nathan.app.githubtrendie.util.TestUtil
import com.nathan.app.githubtrendie.vo.Repo
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


@RunWith(JUnit4::class)
class RepoRepositoryTest {

    @Rule
    @JvmField
    val testSchedulerRule = RxSchedulerRule()

    private lateinit var repository: RepoRepository

    private val dao = mock(RepoDao::class.java)

    private val service = mock(GithubTrendingApi::class.java)

    private lateinit var testObserver: TestObserver<List<Repo>>

    @Before
    fun init() {
        val db = mock(AppDatabase::class.java)
        val sharedPrefs =
            mock(SharedPreferences::class.java)
        val context = mock<Context>(Context::class.java)
        `when`(db.repoDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs)
        repository = RepoRepository( service, dao, sharedPrefs)
        testObserver = TestObserver()
    }

    @Test
    fun getReposFromNetwork() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        `when`(service.getRepositories()).thenReturn(Observable.just(listOf(repo)))

        repository.refreshedRepo(false).doOnError { it.printStackTrace() }.subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
    }
}