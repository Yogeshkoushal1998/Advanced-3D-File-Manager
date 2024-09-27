package com.yk.mobile.advanced3dfilemanager.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.FragmentAllListBinding
import com.yk.mobile.advanced3dfilemanager.ui.adapters.AllFilesListAdapter
import com.yk.mobile.advanced3dfilemanager.viewmodel.FilesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import utility.Constants
import utility.FileHelper
import utility.FilesResult
import utility.OnBackPressedListener
import utility.SecuredPreferenceHelper
import utility.Utils
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class AllListFragment : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var fileHelper: FileHelper

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var securedPreferenceHelper: SecuredPreferenceHelper

    private var _binding: FragmentAllListBinding? = null

    private val binding get() = _binding!!

    private var redirectionType: String? = null

    private var directoryPath: String? = null

    private val downloadViewModel: FilesListViewModel by viewModels()

    private lateinit var adapter: AllFilesListAdapter

    private var isSearchEnable = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        redirectionType = args?.getString(Constants.REDIRECTION_TYPE)
        directoryPath = args?.getString(Constants.DIRECTORY_PATH)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllListBinding.inflate(inflater, container, false)
        adapter = AllFilesListAdapter(
            ::onFileItemClicked,
            ::onSearchEnableGetCounts,
            utils,
            fileHelper,
            requireActivity()
        )
        return binding.root
    }

    private fun onFileItemClicked(file: File) {
        if (file.isDirectory) {
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE, Constants.GENERAL)
                putString(Constants.DIRECTORY_PATH, file.path)
            }
            findNavController().navigate(R.id.action_allFragments_self, bundle)
        } else {
            fileHelper.openFile(file.path)
        }
    }

    private fun onSearchEnableGetCounts(listSize: Int) {
        binding.noDataAvailable.root.isVisible = listSize == 0
        val title = redirectionType.let {
            var titleSuffix =
                if (redirectionType.equals(Constants.GENERAL) || redirectionType.equals(Constants.PICTURES) || redirectionType.equals(
                        Constants.VIDEO
                    )
                ) {
                    File(directoryPath).name
                } else {
                    it
                }
            titleSuffix.plus(" (${listSize})")
        }
        binding.include.collapsingToolbar.title = title
        binding.include.toolbar.title = title
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        setToolbar()
        setListTypeInToolbar(
            securedPreferenceHelper.getBooleanSecuredPreference(
                Constants.IS_LIST_VIEW,
                true
            )
        )
        setClickListener()
        setSearchView()
        binding.rvList.setHasFixedSize(true)
        binding.rvList.setItemViewCacheSize(20)
        binding.rvList.adapter = adapter
        setRecyclerView()
        redirectionType.let {
            when (it) {
                Constants.AUDIO -> {
                    downloadViewModel.getAudiosFilesList()
                }

                Constants.ZIP -> {
                    downloadViewModel.getZipsFilesList()
                }

                Constants.APP -> {
                    downloadViewModel.getAppsFilesList()
                }

                Constants.DOCUMENT -> {
                    downloadViewModel.getDocumentsFilesList()
                }

                Constants.DOWNLOADS -> {
                    downloadViewModel.getDownLoadsFilesList()
                }

                Constants.VIDEO -> {
                    directoryPath.let {
                        downloadViewModel.getAllFilesOfDirectoryOfSpecificType(
                            File(it),
                            Constants.VIDEO
                        )
                    }
                }

                Constants.PICTURES -> {
                    directoryPath.let {
                        downloadViewModel.getAllFilesOfDirectoryOfSpecificType(
                            File(it),
                            Constants.PICTURES
                        )
                    }
                }

            }

        }

    }

    private fun setSearchView() {
        binding.include.btnSearch.setOnClickListener({
            showSearchView(true)
        })

        binding.include.commonSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText)
                return false
            }
        })



        binding.include.ivCrossSearchView.setOnClickListener({
            if (binding.include.commonSearchView.query!!.isEmpty()) {
                showSearchView(false)
            } else {
                binding.include.commonSearchView.setQuery("", false)
            }
        })
    }


    fun showSearchView(isShow: Boolean) {
        isSearchEnable=isShow
        binding.include.btnBack.isVisible = !isShow
        binding.include.btnSearch.isVisible = !isShow
        binding.include.btnGridViewListView.isVisible = !isShow
        binding.include.neumorphSearchView.isVisible = isShow
        binding.include.commonSearchView.requestFocus()
    }


    private fun setRecyclerView() {
        val booleanSecuredPreference =
            securedPreferenceHelper.getBooleanSecuredPreference(Constants.IS_LIST_VIEW, true)
        binding.rvList.layoutManager = if (booleanSecuredPreference) {
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        } else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        adapter.updateViewType(booleanSecuredPreference)
    }

    /*private fun setSwipeOnRecyclerBarView() {
        binding.customScrollbar.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val scrollBarHeight = binding.customScrollbar.height
                    val touchY = event.y
                    // Calculate scroll position percentage
                    val scrollPercent = touchY / scrollBarHeight
                    // Calculate the target item position in RecyclerView
                    val targetPosition = (adapter.itemCount * scrollPercent).toInt()
                        .coerceIn(0, adapter.itemCount - 1)
                    // Smooth scroll to target position
                    binding.rvList.smoothScrollToPosition(targetPosition)
                    true
                }

                else -> false
            }
        }
    }*/

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


    private fun bindObservers() {
        redirectionType.let {
            when (it) {
                Constants.AUDIO -> {
                    setAudiosObserver()
                }

                Constants.ZIP -> {
                    setZipsObserver()
                }

                Constants.APP -> {
                    setAppsObserver()
                }

                Constants.DOCUMENT -> {
                    setDocumentsObserver()
                }

                Constants.DOWNLOADS -> {
                    setDownloadsObserver()
                }

                Constants.VIDEO -> {
                    setGeneralObserver()
                }

                Constants.PICTURES -> {
                    setGeneralObserver()
                }
            }

        }


    }


    private fun setGeneralObserver() {
        lifecycleScope.launch {
            downloadViewModel.allFilesOfDirectoryOfSpecificType.collect { result ->
                hideProgressBar()
                manageListResponseData(result)
            }
        }
    }

    private fun setAppsObserver() {
        lifecycleScope.launch {
            downloadViewModel.appsFilesLiveData.collect { result ->
                hideProgressBar()
                manageListResponseData(result)
            }
        }
    }

    private fun setZipsObserver() {
        lifecycleScope.launch {
            downloadViewModel.zipsFilesLiveData.collect { result ->
                hideProgressBar()
                manageListResponseData(result)
            }
        }
    }

    private fun setAudiosObserver() {

        lifecycleScope.launch {
            downloadViewModel.audiosFilesLiveData.collect { result ->
                hideProgressBar()
                manageListResponseData(result)
            }
        }

    }

    private fun setDownloadsObserver() {
        lifecycleScope.launch {
            downloadViewModel.downloadFilesLiveData.collect { result ->
                hideProgressBar()
                manageListResponseData(result)
            }
        }
    }

    private fun setDocumentsObserver() {
        lifecycleScope.launch {
            downloadViewModel.documentsFilesLiveData.collect { result ->
                hideProgressBar()
                manageListResponseData(result)
            }
        }
    }


    private fun manageListResponseData(it: FilesResult<List<File>>) {
        when (it) {
            is FilesResult.Success -> {
                updateToolBarTitles(it)
                it.let {
                    val empty = it.data!!.isEmpty()
                    adapter.updateFiles(it.data!!)
                    binding.noDataAvailable.root.isVisible = empty
                }
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
        binding.rvList.isVisible = false
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
        binding.rvList.isVisible = true
    }

    private fun updateToolBarTitles(filesList: FilesResult<List<File>>) {
        val title = redirectionType.let {
            var titleSuffix =
                if (redirectionType.equals(Constants.GENERAL) || redirectionType.equals(Constants.PICTURES) || redirectionType.equals(
                        Constants.VIDEO
                    )
                ) {
                    File(directoryPath).name
                } else {
                    it
                }
            titleSuffix.plus(" (${filesList.data?.size})")
        }
        binding.include.collapsingToolbar.title = title
        binding.include.toolbar.title = title
    }


    private fun setToolbar() {
        redirectionType.let {
            if (it.equals(Constants.GENERAL)) {
                binding.include.collapsingToolbar.title = File(directoryPath).name
                binding.include.toolbar.title = File(directoryPath).name
            } else {
                binding.include.collapsingToolbar.title = it
                binding.include.toolbar.title = it
            }

        }
        binding.include.collapsingToolbar.isTitleEnabled = true
        binding.include.collapsingToolbar.expandedTitleGravity =
            Gravity.CENTER_HORIZONTAL
        binding.include.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedAppBar)
        binding.include.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedAppBar)
    }

    override fun onBackPressed(): Boolean {
        // Return true if back press is handled in the fragment
        return if (shouldHandleCustomBackPress()) {
            binding.include.ivCrossSearchView.performClick()
            true
        } else {
            // Let the activity handle it
            findNavController().popBackStack()
        }
    }

    private fun shouldHandleCustomBackPress(): Boolean {
        // Your custom conditions here
        return isSearchEnable
    }

}