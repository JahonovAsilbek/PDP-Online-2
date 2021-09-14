package uz.revolution.pdponlinerxkotlin.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.databinding.ItemGeneralHomeBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Module
import uz.revolution.pdponlinerxkotlin.models.GeneralData

class GeneralAdapter : RecyclerView.Adapter<GeneralAdapter.VH>() {

    var onAllItemClick: OnAllItemClick? = null
    lateinit var context: Context
    var generalData: ArrayList<GeneralData>? = null
    lateinit var onModuleClick: OnModuleClick

    fun setAdapter(
        context: Context,
        generalData: ArrayList<GeneralData>,
        onModuleClick: OnModuleClick
    ) {
        this.context = context
        this.generalData = generalData
        this.onModuleClick = onModuleClick
    }

    inner class VH(var binding: ItemGeneralHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(generalData: GeneralData) {
            binding.courseName.text = generalData.course.courseName
            binding.root.animation = AnimationUtils.loadAnimation(context, R.anim.anim1)

            val itemHomeAdapter = ItemHomeAdapter(context)
            itemHomeAdapter.setAdapter(generalData.module, onModuleClick)
            binding.homeItemRv.adapter = itemHomeAdapter

            binding.allModules.setOnClickListener {
                onAllItemClick?.onClick(generalData.course)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemGeneralHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(generalData!![position])
    }

    interface OnAllItemClick {
        fun onClick(course: Course)
    }

    interface OnModuleClick {
        fun onClick(module: Module)
    }

    override fun getItemCount(): Int = generalData!!.size
}