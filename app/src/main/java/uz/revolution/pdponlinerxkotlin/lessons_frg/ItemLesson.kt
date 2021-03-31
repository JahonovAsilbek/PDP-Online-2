package uz.revolution.pdponlinerxkotlin.lessons_frg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.databinding.FragmentItemLessonBinding
import uz.revolution.pdponlinerxkotlin.databinding.ItemLesson2Binding
import uz.revolution.pdponlinerxkotlin.databinding.ItemLessonBinding
import uz.revolution.pdponlinerxkotlin.entities.Lesson

private const val ARG_PARAM1 = "lesson"
private const val ARG_PARAM2 = "param2"

class ItemLesson : Fragment() {

    private var lesson: Lesson? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson = it.getSerializable(ARG_PARAM1) as Lesson
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentItemLessonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemLessonBinding.inflate(layoutInflater, container, false)

        binding.lessonLocation.text="${lesson?.lessonLocation}-dars"
        binding.lessonName.text=lesson?.lessonName
        binding.lessonInfo.text = lesson?.lessonInfo
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}