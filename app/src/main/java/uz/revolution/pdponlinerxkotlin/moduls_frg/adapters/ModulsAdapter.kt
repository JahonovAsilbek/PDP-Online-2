package uz.revolution.pdponlinerxkotlin.moduls_frg.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module

class ModulsAdapter :
    ListAdapter<Module, ModulsAdapter.Vh>(MyDiffUtil()) {

    //    private var modulList: ArrayList<Module>? = null
    private var imagePath: String? = null

    fun setModuleImage(imagePath: String) {
        this.imagePath = imagePath
    }

    inner class Vh(var binding: ItemModuleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(module: Module) {
            binding.moduleImage.setImageURI(Uri.parse(imagePath))
            binding.moduleName.text = module.moduleName
            binding.moduleSize.text = module.location.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bindData(getItem(position))
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