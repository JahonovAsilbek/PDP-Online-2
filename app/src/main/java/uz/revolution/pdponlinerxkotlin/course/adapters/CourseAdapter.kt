package uz.revolution.pdponlinerxkotlin.course.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.models.CourseModel

class CourseAdapter : ListAdapter<CourseModel, CourseAdapter.VH>(MyDiffUtil()) {

    private var courseName: String? = null
    private var imagePath: String? = null
    var onMoreClick: OnMoreClick? = null

    fun setAdapter(
        imagePath: String,
        courseName: String
    ) {
        this.imagePath = imagePath
        this.courseName = courseName
    }

    inner class VH(var itemModuleBinding: ItemModuleBinding) :
        RecyclerView.ViewHolder(itemModuleBinding.root) {

        fun onBind(module: Module, studentCount: Int) {
            itemModuleBinding.moduleName.text = module.moduleName
            itemModuleBinding.courseName.text = courseName
            itemModuleBinding.moduleImage.setImageURI(Uri.parse(imagePath))
            itemModuleBinding.moduleSize.text = studentCount.toString()
            itemModuleBinding.moduleTrash.visibility = View.GONE
            itemModuleBinding.moduleEdit.visibility = View.GONE

            itemModuleBinding.moduleSeeMore.setOnClickListener {
                onMoreClick?.onClick(module)
            }

            itemModuleBinding.root.setOnClickListener {
                onMoreClick?.onClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position).module, getItem(position).lessonCount)
    }

    interface OnMoreClick {
        fun onClick(module: Module)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<CourseModel>() {
        override fun areItemsTheSame(oldItem: CourseModel, newItem: CourseModel): Boolean {
            return oldItem.module.id == newItem.module.id
        }

        override fun areContentsTheSame(oldItem: CourseModel, newItem: CourseModel): Boolean {
            return oldItem.module.equals(newItem.module)
        }

    }

}