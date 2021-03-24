package uz.revolution.pdponlinerxkotlin.course.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemCourseBinding
import uz.revolution.pdponlinerxkotlin.databinding.ItemModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.VH>() {

    private var moduleList: ArrayList<Module>? = null
    private var courseName: String? = null
    private var lessonCountList: ArrayList<Int>? = null
    var onMoreClick: OnMoreClick? = null

    fun setAdapter(
        moduleList: ArrayList<Module>,
        courseName: String,
        lessonCountList: ArrayList<Int>
    ) {
        this.moduleList = moduleList
        this.courseName = courseName
        this.lessonCountList = lessonCountList
    }

    inner class VH(var itemModuleBinding: ItemModuleBinding) :
        RecyclerView.ViewHolder(itemModuleBinding.root) {

        fun onBind(module: Module, studentCount: Int) {
            itemModuleBinding.moduleName.text = module.moduleName
            itemModuleBinding.courseName.text = courseName
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
        holder.onBind(moduleList!![position], lessonCountList!![position])
    }

    override fun getItemCount(): Int = moduleList!!.size

    interface OnMoreClick {
        fun onClick(moduleID: Int)
    }
}