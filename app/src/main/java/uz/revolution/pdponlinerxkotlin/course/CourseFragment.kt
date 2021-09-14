package uz.revolution.pdponlinerxkotlin.course

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.course.adapters.CourseAdapter
import uz.revolution.pdponlinerxkotlin.daos.AllDaos
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentCourseBinding
import uz.revolution.pdponlinerxkotlin.entities.Lesson
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.models.CourseModel

// comes courseID from HomeFragment
private const val ARG_PARAM1 = "courseID"
private const val ARG_PARAM2 = "courseName"
private const val ARG_PARAM3 = "courseImagePath"

class CourseFragment : Fragment() {

    var courseID: Int = 0
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
    lateinit var getDao: AllDaos
    lateinit var data: ArrayList<CourseModel>

    private var adapter: CourseAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentCourseBinding.inflate(LayoutInflater.from(container?.context), container, false)
        binding.titleCourseFragment.text = courseName

        loadData()
        itemClick()

        return binding.root
    }

    private fun itemClick() {
        adapter?.onMoreClick = object : CourseAdapter.OnMoreClick {
            override fun onClick(module: Module) {
                val bundle = Bundle()
                bundle.putSerializable("module", module)
                findNavController().navigate(R.id.lessonFragment, bundle)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        data = ArrayList()

        for (i in getDao.getModulesByCourceIDs(courseID).indices) {
            val moduleID = getDao.getModulesByCourceIDs(courseID)[i].id
            val lessonList = getDao.getLessonsByModuleIDs(moduleID!!) as ArrayList
            data.add(CourseModel(getDao.getModulesByCourceIDs(courseID)[i], lessonList.size))
        }

        adapter?.setAdapter(imagePath!!, courseName!!, data)
        binding.courseRv.adapter = adapter
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