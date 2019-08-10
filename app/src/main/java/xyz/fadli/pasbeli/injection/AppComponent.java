package xyz.fadli.pasbeli.injection;

import android.app.Application;

import xyz.fadli.pasbeli.global.PasBeli;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * @author Mahendri
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ActivityBuilderModule.class,
        ViewModelModule.class,
        ApiModule.class
})
public interface AppComponent {
    void inject(PasBeli pasBeli);

    @Component.Builder
    interface Builder {
        AppComponent build();
        @BindsInstance Builder application(Application application);
    }
}
