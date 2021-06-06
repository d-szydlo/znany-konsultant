package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Opinion(
    var rating : Float = 0f,
    var title : String = "",
    var description : String = "",
    var consultantUid : String = "",
    var userId : String = ""
) {

}

