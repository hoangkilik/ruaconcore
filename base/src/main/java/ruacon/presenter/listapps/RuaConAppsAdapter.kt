package ruacon.presenter.listapps

import android.view.View
import com.base.R
import com.base.util.AppUtil
import com.base.view.adapter.BaseAdapter
import com.base.view.adapter.BaseViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row_rua_con_app.view.*
import ruacon.entity.RuaConApp
import ruacon.util.RuaConUtil

/**
 * Created by Nguyen Tien HOANG on 2018-12-20.
 */
class RuaConAppsAdapter : BaseAdapter<RuaConApp>() {

    private var mOnRuaConAppListener: OnRuaConAppListener? = null

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_row_rua_con_app
    }

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<RuaConApp> {
        return RuaConAppViewHolder(view)
    }

    fun setOnRuaConAppListener(listener: OnRuaConAppListener) {
        mOnRuaConAppListener = listener
    }

    inner class RuaConAppViewHolder(itemView: View) : BaseViewHolder<RuaConApp>(itemView) {

        init {
            itemView.btnInstall.setOnClickListener {
                if (isValidAdapterPosition()) {
                    RuaConUtil.addPendingInstall(itemView.context, mListItems[adapterPosition].packageName)
                    AppUtil.openAppInPlayStore(itemView.context, mListItems[adapterPosition].packageName)
                }
            }

            itemView.btnOpen.setOnClickListener {
                if (isValidAdapterPosition()) {
                    AppUtil.openApp(itemView.context, mListItems[adapterPosition].packageName)
                }
            }

            itemView.btnReceiveBonus.setOnClickListener {
                if (isValidAdapterPosition()) {
                    RuaConUtil.addReceiveBonus(itemView.context, mListItems[adapterPosition].packageName)
                    mListItems[adapterPosition].receiveBonus()
                    notifyItemChanged(adapterPosition)
                    mOnRuaConAppListener?.onReceiveBonus()
                }
            }
        }

        override fun bindData(t: RuaConApp, position: Int, viewType: Int) {
            itemView.tvAppName.text = t.name
            Glide.with(itemView).load(t.icon).into(itemView.ivIconApp)
            val isInstalled = t.isInstall(itemView.context)
            val isReceivedBonus = t.isReceivedBonus(itemView.context)

            if (isReceivedBonus) {
                itemView.tvStatus.setText(R.string.message_received_bonus_this_app)
                if (isInstalled) {
                    showHideHideView(itemView.btnOpen, itemView.btnInstall, itemView.btnReceiveBonus)
                } else {
                    showHideHideView(itemView.btnInstall, itemView.btnOpen, itemView.btnReceiveBonus)
                }
            } else {
                if (isInstalled) {
                    itemView.tvStatus.setText(R.string.message_not_received_bonus_this_app_but_installed)
                    showHideHideView(itemView.btnReceiveBonus, itemView.btnOpen, itemView.btnInstall)
                } else {
                    itemView.tvStatus.setText(R.string.message_not_received_bonus_this_app)
                    showHideHideView(itemView.btnInstall, itemView.btnOpen, itemView.btnReceiveBonus)
                }
            }
        }

        private fun showHideHideView(viewShow: View, hideView1: View, hideView2: View) {
            viewShow.visibility = View.VISIBLE
            hideView1.visibility = View.GONE
            hideView2.visibility = View.GONE
        }
    }

    interface OnRuaConAppListener {
        fun onReceiveBonus()
    }
}