package com.nathan.app.githubtrendie.ui.trending

import com.nathan.app.githubtrendie.repository.RepoRepository
import com.nathan.app.githubtrendie.util.RxSchedulerRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class TrendingViewModelTest {

    @Rule
    @JvmField
    val testSchedulerRule = RxSchedulerRule()

    private val repository = mock(RepoRepository::class.java)
    private var repoViewModel = TrendingViewModel(repository)

    @Test
    fun retry() {
        verifyNoMoreInteractions(repository)
    }
}