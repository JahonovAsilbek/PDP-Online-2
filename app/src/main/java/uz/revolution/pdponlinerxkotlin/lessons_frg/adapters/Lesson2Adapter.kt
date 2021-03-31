package uz.revolution.pdponlinerxkotlin.lessons_frg.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemLesson2Binding
import uz.revolution.pdponlinerxkotlin.entities.Lesson

class Lesson2Adapter : ListAdapter<Lesson, Lesson2Adapter.Vh>(MyDiffUtil()) {

    var onItemClick: OnItemClick? = null

    inner class Vh(var binding: ItemLesson2Binding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(lesson: Lesson) {
            binding.lessonLocation.text = "${lesson.lessonLocation}\ndars"
            binding

            binding.root.setOnClickListener {
                onItemClick?.onClick(lesson)
            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {

        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.equals(newItem)
        }
    }

    interface OnItemClick {
        fun onClick(lesson: Lesson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemLesson2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}