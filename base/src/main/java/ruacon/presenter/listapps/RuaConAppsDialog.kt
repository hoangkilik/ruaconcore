package ruacon.presenter.listapps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.R
import kotlinx.android.synthetic.main.dialog_rua_con_apps.*
import ruacon.api.RuaConApi
import ruacon.entity.RuaConApp

/**
 * Created by on 2018-12-20.
 */
class RuaConAppsDialog : DialogFragment() {

    private lateinit var mAdapter: RuaConAppsAdapter
    private lateinit var mOnRuaConAppListener: RuaConAppsAdapter.OnRuaConAppListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_rua_con_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        callApiGetListApp()
    }

    private fun initView() {
        mAdapter = RuaConAppsAdapter()
        mAdapter.setOnRuaConAppListener(mOnRuaConAppListener)
        rvRuaConApps.adapter = mAdapter
        rvRuaConApps.layoutManager = LinearLayoutManager(context)
        swipeRefresh.setColorSchemeResources(R.color.md_green_600, R.color.md_orange_600, R.color.md_blue_600)
        swipeRefresh.setOnRefreshListener { callApiGetListApp() }
    }

    private fun callApiGetListApp() {
        swipeRefresh.isRefreshing = true
        RuaConApi.getListApp(object : RuaConApi.ICallback<ArrayList<RuaConApp>> {
            override fun onSuccess(t: ArrayList<RuaConApp>) {
                swipeRefresh.isRefreshing = false
                mAdapter.setListItem(t)
            }

            override fun onError() {
                swipeRefresh.isRefreshing = false
            }
        })
    }

    fun setOnRuaConAppListener(listener: RuaConAppsAdapter.OnRuaConAppListener) {
        mOnRuaConAppListener = listener
    }
}