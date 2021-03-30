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
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentEditModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module

private const val ARG_PARAM1 = "modul"
private const val ARG_PARAM2 = "kursID"

class EditModule : Fragment() {
    // TODO: Rename and change types of parameters
    private var module: Module? = null
    private var courseID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            module = it.getSerializable(ARG_PARAM1) as Module
            courseID = it.getInt(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentEditModuleBinding
    private var moduleList: ArrayList<Module>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditModuleBinding.inflate(layoutInflater, container, false)

        loadDataToView()
        loadData()
        editClick()


        return binding.root
    }

    private fun editClick() {
        binding.editModuleBtn.setOnClickListener {
            val name = binding.editModuleName.text.trim().toString()
            val location = binding.editModuleLocation.text.trim().toString()

            if (name.isNotEmpty() && location.isNotEmpty()) {

                if (checkName(name)) {

                    if (checkLocation(location.toInt())) {

                        val module =
                            Module(
                                module?.id,
                                courseID!!,
                                name,
                                module?.imagePath,
                                location.toInt()
                            )
                        Observable.fromCallable {
                            AppDatabase.get.getDatabase().getDao().updateModule(module)
                        }.subscribe()
                        findNavController().popBackStack()
                        Snackbar.make(
                            binding.root,
                            "Muvaffaqiyatli o'zgartirildi",
                            Snackbar.LENGTH_LONG
                        ).show()

                    } else {
                        Snackbar.make(
                            binding.root,
                            "$location o'rinli modul mavjud",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                } else {
                    Snackbar.make(binding.root, "$name nomli modul mavjud", Snackbar.LENGTH_LONG)
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
        moduleList = ArrayList()

        AppDatabase.get.getDatabase().getDao().getModulesByCourceID(courseID!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Module>> {
                override fun accept(t: List<Module>?) {
                    moduleList = t as ArrayList
                }

            })

    }

    private fun checkName(name: String): Boolean {
        var check = true

        for (i in 0 until moduleList!!.size) {
            if (name.equals(moduleList!![i].moduleName, true) && !name.equals(
                    module?.moduleName,
                    true
                )
            ) {
                check = false
                break
            }
        }
        return check
    }

    private fun checkLocation(location: Int): Boolean {
        var check = true

        for (i in 0 until moduleList!!.size) {
            if (location == moduleList!![i].location && location != module?.location) {
                check = false
                break
            }
        }
        return check
    }

    private fun loadDataToView() {
        binding.titleEditModuleFragment.text = module?.moduleName
        binding.editModuleName.setText(module?.moduleName)
        binding.editModuleLocation.setText(module?.location.toString())
    }

    companion object {

        @JvmStatic
        fun newInstance(module: String, courseID: String) =
            EditModule().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, module)
                    putString(ARG_PARAM2, courseID)
                }
            }
    }
}