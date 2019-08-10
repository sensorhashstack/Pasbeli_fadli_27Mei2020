package xyz.fadli.pasbeli.injection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import xyz.fadli.pasbeli.ui.addharga.HargaViewModel;
import xyz.fadli.pasbeli.ui.addpasar.AddPasarViewModel;
import xyz.fadli.pasbeli.ui.editpasar.EditPasarViewModel;
import xyz.fadli.pasbeli.ui.main.MainViewModel;
import xyz.fadli.pasbeli.ui.popup.PopVM;
import xyz.fadli.pasbeli.ui.splash.SplashViewModel;
import xyz.fadli.pasbeli.ui.history.VDViewModel;
import xyz.fadli.pasbeli.viewmodel.PasBeliViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author Mahendri
 */

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel bindSplashViewModel(SplashViewModel splashViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HargaViewModel.class)
    abstract ViewModel bindHargaViewModel(HargaViewModel hargaViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddPasarViewModel.class)
    public abstract ViewModel bindApiPasarViewModel(AddPasarViewModel addPasarViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditPasarViewModel.class)
    abstract ViewModel bindEditPasarViewModel(EditPasarViewModel editPasarViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VDViewModel.class)
    abstract ViewModel bindVDViewModel (VDViewModel vDViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopVM.class)
    abstract ViewModel bindPopVM (PopVM popVM);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PasBeliViewModelFactory factory);
}
