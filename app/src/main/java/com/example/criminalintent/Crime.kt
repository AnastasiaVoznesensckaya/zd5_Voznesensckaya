package com.example.criminalintent

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Crime(@PrimaryKey var id: UUID = UUID.randomUUID())
{
     var requiresPolice :Boolean = false
    var title:String = ""
    var date: Date? = null
    var isSolved: Boolean = false
    constructor(id:UUID, title:String, date:Date, isSolved:Boolean) : this() {
        this.id = id
        this.title = title
        this.date = date
        this.isSolved = isSolved
    }
}
