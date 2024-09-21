package com.yk.mobile.advanced3dfilemanager.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.FragmentDownloadsBinding
import com.yk.mobile.advanced3dfilemanager.ui.adapters.AllFilesListAdapter
import com.yk.mobile.advanced3dfilemanager.viewmodel.FilesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import utility.Constants
import utility.FilesResult
import utility.FileHelper
import utility.Utils
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class DownloadsFragment : Fragment() {
    private var _binding: FragmentDownloadsBinding? = null
    private val binding get() = _binding!!

    private val downloadViewModel: FilesListViewModel by viewModels()

    private lateinit var adapter: AllFilesListAdapter

    @Inject
    lateinit var fileHelper: FileHelper

    @Inject
    lateinit var utils: Utils

    private var redirectionType: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        redirectionType = args?.getString(Constants.REDIRECTION_TYPE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        adapter = AllFilesListAdapter(::onFileItemClicked, utils)
        return binding.root
    }

    private fun onFileItemClicked(file: File) {
        fileHelper.openFile(file.path)
//        val bundle = Bundle()
//        bundle.putString("bundle", Gson().toJson(bundle))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rvList.adapter = adapter
        bindObservers()
        setToolbarTitle()
        setClickListener()

        redirectionType.let {
            when (it) {
                Constants.IMAGE -> {
                    downloadViewModel.getDownLoadsFilesList()
                }
                Constants.AUDIO-> {
                    downloadViewModel.getDownLoadsFilesList()
                }
                Constants.VIDEO -> {
                    downloadViewModel.getDownLoadsFilesList()
                }
                Constants.ZIP -> {
                    downloadViewModel.getDownLoadsFilesList()
                }
                Constants.APP-> {
                    downloadViewModel.getDownLoadsFilesList()
                }
                Constants.DOCUMENT -> {
                    downloadViewModel.getDocumentsFilesList()
                }
                Constants.DOWNLOADS -> {
                    downloadViewModel.getDownLoadsFilesList()
                }
            }

        }

    }

    private fun setClickListener() {
        binding.include.btnBack.setOnClickListener({
            it.findNavController().popBackStack()
        })
    }


    private fun bindObservers() {
        downloadViewModel.downloadFilesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is FilesResult.Success -> {
                    val title = getString(R.string.downloads).plus(" (${it.data?.size})")
                    binding.include.collapsingToolbar.title = title
                    binding.include.toolbar.title = title
                    adapter.submitList(it.data)
                }

                is FilesResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is FilesResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun setToolbarTitle() {
        val title = getString(R.string.downloads)
        binding.include.collapsingToolbar.isTitleEnabled = true
        binding.include.collapsingToolbar.expandedTitleGravity =
            Gravity.CENTER_HORIZONTAL
        binding.include.collapsingToolbar.title = title
        binding.include.toolbar.title = title
        binding.include.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        binding.include.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        binding.include.etSearchView.hint = getString(R.string.search)
    }


}