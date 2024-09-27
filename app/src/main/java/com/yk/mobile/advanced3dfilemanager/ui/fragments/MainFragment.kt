package com.yk.mobile.advanced3dfilemanager.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yk.mobile.advanced3dfilemanager.R
import com.yk.mobile.advanced3dfilemanager.databinding.FragmentMainBinding
import com.yk.mobile.advanced3dfilemanager.viewmodel.FileSizeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import soup.neumorphism.NeumorphButton
import utility.Constants

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val fileSizeViewModel: FileSizeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nvInternal.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.GENERAL )
                putString(Constants.DIRECTORY_PATH, Environment.getExternalStorageDirectory().path)
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_imagesFragments,bundle)
        })
        binding.llImages.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.PICTURES )
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_imagesFragments,bundle)
        })
        binding.llAudio.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.AUDIO )
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_allFragments,bundle)
        })
        binding.llVideo.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.VIDEO )
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_imagesFragments,bundle)
        })
        binding.llZip.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.ZIP )
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_allFragments,bundle)
        })
        binding.llApps.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.APP )
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_allFragments,bundle)

        })
        binding.llDocuments.setOnClickListener({
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.DOCUMENT )
            }
            it.findNavController().navigate(R.id.action_mainFragment_to_allFragments,bundle)

        })
        binding.llDownloads.setOnClickListener({
            // Create a Bundle to pass data
            val bundle = Bundle().apply {
                putString(Constants.REDIRECTION_TYPE,Constants.DOWNLOADS )
            }

            it.findNavController().navigate(R.id.action_mainFragment_to_allFragments,bundle)
        })


        // Collect and observe the StateFlow
        addCollectors()
        // Trigger the file size calculation
        fileSizeViewModel.getTotalAndAvailableInternalMemorySize()
        fileSizeViewModel.getTotalAndAvailableSdMemorySize()
        fileSizeViewModel.getInternalStorageFilesSize()
        fileSizeViewModel.getSdStorageFilesSize()
        fileSizeViewModel.getDownLoadsFilesSize()

    }

    private fun addCollectors() {
        // Collecting the combined result
        lifecycleScope.launch {
            combine(
                fileSizeViewModel.internalImageSize,
                fileSizeViewModel.sdImageSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (internalImageSize, sdImageSize) ->
                // Handle the combined result
                binding.tvImagesSize.text =
                    Formatter.formatFileSize(requireActivity(), internalImageSize + sdImageSize)
            }
        }

        lifecycleScope.launch {
            combine(
                fileSizeViewModel.internalVideoSize,
                fileSizeViewModel.sdVideoSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (internalVideoSize, sdVideoSize) ->
                // Handle the combined result
                binding.tvVideosSize.text =
                    Formatter.formatFileSize(requireActivity(), internalVideoSize + sdVideoSize)
            }
        }


        lifecycleScope.launch {
            combine(
                fileSizeViewModel.internalAudioSize,
                fileSizeViewModel.sdAudioSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (internalAudioSize, sdAudioSize) ->
                // Handle the combined result
                binding.tvAudioSize.text =
                    Formatter.formatFileSize(requireActivity(), internalAudioSize + sdAudioSize)
            }
        }

        lifecycleScope.launch {
            combine(
                fileSizeViewModel.internalZipsSize,
                fileSizeViewModel.sdZipsSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (internalZipsSize, sdZipsSize) ->
                // Handle the combined result
                binding.tvZipsSize.text =
                    Formatter.formatFileSize(requireActivity(), internalZipsSize + sdZipsSize)
            }
        }

        lifecycleScope.launch {
            combine(
                fileSizeViewModel.internalAppsSize,
                fileSizeViewModel.sdAppsSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (internalAppsSize, sdAppsSize) ->
                // Handle the combined result
                binding.tvAppsSize.text =
                    Formatter.formatFileSize(requireActivity(), internalAppsSize + sdAppsSize)
            }
        }
        lifecycleScope.launch {
            combine(
                fileSizeViewModel.internalDocumentSize,
                fileSizeViewModel.sdDocumentSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (internalDocumentSize, sdDocumentSize) ->
                // Handle the combined result
                binding.tvDocumentsSize.text =
                    Formatter.formatFileSize(
                        requireActivity(),
                        internalDocumentSize + sdDocumentSize
                    )
            }
        }

        lifecycleScope.launch {
            fileSizeViewModel.internalDownloadSize.collect { internalDownloadSize ->
                binding.tvDownloadsSize.text =
                    Formatter.formatFileSize(requireActivity(), internalDownloadSize)
            }
        }

        lifecycleScope.launch {
            combine(
                fileSizeViewModel.totalAvailableInternalSize,
                fileSizeViewModel.totalInternalSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (totalAvailableInternalSize, totalInternalSize) ->
                // Handle the combined result
                binding.tvInternalUsed.text =
                    Formatter.formatFileSize(
                        requireActivity(),
                        (totalInternalSize - totalAvailableInternalSize)
                    )
                binding.tvInternalTotal.text =
                    Formatter.formatFileSize(requireActivity(), totalInternalSize)
                // Calculate used memory percentage as Float
                val usedMemoryPercentage: Float =
                    ((totalInternalSize - totalAvailableInternalSize).toFloat() / totalInternalSize.toFloat()) * 100
                val colors = mutableListOf(
                    ContextCompat.getColor(requireActivity(), R.color.red),
                    ContextCompat.getColor(requireActivity(), R.color.transparent)
                )
                setupPieChart(usedMemoryPercentage, binding.pieChart, binding.mBgPieChart, colors)
            }
        }

        lifecycleScope.launch {
            fileSizeViewModel.isSdCardExist.collect { isSdCardExist ->
                binding.nvSdMemory.visibility = (isSdCardExist ?: false).let {
                    if (it) View.VISIBLE else View.GONE
                }
            }
        }

        lifecycleScope.launch {
            combine(
                fileSizeViewModel.totalAvailableSdSize,
                fileSizeViewModel.totalSdSize
            ) { value1, value2 ->
                // Combine both values (value1 from flow1 and value2 from flow2)
                Pair(value1, value2)
            }.collect { (totalAvailableSdSize, totalSdSize) ->
                // Handle the combined result
                binding.tvSdUsed.text =
                    Formatter.formatFileSize(
                        requireActivity(),
                        (totalSdSize - totalAvailableSdSize)
                    )
                binding.tvSdTotal.text =
                    Formatter.formatFileSize(requireActivity(), totalAvailableSdSize)
                // Calculate used memory percentage as Float
                val usedSDMemoryPercentage: Float =
                    ((totalSdSize - totalAvailableSdSize).toFloat() / totalSdSize.toFloat()) * 100
                val colors = mutableListOf(
                    ContextCompat.getColor(requireActivity(), R.color.yellow),
                    ContextCompat.getColor(requireActivity(), R.color.transparent)
                )
                setupPieChart(
                    usedSDMemoryPercentage,
                    binding.pieChartSd,
                    binding.mBgPieChartSd,
                    colors
                )
            }
        }

    }

    private fun setupPieChart(
        usedMemory: Float,
        pieChart: PieChart,
        btnView: NeumorphButton,
        colors: MutableList<Int>
    ) {
        btnView.setShapeType(2)
        val colorStateList = ColorStateList(
            arrayOf<IntArray>(intArrayOf(-android.R.attr.state_pressed)),
            intArrayOf(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.screen_color_white_theme
                )
            )
        )
        btnView.setStrokeWidth(24f)
        btnView.setStrokeColor(colorStateList)

        // Create data entries for PieChart
        val entries = listOf(
            PieEntry(usedMemory, ""),
            PieEntry((100f - usedMemory), ""),
        )

        pieChart.setUsePercentValues(false)
        pieChart.description = null
        pieChart.setDrawSliceText(false)
        pieChart.setTouchEnabled(false)
        pieChart.isRotationEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setNoDataTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black_80
            )
        )
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.rotationAngle = 0f
        pieChart.holeRadius = 90f
        pieChart.setCenterTextSize(14f)
        (String.format("%.2f", usedMemory) + " %")
            .also { pieChart.centerText = it }
        pieChart.setCenterTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        pieChart.setCenterTextColor(colors[0])
        pieChart.transparentCircleRadius = 70f
        pieChart.setTransparentCircleAlpha(50)
        pieChart.legend.isEnabled = false
        pieChart.highlightValues(null)
        // Create a PieDataSet with the entries
        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.sliceSpace = 0f
        pieDataSet.selectionShift = 5f
        pieDataSet.setDrawValues(false)
        pieDataSet.colors = (colors)
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(11f)
        pieData.setValueTextColor(Color.WHITE)
        pieChart.data = pieData
        pieChart.animateY(1000)
        pieChart.invalidate() // Redraw chart with data
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

}