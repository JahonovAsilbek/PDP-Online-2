package uz.revolution.pdponlinerxkotlin.lessons_frg

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentAddLessonBinding
import uz.revolution.pdponlinerxkotlin.entities.Lesson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "lesson"
private const val ARG_PARAM2 = "moduleID"

class AddLessonFragment : Fragment() {

    private var lesson: Lesson? = null
    private var moduleID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson = it.getSerializable(ARG_PARAM1) as Lesson?
            moduleID = it.getInt(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentAddLessonBinding
    private var lessonList: ArrayList<Lesson>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLessonBinding.inflate(layoutInflater, container, false)

        loadDataToView()
        loadData()
        editClick()


        return binding.root
    }

    private fun editClick() {
        binding.editAddLessoneBtn.setOnClickListener {
            val name = binding.editLessonNameEt.text.toString().trim()
            val info = binding.editLessonInfoEt.text.toString().trim()
            val location = binding.editLessonLocationEt.text.toString().trim()

            if (name.isNotEmpty() && info.isNotEmpty() && location.isNotEmpty()) {

                if (checkName(name)) {

                    if (checkLocation(location.toInt())) {

                        val lesson = Lesson(lesson?.id, moduleID, name, info, location.toInt())
                        Observable.fromCallable {
                            AppDatabase.get.getDatabase().getDao().updateLesson(lesson)
                        }.subscribe()

                        findNavController().popBackStack()

                        Snackbar.make(
                            binding.root,
                            "Muvaffaqiyatli o'zgartirildi!",
                            Snackbar.LENGTH_LONG
                        ).show()

                    } else {
                        Snackbar.make(
                            binding.root,
                            "$location o'rinli dars mavjud",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                } else {
                    Snackbar.make(binding.root, "$name nomli dars mavjud", Snackbar.LENGTH_LONG)
                        .show()
                }

            } else {
                Snackbar.make(binding.root, "Barcha maydonlarni to'ldiring!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun checkLocation(location: Int): Boolean {
        var check = true

        for (i in 0 until lessonList!!.size) {
            if (location == lessonList!![i].lessonLocation && location != lesson?.lessonLocation) {
                check = false
                break
            }
        }
        return check
    }

    private fun checkName(name: String): Boolean {
        var check = true

        for (i in 0 until lessonList!!.size) {
            if (name.equals(lessonList!![i].lessonName, true) && !name.equals(
                    lesson?.lessonName,
                    true
                )
            ) {
                check = false
                break
            }
        }
        return check
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        lessonList = ArrayList()

        AppDatabase.get.getDatabase().getDao().getLessonsByModuleID(moduleID!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Lesson>> {
                override fun accept(t: List<Lesson>?) {
                    lessonList = t as ArrayList
                }
            })
    }


    private fun loadDataToView() {
        binding.editToolbar.title = "${lesson?.lessonLocation}-dars"
        binding.editToolbar.navigationIcon = null
        binding.editLessonNameEt.setText(lesson?.lessonName)
        binding.editLessonInfoEt.setText(lesson?.lessonInfo)
        binding.editLessonLocationEt.setText(lesson?.lessonLocation.toString())
    }


}