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

class GeneralAdapter(
    var context: Context,
    var data: ArrayList<GeneralData>,
    var itemHomeAdapter: ItemHomeAdapter
) :
    RecyclerView.Adapter<GeneralAdapter.VH>() {

    var onAllItemClick: OnAllItemClick? = null

    inner class VH(var binding: ItemGeneralHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(generalData: GeneralData) {
            binding.courseName.text = generalData.course.courseName
            binding.root.animation = AnimationUtils.loadAnimation(context, R.anim.anim1)

//            val itemHomeAdapter = ItemHomeAdapter(context, generalData.module)
            itemHomeAdapter.setAdapter(generalData.module)
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
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size

    interface OnAllItemClick {
        fun onClick(course: Course)
    }
}