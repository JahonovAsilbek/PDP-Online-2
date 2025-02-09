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
    private var courseName: String? = null
    var onEditClick: OnEditClick? = null
    var onDeleteClick: OnDeleteClick? = null
    var onItemClick: OnItemClick? = null

    fun setModuleImage(imagePath: String, courseName: String) {
        this.imagePath = imagePath
        this.courseName = courseName
    }

    inner class Vh(var binding: ItemModuleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(module: Module) {
            binding.moduleImage.setImageURI(Uri.parse(imagePath))
            binding.moduleName.text = module.moduleName
            binding.moduleSize.text = module.location.toString()
            binding.courseName.text = courseName

            binding.moduleEdit.setOnClickListener {
                onEditClick?.onClick(module)
            }

            binding.moduleTrash.setOnClickListener {
                onDeleteClick?.onClick(module)
            }

            binding.root.setOnClickListener {
                onItemClick?.onClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bindData(getItem(position))
    }

    interface OnDeleteClick {
        fun onClick(module: Module)
    }

    interface OnEditClick {
        fun onClick(module: Module)
    }

    interface OnItemClick {
        fun onClick(module: Module)
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