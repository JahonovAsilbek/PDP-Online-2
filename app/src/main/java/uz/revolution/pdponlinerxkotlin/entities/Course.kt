package uz.revolution.pdponlinerxkotlin.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "course")
class Course : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var courseName: String? = null

    var imagePath: String? = null

    constructor()

    @Ignore
    constructor(id: Int?, courseName: String?, imagePath: String?) {
        this.id = id
        this.courseName = courseName
        this.imagePath = imagePath
    }

    @Ignore
    constructor(courseName: String?, imagePath: String?) {
        this.courseName = courseName
        this.imagePath = imagePath
    }


}