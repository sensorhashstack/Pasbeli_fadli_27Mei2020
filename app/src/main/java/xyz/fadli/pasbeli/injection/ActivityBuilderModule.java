package xyz.fadli.pasbeli.injection;

import xyz.fadli.pasbeli.ui.addharga.AddHargaActivity;
import xyz.fadli.pasbeli.ui.addpasar.AddPasarActivity;
import xyz.fadli.pasbeli.ui.editpasar.EditPasarActivity;
import xyz.fadli.pasbeli.ui.main.MainActivity;
import xyz.fadli.pasbeli.ui.popup.PopupVerifikasiActivity;
import xyz.fadli.pasbeli.ui.splash.SplashActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import xyz.fadli.pasbeli.ui.history.TambahDataActivity;

/**
 * @author Mahendri
 */

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {LocationHelperModule.class})
    public abstract AddHargaActivity bindAddHargaActivity();

    @ContributesAndroidInjector(modules = { LocationHelperModule.class})
    abstract AddPasarActivity bindAddPasarActivity();

    @ContributesAndroidInjector
    abstract EditPasarActivity bindEditPasarActivity();

    @ContributesAndroidInjector
    abstract PopupVerifikasiActivity bindpopupVerifikasiActivity();

    @ContributesAndroidInjector
    abstract TambahDataActivity bindtambahDataActivity();

}
