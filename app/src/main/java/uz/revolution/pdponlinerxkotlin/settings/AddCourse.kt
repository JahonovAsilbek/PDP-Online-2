package uz.revolution.pdponlinerxkotlin.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import com.github.florent37.runtimepermission.kotlin.askPermission
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddCourse : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
//        binding = FragmentAddCourseBinding.inflate(layoutInflater, container, false)
        binding = FragmentAddCourseBinding.inflate(
            LayoutInflater.from(container?.context),
            container,
            false
        )

        loadData()
        imageClick()
        saveClick()

        return binding.root
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

        getDao!!.getAllCourse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                @RequiresApi(Build.VERSION_CODES.N)
                object : Consumer<List<Course>>, io.reactivex.functions.Consumer<List<Course>> {
                    override fun accept(dataCourse: List<Course>) {
                        adapter?.submitList(dataCourse)
                        courseList = dataCourse as ArrayList
                    }
                },
                @RequiresApi(Build.VERSION_CODES.N)
                object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {

                    override fun accept(p0: Throwable) {

                    }

                }, object : Action {

                    override fun run() {

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

    @SuppressLint("CheckResult")
    private fun getPreviousCourseID(): Int {
        var previousID = 0
        getDao!!.getAllCourse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@RequiresApi(Build.VERSION_CODES.N)
            object : Consumer<List<Course>>,
                io.reactivex.functions.Consumer<List<Course>> {
                override fun accept(t: List<Course>) {
                    if (t.isEmpty()) {
                        previousID = 0
                    } else {
                        previousID = t[t.size - 1].id!!
                    }
                }

            },
                @RequiresApi(Build.VERSION_CODES.N)
                object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {
                    override fun accept(p0: Throwable) {

                    }

                }, object : Action {
                    override fun run() {

                    }

                })

        return previousID
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCOde && resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            binding.addCourseImage.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            val file = File(requireActivity().filesDir, "image${getPreviousCourseID()}.jpg")
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
//                appendText(resultView, "ForeverDenied :")
                //the list of forever denied permissions, user has check 'never ask again'
//                e.foreverDenied.forEach {
//                    appendText(resultView, it)
//                }
                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCourse().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}