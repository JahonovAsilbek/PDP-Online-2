package uz.revolution.pdponlinerxkotlin.course.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.models.CourseModel

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.VH>() {

    private var courseName: String? = null
    private var imagePath: String? = null
    var onMoreClick: OnMoreClick? = null
    lateinit var courseModel: ArrayList<CourseModel>

    fun setAdapter(
        imagePath: String,
        courseName: String,
        courseModel: ArrayList<CourseModel>
    ) {
        this.imagePath = imagePath
        this.courseName = courseName
        this.courseModel = courseModel
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
        holder.onBind(courseModel[position].module, courseModel[position].lessonCount)
    }

    interface OnMoreClick {
        fun onClick(module: Module)
    }

    override fun getItemCount(): Int = courseModel.size

}