package uz.revolution.pdponlinerxkotlin.moduls_frg

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
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentAddModulBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.moduls_frg.adapters.ModulsAdapter

private const val ARG_PARAM1 = "course"
private const val ARG_PARAM2 = "param2"

class AddModulFragment : Fragment() {
    private var course: Course? = null
    private var param2: String? = null
    lateinit var binding: FragmentAddModulBinding
    private var modulList: ArrayList<Module>? = null
    private var adapter: ModulsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable(ARG_PARAM1) as Course
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = ModulsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddModulBinding.inflate(inflater, container, false)

        setToolbar()
        loadData()

        addBtnClick()

        return binding.root
    }

    private fun checkModuleLocation(location: Int): Boolean {
        var check = false
        for (i in 0 until modulList!!.size) {
            if (location == modulList!![i].location) {
                check = true
                break
            }
        }
        return check
    }

    private fun checkModuleName(moduleName: String): Boolean {
        var check = false

        for (i in 0 until modulList!!.size) {
            if (modulList!![i].moduleName.equals(moduleName, true)) {
                check = true
                break
            }
        }
        return check
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        modulList = ArrayList()
        AppDatabase.get.getDatabase().getDao().getModulesByCourceID(course!!.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : io.reactivex.functions.Consumer<List<Module>> {
                    override fun accept(module_db: List<Module>) {
                        modulList = module_db as ArrayList<Module>
                        adapter?.submitList(module_db)
                        adapter?.setModuleImage(course!!.imagePath.toString())
                    }
                },
                object : io.reactivex.functions.Consumer<Throwable> {

                    override fun accept(p0: Throwable) {

                    }

                }, object : Action {

                    override fun run() {

                    }
                }
            )

        binding.modulsRv.adapter = adapter
    }

    private fun addBtnClick() {
        binding.addModuleBtn.setOnClickListener {
            val name = binding.modulNameEt.text.toString().trim()
            val location = binding.modulLocationEt.text.toString().trim()
            if (name != "" && location != "") {

                if (!checkModuleName(name)) {

                    if (!checkModuleLocation(location.toInt())) {
                        val newModul =
                            Module(course!!.id, name, course!!.imagePath, location.toInt())
                        modulList!!.add(newModul)
                        Observable.fromCallable {
                            AppDatabase.get.getDatabase().getDao().insertModule(newModul)
                        }.subscribe()
                        Snackbar.make(
                            binding.root,
                            "Muvaffaqiyatli qo'shildi!",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                        binding.modulNameEt.setText("")
                        binding.modulLocationEt.setText("")
                    } else {
                        Snackbar.make(
                            binding.root,
                            "$location o'rinli modul mavjud!",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }


                } else {
                    Snackbar.make(binding.root, "$name nomli modul mavjud!", Snackbar.LENGTH_LONG)
                        .show()
                }


            } else {
                Snackbar.make(
                    binding.addModuleBtn,
                    "Barcha maydonlarni to'ldiring!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.title = course?.courseName
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(course: Course, param2: String) =
            AddModulFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, course)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}