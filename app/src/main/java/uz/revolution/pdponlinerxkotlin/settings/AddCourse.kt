package uz.revolution.pdponlinerxkotlin.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.daos.AllDaos
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentAddCourseBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.settings.adapters.AddCourseAdapter
import java.io.File
import java.io.FileOutputStream
import java.util.function.Consumer

class AddCourse : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDao = AppDatabase.get.getDatabase().getDao()
    }

    lateinit var binding: FragmentAddCourseBinding
    private var uri: Uri? = null
    private var absolutePath: String? = null
    private var getDao: AllDaos? = null
    private var requestCOde = 1
    private var courseList: ArrayList<Course>? = null
    private var adapter: AddCourseAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCourseBinding.inflate(layoutInflater, null, false)
        loadData()
        imageClick()
        saveClick()
        deleteClick()
        editClick()

        return binding.root
    }

    private fun editClick() {
        adapter?.onEditClick = object : AddCourseAdapter.OnEditClick {
            override fun onClick(course: Course) {
                val bundle = Bundle()
                bundle.putSerializable("kurs", course)
                findNavController().navigate(R.id.editCourse, bundle)
            }
        }
    }

    private fun deleteClick() {
        adapter?.onDeleteClick = object : AddCourseAdapter.OnDeleteClick {
            override fun onClick(course: Course) {

                if (!checkSize(course)) {
                    val dialog = AlertDialog.Builder(binding.root.context)
                    dialog.setMessage("Bu kurs ichida modullar kiritilgan. Modullar bilan birgalikda o’chib ketishiga rozimisiz? ")
                    dialog.setPositiveButton("Ha"
                    ) { p0, p1 -> // delete course with its all modules
                        Observable.fromCallable {
                            getDao?.deleteCourse(course)
                        }.subscribe()
                        p0?.cancel()
                        Snackbar.make(binding.root, "O'chirildi", Snackbar.LENGTH_LONG).show()
                    }
                    dialog.setNegativeButton("Yo'q", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.cancel()
                        }
                    })
                    dialog.show()
                } else {

                    Observable.fromCallable {
                        getDao?.deleteCourse(course)
                    }.subscribe()
                    Snackbar.make(binding.root, "O'chirildi", Snackbar.LENGTH_LONG).show()

                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun checkSize(course: Course): Boolean {
        var check = true

        if (getDao?.getCourseSize(course.id!!)!! > 0) {
            check = false
        }
        return check
    }

    private fun checkCourseName(courseName: String): Boolean {
        var check = false
        for (i in 0 until courseList!!.size) {
            if (courseList!![i].courseName.equals(courseName, true)) {
                check = true
                break
            }
        }
        return check
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        adapter = AddCourseAdapter()
        courseList = ArrayList()

        //course item click(not edit or delete btn)     by Olimjon
        adapter!!.selfClickobject = object : AddCourseAdapter.SelfOnCLick {
            override fun onSelfClick(course: Course) {
                val bundle = Bundle()
                bundle.putSerializable("course", course)
                findNavController().navigate(R.id.addModulFragment, bundle)
            }
        }

        getDao!!.getAllCourses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                @RequiresApi(Build.VERSION_CODES.N)
                object : Consumer<List<Course>>, io.reactivex.functions.Consumer<List<Course>> {
                    override fun accept(dataCourse: List<Course>) {
                        adapter?.submitList(dataCourse)
                        courseList = dataCourse as ArrayList
                    }
                })

        binding.courseRv.adapter = adapter

    }

    private fun saveClick() {
        binding.addCourseBtn.setOnClickListener {

            val courseName = binding.addCourseEt.text.toString()
            val imagePath = absolutePath

            if (imagePath != null && courseName.isNotEmpty()) {

                if (!checkCourseName(courseName)) {
                    Observable.fromCallable {
                        getDao?.insertCourse(Course(courseName, imagePath))
                    }.subscribe()
                    binding.addCourseImage.setImageResource(R.drawable.ic_baseline_image_24)
                    binding.addCourseEt.setText("")
                    absolutePath = null
                    Toast.makeText(
                        binding.root.context,
                        "Muvaffaqiyatli qo'shildi!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        binding.root.context,
                        "$courseName nomli kurs mavjud",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
//            Snackbar.make(binding.root, "Barcha maydonlarni to'ldiring!", Snackbar.LENGTH_LONG).show()
                Toast.makeText(
                    binding.root.context,
                    "Barcha maydonlarni to'ldiring!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun imageClick() {
        binding.addCourseImage.setOnClickListener {
            setPermission()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCOde && resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            binding.addCourseImage.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            var file: File? = null
            if (courseList!!.isEmpty()) {
                file = File(
                    requireActivity().filesDir,
                    "image0.jpg"
                )
            } else {
                file = File(
                    requireActivity().filesDir,
                    "image${courseList!![courseList!!.size - 1].id}.jpg"
                )
            }
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            absolutePath = file.absolutePath
            Log.d("AAAA", "onActivityResult: $absolutePath")
        }
    }

    private fun setPermission() {

        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            //all permissions already granted or just granted

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, requestCOde)

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(binding.root.context)
                    .setMessage("Qurilma xotirasiga ruxsat zarur")
                    .setPositiveButton("Ruxsat berish") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("Bekor qilish") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                e.goToSettings();
            }
        }
    }
}