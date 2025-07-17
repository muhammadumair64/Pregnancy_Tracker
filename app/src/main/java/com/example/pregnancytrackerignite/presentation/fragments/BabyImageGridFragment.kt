package com.example.pregnancytrackerignite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BabyDataClass
import com.example.pregnancytrackerignite.databinding.FragmentBabyImageGridBinding
import com.example.pregnancytrackerignite.presentation.adapters.BabyImageAdapter
import com.iobits.videocompressor.utils.popBackStack
import kotlinx.coroutines.launch


class BabyImageGridFragment : Fragment() {
 val binding by lazy {
     FragmentBabyImageGridBinding.inflate(layoutInflater)
 }
    private var mList: ArrayList<BabyDataClass> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        return binding.root
    }

    fun initViews(){

        binding.backBtn.setOnClickListener {
            popBackStack()
        }

        val mAdapter = BabyImageAdapter(requireContext())
        lifecycleScope.launch {
            for (week in 1..40) {
                val imageResId = resources.getIdentifier("image$week", "drawable", context?.packageName)
                val weekString = "Week $week"
                mList.add(BabyDataClass(imageResId, weekString))
            }
            mAdapter.differ.submitList(mList)
        }
        binding.babyRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = mAdapter
        }
    }
}
