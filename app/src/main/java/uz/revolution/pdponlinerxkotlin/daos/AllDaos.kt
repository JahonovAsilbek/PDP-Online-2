package uz.revolution.pdponlinerxkotlin.daos

import androidx.room.*
import io.reactivex.Flowable
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Lesson
import uz.revolution.pdponlinerxkotlin.entities.Module

@Dao
interface AllDaos {

    //  methods for courses

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: Course)

    @Query("select * from course")
    fun getAllCourse(): Flowable<List<Course>>

    @Update
    fun updateCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)


    //  mehtods for modules

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModule(module: Module)

    @Query("select * from module")
    fun getAllModules(): Flowable<List<Module>>

    @Query("select * from module where courseID=:courseID")
    fun getModulesByCourceID(courseID: Int): Flowable<List<Module>>

    @Update
    fun updateModule(module: Module)

    @Delete
    fun deleteModule(module: Module)


    //  methods for lessons

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLesson(lesson: Lesson)

    @Query("select * from lesson")
    fun getAllLessons(): Flowable<List<Lesson>>

    @Query("select * from lesson where moduleID=:moduleID")
    fun getLessonsByModuleID(moduleID: Int): Flowable<List<Lesson>>

    @Update
    fun updateLesson(lesson: Lesson)

    @Delete
    fun deleteLesson(lesson: Lesson)
}