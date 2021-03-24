package uz.revolution.pdponlinerxkotlin.settings.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemCourseBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import java.io.File

class AddCourseAdapter : ListAdapter<Course, AddCourseAdapter.VH>(MyDiffUtil()) {

    var onEditClick: OnEditClick? = null
    var onDeleteClick: OnDeleteClick? = null

    inner class VH(var itemCourseBinding: ItemCourseBinding) :
        RecyclerView.ViewHolder(itemCourseBinding.root) {

        fun onBind(course: Course) {
            itemCourseBinding.courseName.text = course.courseName

            val file = File(course.imagePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                itemCourseBinding.courseImage.setImageBitmap(bitmap)
            }

            itemCourseBinding.courseEdit.setOnClickListener {
                onEditClick?.onClick(course)
            }

            itemCourseBinding.courseTrash.setOnClickListener {
                onDeleteClick?.onClick(course)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnEditClick {
        fun onClick(course: Course)
    }

    interface OnDeleteClick {
        fun onClick(course: Course)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {

        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.equals(newItem)
        }
    }

}