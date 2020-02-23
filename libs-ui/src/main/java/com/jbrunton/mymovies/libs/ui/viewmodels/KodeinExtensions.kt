package com.jbrunton.mymovies.libs.ui.viewmodels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.TT
import org.kodein.di.direct

class KodeinViewModelFactory(
        val kodein: Kodein
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            kodein.direct.Instance(TT(modelClass))
}

class KodeinViewModelFactoryWithArgument<T : Any>(
        val kodein: Kodein,
        val argClass: Class<T>,
        val arg: T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            kodein.direct.Instance(type = TT(modelClass), arg = arg, argType = TT(argClass))
}

fun <T, VM: ViewModel> T.resolveViewModel(
        viewModelClass: Class<VM>
): VM
        where T : FragmentActivity, T : KodeinAware
{
    return ViewModelProvider(this, KodeinViewModelFactory(kodein)).get(viewModelClass)
}

fun <T, VM: ViewModel> T.resolveViewModel(
        viewModelClass: Class<VM>
): VM
        where T : Fragment, T : KodeinAware
{
    return ViewModelProvider(this, KodeinViewModelFactory(kodein)).get(viewModelClass)
}

fun <T, VM: ViewModel, S : Any> T.resolveViewModel(
        viewModelClass: Class<VM>,
        argClass: Class<S>,
        arg: S
): VM
        where T : FragmentActivity, T : KodeinAware
{
    val factory = KodeinViewModelFactoryWithArgument(kodein, argClass, arg)
    return ViewModelProvider(this, factory).get(viewModelClass)
}

fun <T, VM: ViewModel, S : Any> T.resolveViewModel(
        viewModelClass: Class<VM>,
        argClass: Class<S>,
        arg: S
): VM
        where T : Fragment, T : KodeinAware
{
    val factory = KodeinViewModelFactoryWithArgument(kodein, argClass, arg)
    return ViewModelProvider(this, factory).get(viewModelClass)
}

inline fun <T, reified VM: ViewModel> T.injectViewModel(): Lazy<VM>
        where T : FragmentActivity, T : KodeinAware
{
    return lazy { resolveViewModel(VM::class.java) }
}

inline fun <T, reified VM: ViewModel> T.injectViewModel(): Lazy<VM>
        where T : Fragment, T : KodeinAware {
    return lazy { resolveViewModel(VM::class.java) }
}

inline fun <T, reified VM: ViewModel, reified S : Any> T.injectViewModel(crossinline arg: () -> S): Lazy<VM>
        where T : FragmentActivity, T : KodeinAware
{
    return lazy { resolveViewModel(VM::class.java, S::class.java, arg()) }
}

inline fun <T, reified VM: ViewModel, reified S : Any> T.injectViewModel(crossinline arg: () -> S): Lazy<VM>
        where T : Fragment, T : KodeinAware
{
    return lazy { resolveViewModel(VM::class.java, S::class.java, arg()) }
}