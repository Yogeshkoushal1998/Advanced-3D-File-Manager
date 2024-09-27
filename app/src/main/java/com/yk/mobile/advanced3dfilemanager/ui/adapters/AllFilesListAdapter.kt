package com.yk.mobile.advanced3dfilemanager.ui.adapters

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileGridItemBinding
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileListItemBinding
import utility.Constants
import utility.FileHelper
import utility.Utils
import java.io.File
import java.util.Locale

class AllFilesListAdapter(
    private val onFileItemClicked: (File) -> Unit,
    private val onSearchEnableGetCounts: (Int) -> Unit,
    private val utils: Utils,
    private val fileHelper: FileHelper,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var files: List<File> = listOf()

    private var filteredFiles: List<File> = listOf()

    private var isListView: Boolean = true;

    private val VIEW_TYPE_LIST = 0

    private val VIEW_TYPE_GRID = 1


    init {
        filteredFiles = files // Start with the full list
    }

    fun updateViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged() // Update the entire list; consider optimizing for large datasets
    }


    // Method to update the file list
    fun updateFiles(newFiles: List<File>) {
        files = newFiles
        filteredFiles = files
        notifyDataSetChanged() // Update the entire list; consider optimizing for large datasets
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LIST) {
            val binding =
                AllTypeFileListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ListViewHolder(binding)
        } else {
            val binding1 =
                AllTypeFileGridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return GridViewHolder(binding1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val file = filteredFiles[position]
        if (holder is ListViewHolder) {
            (holder as ListViewHolder).bind(file)
        } else if (holder is GridViewHolder) {
            (holder as GridViewHolder).bind(file)
        }
    }


    override fun getItemCount(): Int {
        return filteredFiles.size
    }

    inner class ListViewHolder(private val binding: AllTypeFileListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = Formatter.formatFileSize(binding.root.context, file.length())
            binding.tvModifiedTime.text =
                utils.getFormatedDate(file.lastModified(), Constants.DATE_FORMAT_V1)
            if (!file.isDirectory()) {
                val type: String = file.name.substring(file.name.lastIndexOf('.') + 1)
                val fileType = fileHelper.getFileType(file)

                when (fileType) {
                    Constants.PICTURES -> {
                        Glide.with(context)
                            .load(file)
                            .placeholder(R.drawable.ic_neu_png)
                            .error(R.drawable.ic_neu_png)
                            .override(100, 100)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .into(binding.ivDocTypeIcon)
                    }

                    Constants.VIDEO -> {
                        Glide.with(context)
                            .asBitmap()
                            .load(file)
                            .placeholder(R.drawable.ic_neu_mp4)
                            .error(R.drawable.ic_neu_mp4)
                            .override(100, 100)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .apply(RequestOptions.frameOf(1000000L))
                            .into(binding.ivDocTypeIcon)
                    }

                    Constants.APP -> {
                        binding.ivDocTypeIcon.setImageDrawable(utils.getApkIcon(context, file.path))
                    }

                    else -> {
                        binding.ivDocTypeIcon.setImageResource(
                            utils.getFileExtensionIcon(type.lowercase(Locale.getDefault()))
                        )
                    }
                }
            } else {
                binding.ivDocTypeIcon.setImageResource(R.drawable.ic_neu_folder)
            }
            binding.root.setOnClickListener {
                onFileItemClicked(file)
            }
        }
    }


    inner class GridViewHolder(private val binding: AllTypeFileGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = Formatter.formatFileSize(binding.root.context, file.length())
            if (!file.isDirectory()) {
                val type: String = file.name.substring(file.name.lastIndexOf('.') + 1)
                val fileType = fileHelper.getFileType(file)

                when (fileType) {
                    Constants.PICTURES -> {
                        Glide.with(context)
                            .load(file)
                            .placeholder(R.drawable.ic_neu_png)
                            .error(R.drawable.ic_neu_png)
                            .override(100, 100)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .into(binding.ivDocTypeIcon)
                    }

                    Constants.VIDEO -> {
                        Glide.with(context)
                            .asBitmap()
                            .load(file)
                            .placeholder(R.drawable.ic_neu_mp4)
                            .error(R.drawable.ic_neu_mp4)
                            .override(100, 100)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .apply(RequestOptions.frameOf(1000000L))
                            .into(binding.ivDocTypeIcon)
                    }

                    Constants.APP -> {
                        binding.ivDocTypeIcon.setImageDrawable(utils.getApkIcon(context, file.path))
                    }

                    else -> {
                        binding.ivDocTypeIcon.setImageResource(
                            utils.getFileExtensionIcon(type.lowercase(Locale.getDefault()))
                        )
                    }
                }
            } else {
                binding.ivDocTypeIcon.setImageResource(R.drawable.ic_neu_folder)
            }
            binding.root.setOnClickListener {
                onFileItemClicked(file)
            }
        }
    }


    override fun getItemId(position: Int): Long {
        return filteredFiles[position].lastModified()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isListView) {
            VIEW_TYPE_LIST
        } else {
            VIEW_TYPE_GRID
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim() ?: ""

                filteredFiles = if (query.isEmpty()) {
                    files // No query, return full list
                } else {
                    files.filter {
                        // Filter by file name or any other criteria
                        it.name.contains(query, ignoreCase = true)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredFiles
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredFiles = results?.values as List<File>
                onSearchEnableGetCounts(filteredFiles.size)
                notifyDataSetChanged()
            }
        }
    }

}
