package uz.revolution.pdponlinerxkotlin.entities

import androidx.room.Entity
import androidx.room.Ignore
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

    @Ignore
    constructor(
        id: Int?,
        moduleID: Int?,
        lessonName: String?,
        lessonInfo: String?,
        lessonLocation: Int?
    ) {
        this.id = id
        this.moduleID = moduleID
        this.lessonName = lessonName
        this.lessonInfo = lessonInfo
        this.lessonLocation = lessonLocation
    }

    @Ignore
    constructor(moduleID: Int?, lessonName: String?, lessonInfo: String?, lessonLocation: Int?) {
        this.moduleID = moduleID
        this.lessonName = lessonName
        this.lessonInfo = lessonInfo
        this.lessonLocation = lessonLocation
    }

    constructor()
}