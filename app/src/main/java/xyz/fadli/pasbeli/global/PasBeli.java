package xyz.fadli.pasbeli.global;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import io.fabric.sdk.android.Fabric;
import xyz.fadli.pasbeli.BuildConfig;
import xyz.fadli.pasbeli.injection.AppInjector;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;

/**
 * @author Mahendri
 */

public class PasBeli extends Application implements HasActivityInjector, HasServiceInjector {


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;
    @Inject
    FirebaseJobDispatcher dispatcher;

    private static PasBeli mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        mInstance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }

        AppInjector.init(this);


    }

    public static synchronized PasBeli getInstance() {
        return mInstance;
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }


}
