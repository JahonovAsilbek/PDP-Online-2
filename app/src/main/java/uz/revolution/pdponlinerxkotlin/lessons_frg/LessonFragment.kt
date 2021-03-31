package uz.revolution.pdponlinerxkotlin.lessons_frg

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.database.AppDatabase
import uz.revolution.pdponlinerxkotlin.databinding.FragmentLessonsBinding
import uz.revolution.pdponlinerxkotlin.entities.Lesson
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.lessons_frg.adapters.Lesson2Adapter

private const val ARG_PARAM1 = "module"
private const val ARG_PARAM2 = "param2"

class LessonFragment : Fragment() {

    private var module: Module? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            module = it.getSerializable(ARG_PARAM1) as Module?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentLessonsBinding
    private var adapter: Lesson2Adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsBinding.inflate(layoutInflater, container, false)

        binding.title.text = module?.moduleName
        loadData()
        itemClick()


        return binding.root
    }

    private fun itemClick() {
        adapter?.onItemClick=object :Lesson2Adapter.OnItemClick{
            override fun onClick(lesson: Lesson) {
                val bundle = Bundle()
                bundle.putSerializable("lesson", lesson)
                findNavController().navigate(R.id.itemLesson,bundle)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        adapter = Lesson2Adapter()

        AppDatabase.get.getDatabase().getDao().getLessonsByModuleID(module?.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Lesson>> {
                override fun accept(t: List<Lesson>?) {
                    adapter?.submitList(t)
                }
            })
        binding.lessonsRv.adapter = adapter
    }

}