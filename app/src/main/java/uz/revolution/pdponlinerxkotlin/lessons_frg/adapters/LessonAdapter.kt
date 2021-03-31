package uz.revolution.pdponlinerxkotlin.lessons_frg.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemLessonBinding
import uz.revolution.pdponlinerxkotlin.entities.Lesson

class LessonAdapter : ListAdapter<Lesson, LessonAdapter.Vh>(MyDiffUtil()) {

    private var imagePath: String? = null

    var onItemClick:OnItemClick?=null
    var onEditClick:OnEditClick?=null
    var onDeleteClick:OnDeleteClick?=null

    fun setImage(imagePath: String) {
        this.imagePath = imagePath
    }

    inner class Vh(var binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(lesson: Lesson) {
            binding.lessonName.text = "${lesson.lessonLocation}-dars"
            binding.lessonImage.setImageURI(Uri.parse(imagePath))
            binding.lessonInfo.text = lesson.lessonInfo

            binding.root.setOnClickListener {
                onItemClick?.onClick(lesson)
            }

            binding.lessonEdit.setOnClickListener {
                onEditClick?.onClick(lesson)
            }

            binding.lessonTrash.setOnClickListener {
                onDeleteClick?.onClick(lesson)
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

    interface OnItemClick{
        fun onClick(lesson: Lesson)
    }

    interface OnEditClick{
        fun onClick(lesson: Lesson)
    }

    interface OnDeleteClick{
        fun onClick(lesson: Lesson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}