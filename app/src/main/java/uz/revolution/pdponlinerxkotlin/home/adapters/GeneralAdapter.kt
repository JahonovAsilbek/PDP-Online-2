package uz.revolution.pdponlinerxkotlin.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.databinding.ItemGeneralHomeBinding
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.models.GeneralData

class GeneralAdapter : ListAdapter<GeneralData, GeneralAdapter.VH>(MyDiffUtil()) {

    var onAllItemClick: OnAllItemClick? = null
    var context: Context? = null
    var itemHomeAdapter: ItemHomeAdapter? = null

    fun setAdapter(context: Context, itemHomeAdapter: ItemHomeAdapter) {
        this.context = context
        this.itemHomeAdapter = itemHomeAdapter
    }

    inner class VH(var binding: ItemGeneralHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(generalData: GeneralData) {
            binding.courseName.text = generalData.course.courseName
            binding.root.animation = AnimationUtils.loadAnimation(context, R.anim.anim1)

//            val itemHomeAdapter = ItemHomeAdapter(context, generalData.module)
            itemHomeAdapter?.setAdapter(generalData.module)
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
        holder.onBind(getItem(position))
    }

    interface OnAllItemClick {
        fun onClick(course: Course)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<GeneralData>() {

        override fun areItemsTheSame(oldItem: GeneralData, newItem: GeneralData): Boolean {
            return oldItem.course.id == newItem.course.id
        }

        override fun areContentsTheSame(oldItem: GeneralData, newItem: GeneralData): Boolean {
            return oldItem.equals(newItem)
        }

    }
}