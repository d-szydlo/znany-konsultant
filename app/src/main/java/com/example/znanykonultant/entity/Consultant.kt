package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Consultant(
                var uid: String = "",
                var name: String = "",
                var surname: String = "",
                var email: String = "",
                var picture: String = "",
                var phone : String = "",
                var city : String = "",
                var street : String = "",
                var houseNumber : String = "",
                var description : String = "",
                var page : String = "",
                var averageRating : Float = 0f,
                var worktime : Map<String, WorkDays> = mapOf(),
                var category: Map<String, Boolean> = mapOf(),
                var rating : Map<Int, Boolean> = mapOf(),
                var consultantService : Map<String, ConsultantService> = mapOf(),
                var appointments: Map<String, Boolean> = mapOf(),
                var favourites:  Map<String, Favourite> = mapOf(),
) : Human() {
    override fun getFullName() = "$name $surname"
    fun getAllServiceNames(): MutableList<String> {
        val items = mutableListOf<String>()
        for(service in consultantService){
            items.add(service.value.description)
        }
        return items

    }

}
