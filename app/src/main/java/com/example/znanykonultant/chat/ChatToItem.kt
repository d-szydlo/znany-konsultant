package com.example.znanykonultant.chat

import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.RecyclerMessageToItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class ChatToItem(private val text: String) : BindableItem<RecyclerMessageToItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.recycler_message_to_item
    }

    override fun bind(viewBinding: RecyclerMessageToItemBinding, position: Int) {
        viewBinding.messageTo.text = text
    }

    override fun initializeViewBinding(view: View): RecyclerMessageToItemBinding {
        return RecyclerMessageToItemBinding.bind(view)
    }
}