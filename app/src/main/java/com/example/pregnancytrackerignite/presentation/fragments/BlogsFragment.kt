package com.example.pregnancytrackerignite.presentation.fragments

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.MainBlogsClass
import com.example.pregnancytrackerignite.data.models.MainBlogModel
import com.example.pregnancytrackerignite.databinding.FragmentBlogsBinding
import com.example.pregnancytrackerignite.presentation.adapters.BlogsListHorizontalAdapter
import com.example.pregnancytrackerignite.presentation.adapters.BlogsListVerticleAdapter
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.NavEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.changeStatusBarColor
import com.iobits.videocompressor.utils.popBackStack
import com.iobits.videocompressor.utils.safeNavigate
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import java.io.IOException
import java.io.InputStream

class BlogsFragment : Fragment() {

    private val binding by lazy {
        FragmentBlogsBinding.inflate(layoutInflater)
    }
    val viewModel: SharedViewModel by activityViewModels()
    private var mBlogsList: ArrayList<MainBlogModel> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusBarColor(requireActivity(),R.color.white)
        initViews()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        changeStatusBarColor(requireActivity(),R.color.status_bar_color)
    }

    private fun initViews() {
        val blogsData = getBlogsData()
        mBlogsList = blogsData?.blogs as ArrayList<MainBlogModel>
        val uniqueTypes = mutableSetOf<String>()
        val filteredHorizontalList = ArrayList<MainBlogModel>()

        for (blog in mBlogsList) {
            if (uniqueTypes.add(blog.Type)) {
                filteredHorizontalList.add(blog)
            }
        }

        binding.backBtn.setOnClickListener { popBackStack() }
        // Set up the horizontal RecyclerView with the filtered list
        binding.horizontalRv.adapter = BlogsListHorizontalAdapter(requireContext(), filteredHorizontalList) { blogsData ->
          viewModel.selectedBlogItem = blogsData
          safeNavigate(R.id.action_blogsFragment_to_blogDetailsFragment,R.id.blogsFragment)
        }
        binding.horizontalRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Set up the vertical RecyclerView with the original list
        binding.verticalRv.adapter = BlogsListVerticleAdapter(requireContext(), mBlogsList) { blogsData ->
            viewModel.selectedBlogItem = blogsData
            safeNavigate(R.id.action_blogsFragment_to_blogDetailsFragment,R.id.blogsFragment)
        }
        binding.verticalRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun getBlogsData(): MainBlogsClass? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val json = getJsonFromAssets(requireContext(), "main_blogs_recycler.json")
        Log.d(tag, "FILE IS $json")

        return try {
            val jsonAdapter = moshi.adapter(MainBlogsClass::class.java)
            jsonAdapter.fromJson(json)
        } catch (e: JsonDataException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        }  catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
