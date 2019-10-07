package com.base.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections
import kotlin.collections.ArrayList

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    companion object {
        const val VIEW_ITEM = -1
        const val VIEW_PROGRESS = -2
        const val VIEW_HEADER = -3
    }

    protected var mListItems: ArrayList<T> = ArrayList()

    protected abstract fun getLayoutId(viewType: Int): Int
    protected abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<T>

    fun getListItems(): ArrayList<T> {
        return mListItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindData(mListItems[position], position, getItemViewType(position))
    }

    override fun getItemCount(): Int {
        return mListItems.size
    }

    fun clear() {
        mListItems.clear()
        notifyDataSetChanged()
    }

    fun setListItem(list: List<T>) {
        mListItems.clear()
        mListItems.addAll(list)
        notifyDataSetChanged()
    }

    fun addListItem(list: List<T>?) {
        if (list != null && list.isNotEmpty()) {
            val oldSize = itemCount
            this.mListItems.addAll(list)
            val newSize = itemCount
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        }
    }

    fun addListItem(array: Array<T>?) {
        if (array != null && array.isNotEmpty()) {
            val oldSize = itemCount
            this.mListItems.addAll(array)
            val newSize = itemCount
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        }
    }

    operator fun get(position: Int): T {
        return mListItems[position]
    }

    fun add(element: T) {
        mListItems.add(element)
    }

    fun addAndNotify(element: T) {
        mListItems.add(element)
        notifyItemInserted(itemCount - 1)
    }

    fun add(index: Int, element: T) {
        mListItems.add(index, element)
    }

    fun removeAt(index: Int): T {
        return mListItems.removeAt(index)
    }

    fun removeAndNotify(index: Int): T {
        val t = mListItems.removeAt(index)
        notifyItemRemoved(index)
        return t
    }

    fun sort(c: Comparator<in T>) {
        Collections.sort(mListItems, c)
    }
}