package com.example.znanykonultant


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class SpinnerAdapter(val context: Context, var data: Array<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private class ItemHolder(row: View?) {
        val label: TextView
        init {
            label = row?.findViewById(R.id.mainSpinnerItem) as TextView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }

        vh.label.text = data[position]
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }


}
