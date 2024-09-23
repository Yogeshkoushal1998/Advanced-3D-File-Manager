package com.yk.mobile.advanced3dfilemanager.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileGridItemBinding
import com.yk.mobile.advanced3dfilemanager.databinding.AllTypeFileListItemBinding
import com.yk.mobile.advanced3dfilemanager.databinding.ImageVideoFolderGridViewItemBinding
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import com.yk.mobile.advanced3dfilemanager.ui.adapters.AllFilesListAdapter.GridViewHolder
import com.yk.mobile.advanced3dfilemanager.ui.adapters.AllFilesListAdapter.ListViewHolder
import utility.FileHelper
import utility.Utils


class ImageVideoFilesListAdapter(
    private val onFileItemClicked: (MediaModel) -> Unit,
    private val utils: Utils,
    private val fileHelper: FileHelper, private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mediaModelList: List<MediaModel> = listOf()

    private var isListView: Boolean = true;

    private val VIEW_TYPE_LIST = 0

    private val VIEW_TYPE_GRID = 1

    fun updateViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged() // Update the entire list; consider optimizing for large datasets
    }


    // Method to update the file list
    fun updateFiles(mediaModelList1: List<MediaModel>) {
        mediaModelList = mediaModelList1
        notifyDataSetChanged() // Update the entire list; consider optimizing for large datasets
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LIST) {
            val binding =
                AllTypeFileListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ImageVideoFilesListViewHolder(binding)
        } else {
            val binding1 =
                ImageVideoFolderGridViewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ImageVideoFilesGridViewHolder(binding1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaModel = mediaModelList[position]
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
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = "(${mediaModel.count})"
            binding.tvModifiedTime.isVisible=false
            Glide.with(context)
                .asBitmap()
                .load(mediaModel.firstFileOfDirectory)
                .placeholder(R.drawable.ic_unknown)
                .error(R.drawable.ic_unknown)
                .override(100, 100)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .apply(RequestOptions.frameOf(1000000L))
                .into(binding.ivDocTypeIcon)
            binding.root.setOnClickListener {
                onFileItemClicked(mediaModel)
            }
        }
    }


    inner class ImageVideoFilesGridViewHolder(private val binding: ImageVideoFolderGridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaModel: MediaModel) {
            val file = mediaModel.file
            binding.tvFileName.text = file.name
            binding.tvFileSize.text = "(${mediaModel.count})"
            Glide.with(context)
                .asBitmap()
                .load(mediaModel.firstFileOfDirectory)
                .placeholder(R.drawable.ic_unknown)
                .error(R.drawable.ic_unknown)
                .override(100, 100)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .apply(RequestOptions.frameOf(1000000L))
                .into(binding.ivDocTypeIcon)
            binding.root.setOnClickListener {
                onFileItemClicked(mediaModel)
            }
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mediaModelList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isListView) {
            VIEW_TYPE_LIST
        } else {
            VIEW_TYPE_GRID
        }
    }
}