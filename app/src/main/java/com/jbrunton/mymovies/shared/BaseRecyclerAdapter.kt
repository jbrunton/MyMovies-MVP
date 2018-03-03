package com.jbrunton.mymovies.shared

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseRecyclerAdapter<D, H : RecyclerView.ViewHolder>(
        @field:LayoutRes private val layout: Int,
        private val viewHolderFactory: ViewHolderFactory<D, H>
) : RecyclerView.Adapter<H>() {
    private var dataSource: List<D> = ArrayList()

    private val clickListener = View.OnClickListener { v ->
        val item = v.tag as D
        onItemClicked(item)
    }

    fun setDataSource(items: Collection<D>) {
        this.dataSource = ArrayList(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): H {
        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)

        view.setOnClickListener(clickListener)

        return viewHolderFactory.createViewHolder(view)
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        val item = dataSource[position]
        holder.itemView.tag = item
        viewHolderFactory.bindHolder(holder, item)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    protected fun onItemClicked(item: D) {}

    interface ViewHolderFactory<D, H : RecyclerView.ViewHolder> {
        fun createViewHolder(view: View): H
        fun bindHolder(holder: H, item: D)
    }
}
