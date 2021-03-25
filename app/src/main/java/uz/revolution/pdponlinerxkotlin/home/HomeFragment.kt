package uz.revolution.pdponlinerxkotlin.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.daos.AllDaos
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentHomeBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.home.adapters.GeneralAdapter
import uz.revolution.pdponlinerxkotlin.home.adapters.ItemHomeAdapter
import uz.revolution.pdponlinerxkotlin.models.GeneralData


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    private val TAG = "AAAA"

    lateinit var binding: FragmentHomeBinding
    private var data: ArrayList<GeneralData>? = null

    private var generalAdapter: GeneralAdapter? = null
    private var itemHomeAdapter: ItemHomeAdapter? = null

    lateinit var getDao: AllDaos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        getDao = AppDatabase.get.getDatabase().getDao()
        loadData()
        innerItemClick()
        allModulesClick()
        settingsClick()

        return binding.root
    }

    private fun settingsClick() {
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.addCourse)
        }
    }

    private fun allModulesClick() {
        // works when clicks 'Barchasi' and opens CourseFragment
        generalAdapter?.onAllItemClick = object : GeneralAdapter.OnAllItemClick {
            override fun onClick(course: Course) {
                val bundle = Bundle()
                bundle.putInt("courseID", course.id!!)
                bundle.putString("courseName", course.courseName)
                findNavController().navigate(R.id.courseFragment, bundle)
            }
        }
    }

    private fun innerItemClick() {
        // ItemHomeAdapterni itemi bosilganda ishlaydigan listener
        itemHomeAdapter?.onModuleClick = object : ItemHomeAdapter.OnModuleClick {
            override fun onClick(module: Module) {
                Toast.makeText(binding.root.context, "Item Clicked: ${module.moduleName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        data = ArrayList()
        generalAdapter = GeneralAdapter()
        itemHomeAdapter = ItemHomeAdapter(binding.root.context)
        var courseID: Int

        getDao.getAllCourse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.functions.Consumer<List<Course>> {
                override fun accept(t: List<Course>?) {


                    for (i in 0 until t!!.size) {
                        courseID = t[i].id!!


                        getDao.getModulesByCourceID(courseID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                object : io.reactivex.functions.Consumer<List<Module>> {
                                    override fun accept(p: List<Module>) {

                                        data?.add(GeneralData(t[i], p))



                                        generalAdapter?.setAdapter(binding.root.context, itemHomeAdapter!!)
                                        generalAdapter?.submitList(data)

                                    }
                                },
                                object : io.reactivex.functions.Consumer<Throwable> {
                                    override fun accept(p0: Throwable) {

                                    }
                                }, object : Action {
                                    override fun run() {

                                    }
                                })
                    }
                }

            }, object : io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable?) {

                }

            }, object : Action {

                override fun run() {

                }

            })

        binding.homeMainRv.adapter = generalAdapter

    }
}