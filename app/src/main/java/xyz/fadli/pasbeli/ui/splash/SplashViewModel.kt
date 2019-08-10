package xyz.fadli.pasbeli.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.entity.Resource
import xyz.fadli.pasbeli.entity.Resource.success
import xyz.fadli.pasbeli.model.User
import javax.inject.Inject

/**
 * @author Mahendri
 */

class SplashViewModel @Inject constructor (
    private val webService: WebService
) : ViewModel() {

    val iDName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private val mut = MutableLiveData<User>()
    val insertStatus = MutableLiveData<Resource<String>>()

    fun prepos(): User? {
        val iDNames = iDName.value
        val password123 = password.value
        val m = mut
        var user = m.value
        val web = CompositeDisposable().add(webService.splashStatus(iDNames, password123).subscribe( {
                s -> user = s
                insertStatus.postValue(success("SELAMAT DATANG"))
        }) {
                insertStatus.postValue(Resource.error(it.message, "MAAF ADA KESALAHAN"))
            }
        )
        web.toString()
        println("prepos")
        println(user)
        return user
    }
}