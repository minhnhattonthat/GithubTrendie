package com.nathan.app.githubtrendie.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nathan.app.githubtrendie.util.TestUtil
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDaoTest : DbTest() {

    @Test
    fun insertAllAndRead() {
        val r1 = TestUtil.createRepo("foo1", "bar1", "desc1")
        val r2 = TestUtil.createRepo("foo2", "bar2", "desc1")
        db.beginTransaction()
        try {
            db.repoDao().deleteAll()
            db.repoDao().insertAll(r1, r2)
        } finally {
            db.endTransaction()
        }
        val list = db.repoDao().all
        assertThat(list.size, `is`(2))
        val first = list[0]

        assertThat(first.name, `is`("foo1"))
        assertThat(first.author, `is`("bar1"))
        assertThat(first.description, `is`("desc1"))

        val second = list[1]
        assertThat(second.name, `is`("foo2"))
        assertThat(second.author, `is`("bar2"))
        assertThat(second.description, `is`("desc2"))
    }

    @Test
    fun deleteAll() {
        val insertRows = Math.random().toInt()
        val repos = TestUtil.createRepos(
            insertRows,
            "foo",
            "bar",
            "desc"
        )
        db.beginTransaction()
        try {
            db.repoDao().insertAll(*repos.toTypedArray())
            db.repoDao().deleteAll()
        } finally {
            db.endTransaction()
        }
        val list = db.repoDao().all
        assertThat(list.size, `is`(0))
    }
}