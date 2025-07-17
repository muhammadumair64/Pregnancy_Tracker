package com.example.pregnancytrackerignite.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import com.iobits.videocompressor.utils.changeStatusBarColor
import com.iobits.videocompressor.utils.showTipsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController :NavController by lazy {
        Navigation.findNavController(this,R.id.activity_main_nav_host_fragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(this, R.color.status_bar_color)
        val viewModel : SharedViewModel by viewModels()
        setContentView(R.layout.activity_main)
        getSharedPreferences("DialogPrefs", Context.MODE_PRIVATE).edit().putBoolean("isTipsDialogShown", false).apply()

        viewModel.visitCount.observe(this) { count ->
            if (count == 3) {
                lifecycleScope.launch {
                    showTipsDialog(viewModel.vBlogsList, viewModel) {
                        try {
                            navController.navigate(R.id.toBlogDetailsFragment)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}