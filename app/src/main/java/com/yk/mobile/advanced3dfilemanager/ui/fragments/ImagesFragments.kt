package com.yk.mobile.advanced3dfilemanager.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.FragmentAllListBinding
import com.yk.mobile.advanced3dfilemanager.model.MediaModel
import com.yk.mobile.advanced3dfilemanager.ui.adapters.ImageVideoFilesListAdapter
import com.yk.mobile.advanced3dfilemanager.viewmodel.FilesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import utility.Constants
import utility.FileHelper
import utility.FilesResult
import utility.OnBackPressedListener
import utility.SecuredPreferenceHelper
import utility.Utils
import javax.inject.Inject

@AndroidEntryPoint
class ImagesFragments : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var fileHelper: FileHelper

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var securedPreferenceHelper: SecuredPreferenceHelper

    private var redirectionType: String? = null

    private var _binding: FragmentAllListBinding? = null

    private val binding get() = _binding!!

    private val downloadViewModel: FilesListViewModel by viewModels()

    private lateinit var imageVideoFilesListAdapter: ImageVideoFilesListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        redirectionType = args?.getString(Constants.REDIRECTION_TYPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllListBinding.inflate(inflater, container, false)
        imageVideoFilesListAdapter =
            ImageVideoFilesListAdapter(::onFolderItemClicked, utils, fileHelper, requireActivity())
        return binding.root
    }

    private fun onFolderItemClicked(mediaModel: MediaModel) {
        val bundle = Bundle().apply {
            putString(Constants.REDIRECTION_TYPE, redirectionType)
            putString(Constants.DIRECTORY_PATH, mediaModel.file.path)
        }
        findNavController().navigate(R.id.action_imagesFragments_to_allFragments, bundle)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setSwipeOnRecyclerBarView()
        bindObservers()
        setToolbar()
        setClickListener()
        binding.rvList.setHasFixedSize(true)
        binding.rvList.setItemViewCacheSize(20)
        binding.rvList.adapter = imageVideoFilesListAdapter
        setRecyclerView()
        getDataList()
    }

    private fun setRecyclerView() {
        val booleanSecuredPreference =
            securedPreferenceHelper.getBooleanSecuredPreference(Constants.IS_LIST_VIEW, true)
        binding.rvList.layoutManager = if (booleanSecuredPreference) {
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        } else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        imageVideoFilesListAdapter.updateViewType(booleanSecuredPreference)
    }


    private fun setListTypeInToolbar(booleanSecuredPreference: Boolean) {
        if (booleanSecuredPreference) {
            binding.include.btnGridViewListView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_list_view
                )
            )
        } else {
            binding.include.btnGridViewListView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_grid_view
                )
            )
        }
    }

    private fun getDataList() {
        redirectionType.let {
            when (it) {
                Constants.PICTURES -> {
                    downloadViewModel.getImageFilesList()
                }

                Constants.VIDEO -> {
                    downloadViewModel.getVideosFilesList()
                }
            }
        }
    }

    private fun bindObservers() {
        redirectionType.let {
            when (it) {
                Constants.PICTURES -> {
                    setPicturesObserver()
                }

                Constants.VIDEO -> {
                    setVideosObserver()
                }
            }
        }
    }

    private fun setPicturesObserver() {
        lifecycleScope.launch {
            downloadViewModel.imageFilesLiveData.collect { result ->
                hideProgressBar()
                manageMediaListResponseData(result)
            }
        }
    }

    private fun setVideosObserver() {
        lifecycleScope.launch {
            downloadViewModel.videosFilesLiveData.collect { listFilesResult ->
                hideProgressBar()
                manageMediaListResponseData(listFilesResult)
            }
        }
    }

    private fun manageMediaListResponseData(it: FilesResult<List<MediaModel>>) {
        when (it) {
            is FilesResult.Success -> {
                updateToolBarTitlesFolders(it)
                binding.rvList.isVisible = true
                imageVideoFilesListAdapter.updateFiles(it.data!!)
            }

            is FilesResult.Error -> {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            is FilesResult.Loading -> {
                showProgressBar()
            }
        }
    }


    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun updateToolBarTitlesFolders(filesList: FilesResult<List<MediaModel>>) {
        val title = redirectionType.let { it.plus(" (${filesList.data?.size})") }
        binding.include.collapsingToolbar.title = title
        binding.include.toolbar.title = title
    }


    private fun setToolbar() {
        redirectionType.let {
            binding.include.collapsingToolbar.title = it
            binding.include.toolbar.title = it
        }
        binding.include.collapsingToolbar.isTitleEnabled = true
        binding.include.collapsingToolbar.expandedTitleGravity =
            Gravity.CENTER_HORIZONTAL
        binding.include.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        binding.include.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
        binding.include.etSearchView.hint = getString(R.string.search)
    }

    private fun setClickListener() {
        binding.include.btnBack.setOnClickListener({
            onBackPressed()
        })
        binding.include.btnGridViewListView.setOnClickListener({
            val booleanSecuredPreference =
                securedPreferenceHelper.getBooleanSecuredPreference(Constants.IS_LIST_VIEW, true)
            securedPreferenceHelper.saveBooleanSecuredPreference(
                Constants.IS_LIST_VIEW,
                !booleanSecuredPreference
            )
            setListTypeInToolbar(booleanSecuredPreference)
            //update layouts
            setRecyclerView()
        })

    }

    override fun onBackPressed(): Boolean {
        // Return true if back press is handled in the fragment
        return if (shouldHandleCustomBackPress()) {
            true
        } else {
            // Let the activity handle it
            false
        }
    }

    private fun shouldHandleCustomBackPress(): Boolean {
        // Your custom conditions here
        return false
    }


}