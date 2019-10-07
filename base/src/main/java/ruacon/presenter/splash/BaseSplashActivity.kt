package ruacon.presenter.splash

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.base.R
import ruacon.AppConfig

/**
 * Created by Nguyen Tien Hoang on 2018-01-25.
 */

abstract class BaseSplashActivity : AppCompatActivity() {

    private var mImgLogo: View? = null
    private var mImgBalloon: View? = null
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
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    private fun initViews() {
        mImgLogo = findViewById(R.id.img_rua_con_logo)
        mImgBalloon = findViewById(R.id.img_balloon)

        mImgLogo!!.post {
            mHeightLogo = mImgLogo!!.height
            if (mHeightBalloon != 0) {
                startAnimation()
            }
        }

        mImgBalloon!!.post {
            mHeightBalloon = mImgBalloon!!.height
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
        mImgLogo!!.startAnimation(animationLogo)

        val longSize = if (AppConfig.screenHeight > AppConfig.screenWidth) AppConfig.screenHeight else AppConfig.screenWidth
        val animationBalloon = TranslateAnimation(0f, 0f, AppConfig.screenHeight.toFloat(), (-longSize / 2).toFloat())
        animationBalloon.fillAfter = true
        animationBalloon.duration = 2000
        animationBalloon.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                mImgBalloon!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mImgBalloon!!.startAnimation(animationBalloon)
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