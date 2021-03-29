package uz.revolution.pdponlinerxkotlin.course

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.course.adapters.CourseAdapter
import uz.revolution.pdponlinerxkotlin.daos.AllDaos
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentCourseBinding
import uz.revolution.pdponlinerxkotlin.entities.Lesson
import uz.revolution.pdponlinerxkotlin.entities.Module

// comes courseID from HomeFragment
private const val ARG_PARAM1 = "courseID"
private const val ARG_PARAM2 = "courseName"
private const val ARG_PARAM3 = "courseImagePath"

class CourseFragment : Fragment() {

    private var courseID: Int? = null
    private var courseName: String? = null
    private var imagePath: String? = null
    private val TAG = "AAAA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseID = it.getInt(ARG_PARAM1)
            courseName = it.getString(ARG_PARAM2)
            imagePath = it.getString(ARG_PARAM3)
        }
        getDao = AppDatabase.get.getDatabase().getDao()
        adapter = CourseAdapter()
    }

    lateinit var binding: FragmentCourseBinding
    private var getDao: AllDaos? = null

    private var moduleList: ArrayList<Module>? = null
    private var lessonCountList: ArrayList<Int>? = null

    private var adapter: CourseAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentCourseBinding.inflate(LayoutInflater.from(container?.context), container, false)
        binding.titleCourseFragment.text = courseName

        loadData()

        return binding.root
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        moduleList = ArrayList()

        getDao!!.getModulesByCourceID(courseID!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : io.reactivex.functions.Consumer<List<Module>> {
                    override fun accept(t: List<Module>) {
                        // adapterga set qilish kerak shu kelgan listni malumotini

                        moduleList = t as ArrayList

                        adapter?.submitList(t)

                        Log.d(TAG, "kursID: ${courseID}")
                        Log.d(TAG, "module size: ${t.size}")

                        adapter?.setAdapter(imagePath!!, courseName!!, getLessonCountList(t))
                    }
                },
                object : io.reactivex.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }
                }, object : Action {
                    override fun run() {

                    }

                })

        binding.courseRv.adapter = adapter

    }

    @SuppressLint("CheckResult")
    private fun getLessonCountList(moduleList: List<Module>): ArrayList<Int> {
        lessonCountList = ArrayList()
        for (i in 0 until moduleList.size) {

            getDao!!.getLessonsByModuleID(moduleList[i].id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<List<Lesson>> {
                    override fun accept(lessonList: List<Lesson>?) {
                        lessonCountList?.add(lessonList!!.size)
                        adapter?.setAdapter(
                            imagePath!!,
                            courseName!!, lessonCountList!!
                        )
                    }
                }, object : Consumer<Throwable> {
                    override fun accept(t: Throwable?) {

                    }

                }, object : Action {
                    override fun run() {

                    }

                })
        }
        return lessonCountList!!
    }

    companion object {

        @JvmStatic
        fun newInstance(courseID: String, courseName: String) =
            CourseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, courseID)
                    putString(ARG_PARAM2, courseName)
                }
            }
    }
}