package com.yk.mobile.advanced3dfilemanager.ui.adapters

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileGridItemBinding
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileListItemBinding
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import utility.Constants
import utility.FileHelper
import utility.Utils
import java.util.Locale


class ImageVideoFilesListAdapter(
    private val onFileItemClicked: (MediaModel) -> Unit,
    private val onSearchEnableGetCounts: (Int) -> Unit,
    private val utils: Utils,
    private val fileHelper: FileHelper,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var mediaModelList: List<MediaModel> = listOf()
    private var filteredMediaModelList: List<MediaModel> = listOf()

    private var isListView: Boolean = true

    private val VIEW_TYPE_LIST = 0
    private val VIEW_TYPE_GRID = 1

    init {
        filteredMediaModelList = mediaModelList // Start with the full list
    }

    fun updateViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged()
    }

    // Method to update the file list
    fun updateFiles(mediaModelList1: List<MediaModel>) {
        mediaModelList = mediaModelList1
        filteredMediaModelList = mediaModelList1 // Update filtered list as well
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LIST) {
            val binding =
                AllTypeFileListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ImageVideoFilesListViewHolder(binding)
        } else {
            val binding1 =
                AllTypeFileGridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ImageVideoFilesGridViewHolder(binding1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaModel = filteredMediaModelList[position]
        if (holder is ImageVideoFilesListViewHolder) {
            holder.bind(mediaModel)
        } else if (holder is ImageVideoFilesGridViewHolder) {
            holder.bind(mediaModel)
        }
    }


    inner class ImageVideoFilesListViewHolder(private val binding: AllTypeFileListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaModel: MediaModel) {
            val file = mediaModel.file
            val isDirectory = file.isDirectory
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = if (isDirectory) {
                "(${mediaModel.count})"
            } else {
                Formatter.formatFileSize(binding.root.context, file.length())
            }
            binding.tvModifiedTime.apply {
                text = utils.getFormatedDate(file.lastModified(), Constants.DATE_FORMAT_V1)
                isVisible = !isDirectory
            }

            if (!file.isDirectory()) {
                val type: String = file.name.substring(file.name.lastIndexOf('.') + 1)
                val fileType = fileHelper.getFileType(file)
                when (fileType) {
                    Constants.PICTURES -> {
                        Glide.with(context)
                            .asBitmap()
                            .load(mediaModel.firstFileOfDirectory)
                            .placeholder(R.drawable.ic_unknown)
                            .error(R.drawable.ic_unknown)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .apply(RequestOptions.frameOf(1000000L))
                            .into(binding.ivDocTypeIcon)
                    }

                    Constants.VIDEO -> {
                        Glide.with(context)
                            .asBitmap()
                            .load(mediaModel.firstFileOfDirectory)
                            .placeholder(R.drawable.ic_unknown)
                            .error(R.drawable.ic_unknown)
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
                onFileItemClicked(mediaModel)
            }
        }
    }


    inner class ImageVideoFilesGridViewHolder(private val binding: AllTypeFileGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaModel: MediaModel) {
            val file = mediaModel.file
            val isDirectory = file.isDirectory
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = if (isDirectory) {
                "(${mediaModel.count})"
            } else {
                Formatter.formatFileSize(binding.root.context, file.length())
            }

            if (!file.isDirectory()) {
                val type: String = file.name.substring(file.name.lastIndexOf('.') + 1)
                val fileType = fileHelper.getFileType(file)
                when (fileType) {
                    Constants.PICTURES -> {
                        Glide.with(context)
                            .asBitmap()
                            .load(mediaModel.firstFileOfDirectory)
                            .placeholder(R.drawable.ic_unknown)
                            .error(R.drawable.ic_unknown)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .apply(RequestOptions.frameOf(1000000L))
                            .into(binding.ivDocTypeIcon)
                    }

                    Constants.VIDEO -> {
                        Glide.with(context)
                            .asBitmap()
                            .load(mediaModel.firstFileOfDirectory)
                            .placeholder(R.drawable.ic_unknown)
                            .error(R.drawable.ic_unknown)
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
                onFileItemClicked(mediaModel)
            }
        }
    }


    override fun getItemId(position: Int): Long {
        return mediaModelList[position].file.lastModified()
    }

    override fun getItemCount(): Int {
        return filteredMediaModelList.size
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

                filteredMediaModelList = if (query.isEmpty()) {
                    mediaModelList // No query, return full list
                } else {
                    mediaModelList.filter {
                        // Filter by file name or any other criteria
                        it.file.name.contains(query, ignoreCase = true)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredMediaModelList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMediaModelList = results?.values as List<MediaModel>
                onSearchEnableGetCounts(filteredMediaModelList.size)
                notifyDataSetChanged()
            }
        }
    }
}
