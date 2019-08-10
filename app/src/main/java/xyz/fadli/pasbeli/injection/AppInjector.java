package xyz.fadli.pasbeli.injection;

import xyz.fadli.pasbeli.global.PasBeli;

/**
 * @author Mahendri
 */

public class AppInjector {

    private AppInjector() {}

    public static void init(PasBeli pasBeli) {
        DaggerAppComponent.builder().application(pasBeli).build().inject(pasBeli);
    }
}
