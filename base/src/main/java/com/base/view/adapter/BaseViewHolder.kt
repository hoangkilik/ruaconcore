package com.base.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(t: T, position: Int, viewType: Int)

    protected fun isValidAdapterPosition(): Boolean {
        return adapterPosition >= 0
    }
}