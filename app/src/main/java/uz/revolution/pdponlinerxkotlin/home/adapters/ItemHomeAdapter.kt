package uz.revolution.pdponlinerxkotlin.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.R
import uz.revolution.pdponlinerxkotlin.databinding.ItemHomeBinding
import uz.revolution.pdponlinerxkotlin.entities.Module

class ItemHomeAdapter(var context: Context) :
    RecyclerView.Adapter<ItemHomeAdapter.VH>() {

    private var data: List<Module>? = null
    lateinit var onModuleClick: GeneralAdapter.OnModuleClick

    fun setAdapter(data: List<Module>, onModuleClick: GeneralAdapter.OnModuleClick) {
        this.data = data
        this.onModuleClick = onModuleClick
    }

    inner class VH(var itemHomeBinding: ItemHomeBinding) :
        RecyclerView.ViewHolder(itemHomeBinding.root) {

        fun onBind(module: Module) {
            itemHomeBinding.itemModule.text = module.moduleName

            itemHomeBinding.root.animation = AnimationUtils.loadAnimation(context, R.anim.anim2)

            itemHomeBinding.itemModule.setOnClickListener {
                onModuleClick.onClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(data!![position])
    }

    override fun getItemCount(): Int = data!!.size


}