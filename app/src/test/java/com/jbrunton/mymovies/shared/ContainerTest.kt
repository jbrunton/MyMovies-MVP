package com.jbrunton.mymovies.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ContainerTest {

    lateinit var container: Container

    @Before
    fun setUp() {
        container = Container()
    }

    @Test
    fun resolvesSingletons() {
        container.single{ Foo() }

        val foo1: Foo = container.get()
        val foo2: Foo = container.get()

        assertThat(foo1).isEqualTo(foo2)
    }

    @Test
    fun resolvesWithFactories() {
        container.factory { Foo() }

        val foo1: Foo = container.get()
        val foo2: Foo = container.get()

        assertThat(foo1).isNotEqualTo(foo2)
    }

    class Foo
}