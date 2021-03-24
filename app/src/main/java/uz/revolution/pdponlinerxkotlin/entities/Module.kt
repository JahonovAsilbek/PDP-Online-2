package uz.revolution.pdponlinerxkotlin.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "module")
class Module : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var courseID: Int? = null

    var moduleName: String? = null

    var imagePath: String? = null

    var location: Int? = null

    @Ignore
    constructor(id: Int?, courseID: Int?, moduleName: String?, imagePath: String?, location: Int?) {
        this.id = id
        this.courseID = courseID
        this.moduleName = moduleName
        this.imagePath = imagePath
        this.location = location
    }

    @Ignore
    constructor(courseID: Int?, moduleName: String?, imagePath: String?, location: Int?) {
        this.courseID = courseID
        this.moduleName = moduleName
        this.imagePath = imagePath
        this.location = location
    }

    constructor()
}