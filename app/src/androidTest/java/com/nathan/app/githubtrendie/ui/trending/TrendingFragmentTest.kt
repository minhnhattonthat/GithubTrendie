package com.nathan.app.githubtrendie.ui.trending

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nathan.app.githubtrendie.R
import com.nathan.app.githubtrendie.testing.SingleFragmentActivity
import com.nathan.app.githubtrendie.util.DataBindingIdlingResourceRule
import com.nathan.app.githubtrendie.util.EspressoTestUtil
import com.nathan.app.githubtrendie.vo.Repo
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TrendingFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)

    private val repoLiveData = MutableLiveData<List<Repo>>()

    private lateinit var viewModel: TrendingViewModel

    private val repoFragment = TrendingFragment()

    @Before
    fun init() {
//        viewModel = mock(TrendingViewModel::class.java)
//        doNothing().`when`(viewModel)
//        `when`(viewModel.repos).thenReturn(repoLiveData)
//        repoFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)

        activityRule.activity.setFragment(repoFragment)
        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun testLoading() {
        onView(withId(R.id.repo_list)).check(matches(isDisplayed()))
        onView(withId(R.id.offline_layout)).check(matches(not(isDisplayed())))
    }
}