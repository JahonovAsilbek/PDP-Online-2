package uz.revolution.pdponlinerxkotlin.course.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module

class CourseAdapter : ListAdapter<Module, CourseAdapter.VH>(MyDiffUtil()) {

    private var courseName: String? = null
    private var lessonCountList: ArrayList<Int>? = null
    private var imagePath: String? = null
    var onMoreClick: OnMoreClick? = null

    fun setAdapter(
        imagePath: String,
        courseName: String,
        lessonCountList: ArrayList<Int>
    ) {
        this.imagePath = imagePath
        this.courseName = courseName
        this.lessonCountList = lessonCountList
    }

    inner class VH(var itemModuleBinding: ItemModuleBinding) :
        RecyclerView.ViewHolder(itemModuleBinding.root) {

        fun onBind(module: Module, studentCount: Int) {
            itemModuleBinding.moduleName.text = module.moduleName
            itemModuleBinding.courseName.text = courseName
            itemModuleBinding.moduleImage.setImageURI(Uri.parse(imagePath))
            itemModuleBinding.moduleSize.text = studentCount.toString()

            itemModuleBinding.moduleSeeMore.setOnClickListener {
                onMoreClick?.onClick(module.id!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position), lessonCountList!![position])
    }

    interface OnMoreClick {
        fun onClick(moduleID: Int)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {

        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.equals(newItem)
        }

    }
}