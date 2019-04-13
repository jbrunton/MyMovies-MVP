package com.jbrunton.mymovies.fixtures

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.test.rule.ActivityTestRule

open class FragmentTestRule<A : AppCompatActivity, F : Fragment>(
        activityClass: Class<A>,
        private val fragmentClass: Class<F>,
        initialTouchMode: Boolean,
        launchActivity: Boolean,
        private val launchFragment: Boolean
) : ActivityTestRule<A>(activityClass, initialTouchMode, launchActivity) {
    lateinit var fragment: F

    override fun afterActivityLaunched() {
        if (launchFragment) {
            launchFragment(createFragment())
        }
    }

    fun launchFragment(fragment: F?) {
        try {
            runOnUiThread {
                val instance = fragment ?: createFragment()
                this@FragmentTestRule.fragment = instance
                activity.supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content, instance)
                        .commitNow()
            }
        } catch (throwable: Throwable) {
            throw RuntimeException(throwable)
        }

    }

    protected open fun createFragment(): F {
        try {
            return fragmentClass.newInstance()
        } catch (e: InstantiationException) {
            throw AssertionError(String.format("%s: Could not insert %s into %s: %s",
                    javaClass.simpleName,
                    fragmentClass.simpleName,
                    activity.javaClass.simpleName,
                    e.message))
        } catch (e: IllegalAccessException) {
            throw AssertionError(String.format("%s: Could not insert %s into %s: %s",
                    javaClass.simpleName,
                    fragmentClass.simpleName,
                    activity.javaClass.simpleName,
                    e.message))
        }

    }

    companion object {
        fun <F : Fragment> create(
                fragmentClass: Class<F>,
                initialTouchMode: Boolean = false,
                launchActivity: Boolean = true,
                launchFragment: Boolean = true): FragmentTestRule<*, F> {
            return FragmentTestRule(AppCompatActivity::class.java, fragmentClass, initialTouchMode, launchActivity, launchFragment)
        }
    }
}