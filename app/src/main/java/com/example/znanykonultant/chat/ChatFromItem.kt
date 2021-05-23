package com.example.znanykonultant.chat

import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.RecyclerMessageFromItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class ChatFromItem(private val text: String) : BindableItem<RecyclerMessageFromItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.recycler_message_from_item
    }

    override fun bind(viewBinding: RecyclerMessageFromItemBinding, position: Int) {
        viewBinding.messageFrom.text = text
    }

    override fun initializeViewBinding(view: View): RecyclerMessageFromItemBinding {
        return RecyclerMessageFromItemBinding.bind(view)
    }
}