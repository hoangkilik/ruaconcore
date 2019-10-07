package ruacon.presenter.splash

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.base.R
import kotlinx.android.synthetic.main.activity_base_splash.*
import ruacon.AppConfig

/**
 * Created by on 2018-01-25.
 */

abstract class BaseSplashActivity : AppCompatActivity() {

    private var mHeightLogo: Int = 0
    private var mHeightBalloon: Int = 0

    protected abstract val nextActivityClass: Class<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_splash)
        initSomeConfig()
        initViews()
        hideActionBar()
    }

    private fun initSomeConfig() {
        AppConfig.init(this)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun initViews() {
        ivRuaConLogo.post {
            mHeightLogo = ivRuaConLogo.height
            if (mHeightBalloon != 0) {
                startAnimation()
            }
        }

        ivBalloon?.post {
            mHeightBalloon = ivBalloon.height
            if (mHeightLogo != 0) {
                startAnimation()
            }
        }
    }

    private fun startAnimation() {
        val fromLogo = AppConfig.screenHeight + mHeightBalloon * 11 / 12
        val toLogo = (AppConfig.screenHeight - mHeightLogo) / 2
        val timeLogo = (fromLogo - toLogo) * 2 * 2000 / (3 * AppConfig.screenHeight)
        val animationLogo = TranslateAnimation(0f, 0f, fromLogo.toFloat(), toLogo.toFloat())
        animationLogo.fillAfter = true
        animationLogo.duration = timeLogo.toLong()
        animationLogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                playSound()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        ivRuaConLogo.startAnimation(animationLogo)

        val longSize = if (AppConfig.screenHeight > AppConfig.screenWidth) AppConfig.screenHeight else AppConfig.screenWidth
        val animationBalloon = TranslateAnimation(0f, 0f, AppConfig.screenHeight.toFloat(), (-longSize / 2).toFloat())
        animationBalloon.fillAfter = true
        animationBalloon.duration = 2000
        animationBalloon.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                ivBalloon.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        ivBalloon.startAnimation(animationBalloon)
    }

    private fun playSound() {
        val player = MediaPlayer.create(this, R.raw.ding)
        player.setOnCompletionListener {
            startActivity(Intent(this, nextActivityClass))
            finish()
        }
        player.start()
    }
}