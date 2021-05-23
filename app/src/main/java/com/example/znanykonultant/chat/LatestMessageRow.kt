package com.example.znanykonultant.chat

import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.RecyclerLatestMessageItemBinding
import com.example.znanykonultant.entity.Messages
import com.xwray.groupie.viewbinding.BindableItem
import java.text.SimpleDateFormat
import java.util.*

class LatestMessageRow(private val message: Messages)
    : BindableItem<RecyclerLatestMessageItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.recycler_latest_message_item
    }

    override fun bind(viewBinding: RecyclerLatestMessageItemBinding, position: Int) {
        viewBinding.lastMessageText.text = message.text
        viewBinding.lastMessageSenderLogin.text = message.fromId
        viewBinding.lastMessageDate.text = getTimeFormatted(message.timestamp)
    }

    private fun getTimeFormatted(time: Long): String {
        var dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        if (isLessThanOneDayAgo(time))
            dateFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return dateFormatter.format(Date(time))
    }

    private fun isLessThanOneDayAgo(time: Long) =
        time + DAY_IN_MILLIS > System.currentTimeMillis()

    override fun initializeViewBinding(view: View): RecyclerLatestMessageItemBinding {
        return RecyclerLatestMessageItemBinding.bind(view)
    }

    companion object {
        const val DAY_IN_MILLIS = 86400000
    }
}
