package uz.revolution.pdponlinerxkotlin.moduls_frg.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.revolution.pdponlinerxkotlin.databinding.ItemModuleBinding
import uz.revolution.pdponlinerxkotlin.entities.Module

class ModulsAdapter(var modulList: ArrayList<Module>, var imagePath: String) : RecyclerView.Adapter<ModulsAdapter.Vh>() {
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
        holder.bindData(modulList[position])
    }

    override fun getItemCount(): Int = modulList.size

    fun addElement(module: Module) {
        this.modulList.add(module)
        notifyDataSetChanged()
    }
}