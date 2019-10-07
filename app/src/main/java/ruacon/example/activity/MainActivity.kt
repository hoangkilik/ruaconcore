package ruacon.example.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ruacon.MenuUtil
import ruacon.constant.MenuAction
import ruacon.example.R
import ruacon.example.util.ToastUtil
import ruacon.presenter.listapps.RuaConAppsAdapter
import ruacon.presenter.listapps.RuaConAppsDialog
import ruacon.util.RuaConUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello.setOnClickListener { showRuaConAboutDialog() }
    }

    override fun onResume() {
        super.onResume()
        val countBonus = RuaConUtil.getNumberPointFromInstallBonusApp(this)
        ToastUtil.show("Bonus: $countBonus")
    }

    private fun showListRuaConAppDialog() {
        val dialog = RuaConAppsDialog()
        dialog.show(supportFragmentManager, RuaConAppsDialog::class.java.name)
        dialog.setOnRuaConAppListener(object : RuaConAppsAdapter.OnRuaConAppListener {
            override fun onReceiveBonus() {
                ToastUtil.show("Receive bonus!")
            }
        })
    }

    private fun showRuaConAboutDialog() {
        MenuUtil.action(this, MenuAction.ABOUT)
    }
}