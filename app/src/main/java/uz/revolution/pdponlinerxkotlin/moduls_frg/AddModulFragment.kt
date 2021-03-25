package uz.revolution.pdponlinerxkotlin.moduls_frg

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentAddModulBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.moduls_frg.adapters.ModulsAdapter
import java.util.function.Consumer

private const val ARG_PARAM1 = "course"
private const val ARG_PARAM2 = "param2"

class AddModulFragment : Fragment() {
    private var param1: Course? = null
    private var param2: String? = null
    private var binding: FragmentAddModulBinding? = null
    private var modulList: ArrayList<Module>? = null
    private var adapter: ModulsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Course
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddModulBinding.inflate(inflater, container, false)

        setToolbar()
        loadData()
        binding!!.modulsRv.layoutManager = LinearLayoutManager(context)
        binding!!.modulsRv.adapter = adapter

        addBtnClick()

        return binding!!.root
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        modulList = ArrayList()
        AppDatabase.get.getDatabase().getDao().getModulesByCourceID(param1!!.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                @RequiresApi(Build.VERSION_CODES.N)
                object : Consumer<List<Module>>, io.reactivex.functions.Consumer<List<Module>> {
                    override fun accept(module_db: List<Module>) {
                        modulList = module_db as ArrayList<Module>
                        adapter = ModulsAdapter(module_db,param1!!.imagePath.toString())
                    }
                },
                @RequiresApi(Build.VERSION_CODES.N)
                object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {

                    override fun accept(p0: Throwable) {

                    }

                }, object : Action {

                    override fun run() {

                    }
                }
            )
    }

    private fun addBtnClick() {
        binding!!.addModuleBtn.setOnClickListener {
            val name = binding!!.modulNameEt.text.toString().trim()
            val location = binding!!.modulLocationEt.text.toString().trim()
            if (name != "" && location != "") {
                val newModul= Module(param1!!.id,name,param1!!.imagePath,location.toInt())
                AppDatabase.get.getDatabase().getDao().insertModule(newModul)
                modulList!!.add(newModul)
                adapter!!.addElement(newModul)
            } else {
                Snackbar.make(
                    binding!!.addModuleBtn,
                    "Barcha maydonlarni to'ldiring!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setToolbar() {
        binding!!.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: Course, param2: String) =
            AddModulFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}