package uz.revolution.pdponlinerxkotlin.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "lesson")
class Lesson : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int?=null

    var moduleID:Int?=null

    var lessonName:String?=null

    var lessonInfo:String?=null

    var lessonLocation:Int?=null
}