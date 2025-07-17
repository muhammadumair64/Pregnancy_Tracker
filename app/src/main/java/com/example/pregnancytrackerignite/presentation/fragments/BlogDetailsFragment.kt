package com.example.pregnancytrackerignite.presentation.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.DetailsBlogModel
import com.example.pregnancytrackerignite.data.models.DetailsBlogsClass
import com.example.pregnancytrackerignite.data.models.MainBlogModel
import com.example.pregnancytrackerignite.databinding.FragmentBlogDetailsBinding
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.changeStatusBarColor
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

class BlogDetailsFragment : Fragment() {
    private val binding by lazy {
        FragmentBlogDetailsBinding.inflate(layoutInflater)
    }
    private val viewModelForData: SharedViewModel by activityViewModels()
    private var mId = "01"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusBarColor(requireActivity(), R.color.white)
        binding.apply {
            // Retrieve the selected blog ID
            mId = viewModelForData.selectedBlogItem?.id ?: "01"
            headers.apply {
                backBtn.setOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }

        // Load blog details
        loadBlogDetails()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        changeStatusBarColor(requireActivity(),R.color.status_bar_color)
    }
    private fun loadBlogDetails() {
        val blogDetails = getBlogsData()
        blogDetails?.blogs?.forEach {
            if (it.id == mId){
                initViews(it)
            }
        }
    }

    private fun initViews(it: DetailsBlogModel?) {
        if (it != null) {

            binding.date.text = it.Date
            binding.authorName.text = it.Author

            binding.titleMain.text = it.Title
            Glide.with(requireContext())
                .load(it.image)
                .placeholder(R.drawable.large_ph)
                .into(binding.imageMain)
            val fullText = it.para
            val (firstPart, secondPart) = splitText(fullText, 160)
            binding.para0.text = firstPart
            binding.para02.text = secondPart

            binding.title1.text = it.Title1
            Glide.with(requireContext()).load(it.image1)
                .placeholder(R.drawable.large_ph)
                .into(binding.image1)
            val fullText1 = it.para1
            val (firstPart1, secondPart1) = splitText(fullText1, 160)
            binding.para1.text = firstPart1
            binding.para12.text = secondPart1


            binding.title2.text = it.Title2
            Glide.with(requireContext()).load(it.image2)
                .placeholder(R.drawable.large_ph)
                .into(binding.image2)
            binding.para2.text = it.para2

            binding.title3.text = it.Title3
            Glide.with(requireContext()).load(it.image3)
                .placeholder(R.drawable.large_ph)
                .into(binding.image3)
            binding.para3.text = it.para3
        }
    }

    fun splitText(input: String, limit: Int): Pair<String, String> {
        if (input.length <= limit) {
            return Pair(input, "")
        }
        val truncated = input.substring(0, limit)
        val lastSentenceEnd = truncated.lastIndexOfAny(charArrayOf('.', '!', '?'))

        return if (lastSentenceEnd != -1) {
            Pair(input.substring(0, lastSentenceEnd + 1), input.substring(lastSentenceEnd + 1).trim())
        } else {
            Pair(truncated, input.substring(limit).trim())
        }
    }

    fun resizePlaceholder(context: Context, resId: Int, width: Int, height: Int): BitmapDrawable {
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDrawable(context.resources, resizedBitmap)
    }


    private fun getBlogsData(): DetailsBlogsClass? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // Add this line
            .build()
        val json = getJsonFromAssets(requireContext(), "blogs_details.json")
        Log.d(tag, "FILE IS $json")
        return try {
            val type: Type = Types.newParameterizedType(DetailsBlogsClass::class.java, MainBlogModel::class.java)
            val jsonAdapter = moshi.adapter<DetailsBlogsClass>(type)
            jsonAdapter.fromJson(json)
        } catch (e: JsonDataException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
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
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
