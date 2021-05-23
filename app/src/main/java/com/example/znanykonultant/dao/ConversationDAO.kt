package com.example.znanykonultant.dao

import com.example.znanykonultant.entity.Members
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
}