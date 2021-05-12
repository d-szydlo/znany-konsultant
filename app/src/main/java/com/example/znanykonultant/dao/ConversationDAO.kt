package com.example.znanykonultant.dao

import com.example.znanykonultant.entity.Members
import com.example.znanykonultant.entity.Messages
import com.example.znanykonultant.entity.Rate
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

class ConversationDAO {

    val database = Firebase.database

    // chat class useless?

    fun addConversation(
        membersList : MutableList<String>,
    ): String {
        val membersRef = database.getReference("members")
        val pushedRef = membersRef.push()

        // generated key
        val conversationID = pushedRef.key.toString()

        pushedRef.setValue(
            Members(
                membersList
            ).toMap()
        )

        // ID stored in db
        return conversationID
    }

    fun updateConversation(
        conversationId : String,
        sender : String,
        message : String,
        timestamp: Timestamp
    ){
        val membersRef = database.getReference("messages").child(conversationId)
        membersRef.updateChildren(
            Messages(
                message,
                sender,
                timestamp
            ).toMap()
        )

    }
}