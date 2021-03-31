package uz.revolution.pdponlinerxkotlin.lessons_frg

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
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
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentLessonBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Lesson
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.lessons_frg.adapters.LessonAdapter

private const val ARG_PARAM1 = "module"
private const val ARG_PARAM2 = "course"

class LessonsFragment : Fragment() {

    private var module: Module? = null
    private var course: Course? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            module = it.getSerializable(ARG_PARAM1) as Module?
            course = it.getSerializable(ARG_PARAM2) as Course?
        }
    }

    lateinit var binding: FragmentLessonBinding
    private var lessonList: ArrayList<Lesson>? = null
    private var adapter: LessonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonBinding.inflate(inflater, container, false)

        binding.toolbar.title = module?.moduleName
        binding.toolbar.navigationIcon = null
        loadData()
        saveClick()
        deleteClick()
        editClick()

        return binding.root
    }

    private fun editClick() {
        adapter?.onEditClick = object : LessonAdapter.OnEditClick {
            override fun onClick(lesson: Lesson) {
                val bundle = Bundle()
                bundle.putSerializable("lesson", lesson)
                bundle.putInt("moduleID", module?.id!!)
                findNavController().navigate(R.id.editLesson, bundle)
            }
        }
    }

    private fun deleteClick() {
        adapter?.onDeleteClick = object : LessonAdapter.OnDeleteClick {
            override fun onClick(lesson: Lesson) {
                val dialog = AlertDialog.Builder(binding.root.context)

                dialog.setMessage("Dars o’chishiga rozimisiz?")
                dialog.setPositiveButton("Ha", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Observable.fromCallable {
                            AppDatabase.get.getDatabase().getDao().deleteLesson(lesson)
                        }.subscribe()
                        p0?.cancel()
                        Snackbar.make(binding.root, "O'chirildi!", Snackbar.LENGTH_LONG).show()
                    }

                })
                dialog.setNegativeButton("Yo'q", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.cancel()
                    }
                })
                dialog.show()
            }
        }
    }

    private fun saveClick() {
        binding.addLessoneBtn.setOnClickListener {
            val name = binding.lessonNameEt.text.toString().trim()
            val info = binding.lessonInfoEt.text.toString().trim()
            val location = binding.lessonLocationEt.text.toString().trim()

            if (name.isNotEmpty() && info.isNotEmpty() && location.isNotEmpty()) {

                if (checkName(name)) {

                    if (checkLocation(location.toInt())) {
                        val lesson = Lesson(module?.id, name, info, location.toInt())

                        Observable.fromCallable {
                            AppDatabase.get.getDatabase().getDao().insertLesson(lesson)
                        }.subscribe()

                        Snackbar.make(
                            binding.root,
                            "Muvaffaqiyatli qo'shildi",
                            Snackbar.LENGTH_LONG
                        )
                            .show()

                        binding.lessonNameEt.setText("")
                        binding.lessonInfoEt.setText("")
                        binding.lessonLocationEt.setText("")

                    } else {
                        Snackbar.make(
                            binding.root,
                            "$location o'rinli dars mavjud!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                } else {
                    Snackbar.make(binding.root, "$name nomli dars mavjud!", Snackbar.LENGTH_LONG)
                        .show()
                }

            } else {
                Snackbar.make(binding.root, "Barcha maydonlarni to'ldiring!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun checkName(name: String): Boolean {
        var check = true

        for (i in 0 until lessonList!!.size) {
            if (name.equals(lessonList!![i].lessonName, true)) {
                check = false
                break
            }
        }
        return check
    }

    private fun checkLocation(location: Int): Boolean {
        var check = true

        for (i in 0 until lessonList!!.size) {
            if (location == lessonList!![i].lessonLocation) {
                check = false
                break
            }
        }
        return check
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        lessonList = ArrayList()
        adapter = LessonAdapter()

        AppDatabase.get.getDatabase().getDao().getLessonsByModuleID(module?.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Lesson>> {
                override fun accept(t: List<Lesson>?) {
                    lessonList = t as ArrayList
                    adapter?.submitList(t)
                    adapter?.setImage(course?.imagePath!!)
                }

            })

        binding.lessonsRv.adapter = adapter
    }
}