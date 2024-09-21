package com.yk.mobile.advanced3dfilemanager.ui.adapters

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileItemBinding
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import utility.Constants
import utility.Utils
import java.io.File
import java.util.Locale


class AllFilesListAdapter(private val onFileItemClicked: (File) -> Unit, private val utils: Utils) :
    ListAdapter<File, AllFilesListAdapter.FilesListViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesListViewHolder {
        val binding =
            AllTypeFileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilesListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilesListViewHolder, position: Int) {
        val file = getItem(position)
        file?.let {
            holder.bind(it)
        }
    }

    inner class FilesListViewHolder(private val binding: AllTypeFileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File) {
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = Formatter.formatFileSize(binding.root.context, file.length())
            binding.tvModifiedTime.text =
                utils.getFormatedDate(file.lastModified(), Constants.DATE_FORMAT_V1)

            if (!file.isDirectory()) {
                val type: String =
                    file.name.substring(file.name.lastIndexOf('.') + 1)
                binding.ivDocTypeIcon.setImageResource(
                    utils.getFileExtensionIcon(
                        type.lowercase(
                            Locale.getDefault()
                        )
                    )
                )
            } else {
                binding.ivDocTypeIcon.setImageResource(R.drawable.ic_neu_folder)
            }
            binding.root.setOnClickListener {
                onFileItemClicked(file)
            }
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}