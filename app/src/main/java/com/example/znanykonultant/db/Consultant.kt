package com.example.znanykonultant.db

class Consultant(val phone : String, val city : String, val street : String, val houseNumber : String,
                 val description : String, val page : String, val consultantService : HashMap<Int, ConsultantService>) {}