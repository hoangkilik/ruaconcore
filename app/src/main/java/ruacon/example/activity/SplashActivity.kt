package ruacon.example.activity

import ruacon.presenter.splash.BaseSplashActivity

/**
 * Created by Nguyen Tien HOANG on 2018-01-25.
 */

class SplashActivity : BaseSplashActivity() {

    override val nextActivityClass: Class<*>
        get() = MainActivity::class.java
}