package uz.revolution.pdponlinerxkotlin.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentEditCourseBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import java.io.File
import java.io.FileOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "kurs"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditCourse.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditCourse : Fragment() {
    // TODO: Rename and change types of parameters
    private var course: Course? = null
    private var param2: String? = null
    var requestCOde = 1
    private val TAG = "AAAA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable(ARG_PARAM1) as Course
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentEditCourseBinding
    private var courseList: ArrayList<Course>? = null
    private var uri: Uri? = null
    private var absolutePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCourseBinding.inflate(layoutInflater, container, false)

        loadDataToView()
        loadData()
        imageClick()
        editClick()



        return binding.root
    }

    private fun imageClick() {
        binding.editCourseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, requestCOde)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCOde && resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            binding.editCourseImage.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            val file = File(course?.imagePath)
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            absolutePath = file.absolutePath
            Log.d("AAAA", "onActivityResult: $absolutePath")
        }
    }

    private fun editClick() {
        binding.editCourseBtn.setOnClickListener {
            val courseName = binding.editCourseEt.text.toString().trim()
            val course = Course(course!!.id, courseName, absolutePath)

            if (courseName.isNotEmpty() && absolutePath != null) {

                if (checkCourseName(courseName)) {
                    Observable.fromCallable {
                        AppDatabase.get.getDatabase().getDao().updateCourse(course)
                    }.subscribe()

                    Snackbar.make(
                        binding.root,
                        "Muvaffaqiyatli o'zgartirildi!",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                    findNavController().popBackStack()
                } else {
                    Snackbar.make(binding.root, "$courseName nomli kurs mavjud", Snackbar.LENGTH_LONG)
                        .show()
                }


            } else {
                Snackbar.make(binding.root, "Barcha maydonlarni to'ldiring!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        courseList = ArrayList()

        AppDatabase.get.getDatabase().getDao().getAllCourse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Course>> {
                override fun accept(t: List<Course>?) {
                    courseList = t as ArrayList
                }

            }, object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {

                }

            }, object : Action {
                override fun run() {

                }

            })

    }

    private fun loadDataToView() {
        absolutePath = course?.imagePath
        binding.titleEditCourseFragment.text = course?.courseName
        binding.editCourseImage.setImageURI(Uri.parse(course?.imagePath))
        binding.editCourseEt.setText(course?.courseName)
    }

    @SuppressLint("CheckResult")
    private fun checkCourseName(courseName: String): Boolean {
        var check = true

        for (i in 0 until courseList!!.size) {
            if (courseName.equals(
                    courseList!![i].courseName,
                    true
                ) && !courseName.equals(course?.courseName, true)
            ) {
                check = false
                break
            }
        }

        return check

    }

    companion object {
        @JvmStatic
        fun newInstance(course: String, param2: String) =
            EditCourse().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, course)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}