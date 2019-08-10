package xyz.fadli.pasbeli.injection

import com.google.android.gms.location.LocationRequest
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

@Module
class LocationHelperModule {

    @Provides
    internal fun provideLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = TimeUnit.SECONDS.toMillis(5)

        return locationRequest
    }

}