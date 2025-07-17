package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.FragmentTrimesterChartBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.visible


class TrimesterChartFragment : Fragment() {

val binding by lazy {
    FragmentTrimesterChartBinding.inflate(layoutInflater)
}
    private val sharedViewModel : SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      initViews()
        return binding.root
    }

    fun initViews(){
        trimesterChartSetup(sharedViewModel.weeks)

        binding.backBtn.setOnClickListener {
          popBackStack()
        }
        binding.descriptionTxt.text = Html.fromHtml(getString(R.string.trimester_chart_description), Html.FROM_HTML_MODE_LEGACY)
    }

    private fun trimesterChartSetup(week: Int) {
        val selectors = binding.trimesterChart.run {
            listOf(
                selector0, selector1, selector2, selector3, selector4, selector5, selector6, selector7,
                selector8, selector9, selector10, selector11, selector12, selector13, selector14,
                selector15, selector16, selector17, selector18, selector19, selector20, selector21,
                selector22, selector23, selector24, selector25, selector26, selector27, selector28,
                selector29, selector30, selector31, selector32, selector33, selector34, selector35,
                selector36, selector37, selector38, selector39, selector40
            )
        }
        selectors.forEach { it.gone() }
        if (week in 0..40) {
            selectors[week].visible()
        }
    }
}