package com.iobits.videocompressor.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfRenderer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.MainBlogModel
import com.example.pregnancytrackerignite.data.models.Time
import com.example.pregnancytrackerignite.data.utils.PregnancyTips
import com.example.pregnancytrackerignite.presentation.viewModel.ClickEvents
import com.example.pregnancytrackerignite.presentation.viewModel.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random


fun Fragment.getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
    val currentDateTime = Date()
    return dateFormat.format(currentDateTime)
}

fun Fragment.getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
    val currentDateTime = Date()
    return dateFormat.format(currentDateTime)
}

fun Fragment.getCurrentDateInMM(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDateTime = Date()
    return dateFormat.format(currentDateTime)
}

fun Fragment.getCurrentTime(): String {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val currentTime = Date()
    return timeFormat.format(currentTime)
}

// Extension function to convert dp to pixels
fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun String.toTime(): Time? {
    return try {
        // Parse the string into a Date object using a 12-hour format
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = formatter.parse(this)

        // Extract hour and minute in 24-hour format
        date?.let {
            val calendar = java.util.Calendar.getInstance().apply { time = it }
            val hour = get(java.util.Calendar.HOUR_OF_DAY)
            val minute = get(java.util.Calendar.MINUTE)
            Time(hour.toInt(), minute.toInt())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null // Return null if parsing fails
    }
}

fun getRandomNumber(): Int {
    return Random.nextInt(0, 1_000) // Generates a random number from 0 to 999999
}

fun Fragment.getCurrentMonthNumber(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MONTH) // Adding 1 because months are zero-based
}

fun Date.formatToCustomString(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    return sdf.format(this)
}

fun String.toFormattedDate(
    inputFormat: String = "EEEE, dd MMMM yyyy",
    outputFormat: String = "MMM dd, yyyy",
    locale: Locale = Locale.ENGLISH
): String? {
    return try {
        val parser = SimpleDateFormat(inputFormat, locale)
        val formatter = SimpleDateFormat(outputFormat, locale)
        val date: Date? = parser.parse(this)
        date?.let { formatter.format(it) }
    } catch (e: Exception) {
        null
    }
}

fun Activity.changeStatusBarColor(activity: Activity, colorId: Int) {
    try {
        val window: Window = activity.window
        // activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(activity, colorId)
    } catch (e: Exception) {
        Log.d("STATUS_BAR", "changeStatusBarColor: ${e.localizedMessage}")
    }
}

fun Fragment.changeStatusBarColor(activity: Activity, colorId: Int) {
    val window: Window = activity.window
// Set the system UI visibility flags to indicate light status bar icons
    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    window.statusBarColor = ContextCompat.getColor(activity, colorId)
}

fun Long.formatFileSize(): String {
    val kiloBytes = this / 1024.0
    val megaBytes = kiloBytes / 1024.0
    val gigaBytes = megaBytes / 1024.0

    return when {
        gigaBytes >= 1 -> String.format("%.2f GB", gigaBytes)
        megaBytes >= 1 -> String.format("%.2f MB", megaBytes)
        kiloBytes >= 1 -> String.format("%.2f KB", kiloBytes)
        else -> String.format("%d B", this)
    }
}

fun String.getFileSize(): String {
    val file = File(this)
    return if (file.exists()) {
        val fileSizeInBytes = file.length()
        fileSizeInBytes.formatFileSize()
    } else {
        "File not found"
    }
}

fun String.getFileSizeInLong(): Long {
    val file = File(this)
    return if (file.exists()) {
        val fileSizeInBytes = file.length()
        fileSizeInBytes
    } else {
        0
    }
}

fun String.getFileNameFromPath(): String {
    return File(this).name
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ByteArray.toBitmap(): Bitmap? {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

fun View.gone() {
    visibility = View.GONE
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun EditText.textWatcher(onTextChanged: (String?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            onTextChanged(s)
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                onTextChanged(s.toString())
            } ?: onTextChanged(null)
        }
    })
}

fun Fragment.disableMultipleClicking(view: View, delay: Long = 750) {
    view.isEnabled = false
    this.lifecycleScope.launch {
        delay(delay)
        view.isEnabled = true
    }
}

fun AppCompatActivity.disableMultipleClicking(view: View, delay: Long = 750) {
    view.isEnabled = false
    this.lifecycleScope.launch {
        delay(delay)
        view.isEnabled = true
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {

        override fun onChanged(value: T) {
            observer.onChanged(value)
            removeObserver(this)
        }
    })
}

fun Fragment.handleBackPress(onBackPressed: () -> Unit) {
    var lastBackPressedTime = 0L  // Variable to store the last back button press time

    requireView().isFocusableInTouchMode = true
    requireView().requestFocus()
    requireView().setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackPressedTime > 1000) {  // Check if more than 2 seconds have passed
                lastBackPressedTime = currentTime
                onBackPressed() // Call the provided callback function
            }
            true
        } else false
    }
}

fun Fragment.navigateTo(actionId: Int, destinationName: Int) {
    findNavController().navigate(
        actionId, null, NavOptions.Builder().setPopUpTo(destinationName, true).build()
    )
}

fun Fragment.clearBackStack(destinationId: Int, inclusive: Boolean = false) {
    try {
        findNavController().popBackStack(destinationId, inclusive)
    } catch (e: IllegalArgumentException) {
        Log.e("CLEAR_BACKSTACK_ERROR", "Error clearing back stack: ${e.localizedMessage}")
    }
}

fun Fragment.popBackStack() {
    try {
        findNavController().navigateUp()
    } catch (e: IllegalArgumentException) {
        Log.e("CLEAR_BACKSTACK_ERROR", "Error clearing back stack: ${e.localizedMessage}")
    }
}

fun Fragment.safeNavigate(actionId: Int, currentFragmentId: Int) {
    try {
        if (findNavController().currentDestination?.id == currentFragmentId) {
            findNavController().navigate(
                actionId
            )
        } else {
            Log.d("TAG", "navigateSafe: ")
        }
    } catch (e: Exception) {
        Log.d("SAFE_NAV_ERROR", "safeNavigateError:${e.localizedMessage} ")
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
fun Fragment.showToast(string: String) {
    Toast.makeText(this.requireContext(), string, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLongToast(string: String) {
    Toast.makeText(this.requireContext(), string, Toast.LENGTH_LONG).apply {
        cancel() // Cancel any previous toast
        show()
    }
}

fun Fragment.showKeyboard(view: View?) {
    view?.let {
        val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(it, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}

fun Fragment.hideKeyboard(view: View?): Boolean {
    val inputMethodManager =
        view?.context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
    return inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0) ?: false
}

fun Activity.convertSecondsToString(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun Fragment.closeKeyboard(activity: Activity) {
    val view = activity.currentFocus ?: View(activity) // Get the currently focused view or fallback to a new view
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.convertToSeconds(minutes: Int, seconds: Int): Int {
    return (minutes * 60) + seconds
}

fun Context.showEmailChooser(supportEmail: String, subject: String, body: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        val chooser = Intent.createChooser(intent, "Send Email")
        if (chooser.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show()
        }
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.handleLastBackPress(func: () -> Unit) {
    // This callback will only be called when MyFragment is at least Started.
    val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                func.invoke()
            }
        }
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
}

private fun Fragment.openAppSettingsStorage() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", requireContext().packageName, null)
    intent.data = uri
    requireContext().startActivity(intent)
}

fun ViewModel.isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        } else {
            return true
        }
    }
    return result
}

var toast: Toast? = null

fun Context.singleToast(msg: String) {
    if (toast != null) toast?.cancel()
    toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast?.show()
}

@SuppressLint("MissingInflatedId")
fun Fragment.showMoreDialog(context: Activity, description: String) {
    try {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = context.layoutInflater.inflate(R.layout.dilog_show_more_excercise, null)
        val cancel = dialogView?.findViewById<ImageView>(R.id.close)
        val textMoreAboutExercise = dialogView?.findViewById<TextView>(R.id.textMoreAboutExercise)
        textMoreAboutExercise?.text = description

        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cancel?.setOnClickListener {
            alertDialog?.dismiss()
        }
    } catch (e: Exception) {
        e.localizedMessage
    }
}

@SuppressLint("MissingInflatedId")
fun Fragment.showPredictionDialog(
    context: Activity,
    gender: String,
    solvedQuestions: Int,
    totalQuestions: Int,
    onSkip: () -> Unit
) {
    try {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = context.layoutInflater.inflate(R.layout.item_prediction_dialog, null)
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val cancel = dialogView?.findViewById<ImageView>(R.id.close)
        val nextBtn = dialogView?.findViewById<RelativeLayout>(R.id.start_btn)
        val skipBtn = dialogView?.findViewById<RelativeLayout>(R.id.skip_btn)
        val progressText = dialogView?.findViewById<TextView>(R.id.progress_text)
        val titleText = dialogView?.findViewById<TextView>(R.id.titleMain)
        val desText = dialogView?.findViewById<TextView>(R.id.para)

        val progressBar =
            dialogView.findViewById<com.mikhaellopez.circularprogressbar.CircularProgressBar>(R.id.progress_prediction)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        progressBar.progressMax = totalQuestions.toFloat()
        progressBar.progress = solvedQuestions.toFloat()
        val mDiv = (solvedQuestions.div(totalQuestions.toFloat())).toFloat()
        progressText?.text = "${(mDiv * 100).toInt()}%"

        desText?.text = "Based on ${solvedQuestions} answers, we predict it’s a boy! For \n a more accurate result, please answer all \n questions."
        if (gender == "Boy") {
            titleText?.text = "We Predict Boy"
        } else {
            titleText?.text = "We Predict Girl"
        }

        cancel?.setOnClickListener {
            alertDialog?.dismiss()
        }
        nextBtn?.setOnClickListener {
            alertDialog?.dismiss()
        }
        skipBtn?.setOnClickListener {
            alertDialog?.dismiss()
            onSkip.invoke()
        }
    } catch (e: Exception) {
        e.localizedMessage
    }
}



@SuppressLint("MissingInflatedId")
fun Fragment.showConsentDialog(
    context: Activity,
    onPrivacyClick: () -> Unit
) {
    try {
        val consents = booleanArrayOf(true, true, true, true)

        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = context.layoutInflater.inflate(R.layout.item_consent_dialog, null)
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val nextBtn = dialogView?.findViewById<RelativeLayout>(R.id.start_btn)
        val privacyText = dialogView?.findViewById<TextView>(R.id.spannable_txt)
        val checkboxes = listOf(
            dialogView?.findViewById<ImageView>(R.id.checkbox1),
            dialogView?.findViewById<ImageView>(R.id.checkbox2),
            dialogView?.findViewById<ImageView>(R.id.checkbox3),
            dialogView?.findViewById<ImageView>(R.id.checkbox4)
        )



        checkboxes.forEachIndexed { index, checkbox ->
            checkbox?.setOnClickListener {
                consents[index] = !consents[index]
                checkbox.setImageResource(
                    if (consents[index]) R.drawable.checked else R.drawable.unselected
                )
                updateNextButtonState(nextBtn,consents)
            }
        }

        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }

        // Prevent dismiss on back press
        alertDialog.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
        }

        privacyText?.setOnClickListener { onPrivacyClick.invoke() }

        nextBtn?.setOnClickListener {
            if (consents.all { it }) {
                alertDialog.dismiss()
            } else {
                requireActivity().singleToast("Please accept all terms and conditions")
            }
        }
    } catch (e: Exception) {
        Log.d("CONSENT_DIALOG", "showConsentDialog: ${e.localizedMessage} ")
    }
}


fun updateNextButtonState(nextBtn: RelativeLayout?, consents: BooleanArray) {
    nextBtn?.alpha = if (consents.all { it }) 1f else 0.5f
}

//@SuppressLint("InflateParams")
//fun Fragment.showTipsDialog() {
//    try {
//        val context = requireActivity()
//
//        // Create the dialog with the custom animation theme
//        val dialogBuilder = AlertDialog.Builder(context)
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_tips, null)
//
//        val cancel = dialogView.findViewById<ImageView>(R.id.cross)
//        val title = dialogView.findViewById<TextView>(R.id.title)
//        val descriptionText = dialogView.findViewById<TextView>(R.id.description_txt)
//
//        val alertDialog = dialogBuilder.setView(dialogView).create()
//
//        // Set the tip content
//        val tip = PregnancyTips.tips[Random.nextInt(PregnancyTips.tips.size)]
//        title.text = tip.title
//        descriptionText.text = tip.description
//
//        cancel.setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//
//        // Adjust dialog position to the top-right corner
//        val window = alertDialog.window ?: return
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val layoutParams = window.attributes
//        layoutParams.gravity = Gravity.TOP or Gravity.END
//        layoutParams.x = 50 // Adjust horizontal margin
//        layoutParams.y = 100 // Adjust vertical margin
//        window.attributes = layoutParams
//
//        // Disable background dimming
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//
//        // Allow interaction with UI elements behind the dialog
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//        )
//
//        // Prevent dialog from dismissing when clicking outside
//        alertDialog.setCanceledOnTouchOutside(false)
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}

@SuppressLint("InflateParams")
fun Activity.showTipsDialog(mList: ArrayList<MainBlogModel>, viewModel: SharedViewModel, onTap: () -> Unit) {
    val sharedPreferences = this.getSharedPreferences("DialogPrefs", Context.MODE_PRIVATE)
    val isDialogShown = sharedPreferences.getBoolean("isTipsDialogShown", false)

    if (isDialogShown) {
        return // Dialog is already showing, do not show again
    }
    val mBlogsList = mList.sortedBy { it.id }

    try {
        val context = this
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_tips, null)

        val cancel = dialogView.findViewById<ImageView>(R.id.cross)
        val mainCard = dialogView.findViewById<androidx.cardview.widget.CardView>(R.id.main_card)
        val questionIcon = dialogView.findViewById<ImageView>(R.id.imageView26)
        val title = dialogView.findViewById<TextView>(R.id.title)
        val descriptionText = dialogView.findViewById<TextView>(R.id.description_txt)

        val alertDialog = dialogBuilder.setView(dialogView).create()

        val tip = PregnancyTips.tips[Random.nextInt(PregnancyTips.tips.size)]
        title.text = tip.title
        descriptionText.text = tip.description

        cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()

        val window = alertDialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.TOP or Gravity.END
        layoutParams.x = 20
        layoutParams.y = 100
        window.attributes = layoutParams

        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )

        mainCard.setOnClickListener {
            alertDialog.dismiss()
            viewModel.selectedBlogItem = mBlogsList[tip.blogNumber - 1]
//            viewModel.clickCallBack?.invoke(ClickEvents.ClickBlog)
            onTap.invoke()
        }

        alertDialog.setCanceledOnTouchOutside(false)

        val slideInAnimation = ObjectAnimator.ofFloat(dialogView, "translationX", dialogView.width.toFloat() + 500, 0f)
        slideInAnimation.interpolator = DecelerateInterpolator()
        slideInAnimation.duration = 700
        slideInAnimation.start()

        questionIcon.startRolyPolyAnimationWithPauseAtNeutral()

        sharedPreferences.edit().putBoolean("isTipsDialogShown", true).apply()

        // Prevent dismiss on back press
        alertDialog.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
        }

        alertDialog.setOnDismissListener {
            sharedPreferences.edit().putBoolean("isTipsDialogShown", false).apply()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}



fun View.startRolyPolyAnimationWithPauseAtNeutral() {
    this.doOnLayout {
        // Set the pivot to the bottom of the View
        this.pivotX = this.width / 2f  // Center horizontally
        this.pivotY = this.height.toFloat()  // Bottom edge vertically

        // Create the pendulum animation
        val rotatePendulum = ValueAnimator.ofFloat(-25f, 25f) // Rotate back and forth
        rotatePendulum.duration = 100 // Duration for a single swing
        rotatePendulum.repeatCount = ValueAnimator.INFINITE // Infinite repetition
        rotatePendulum.repeatMode = ValueAnimator.REVERSE // Smooth back-and-forth motion

        // Track the number of iterations
        var iterationCount = 0
        val iterationsBeforePause = 5
        val pauseDuration = 2000L // Pause duration in milliseconds

        rotatePendulum.addUpdateListener { animation ->
            this.rotation = animation.animatedValue as Float
        }

        rotatePendulum.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {}

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {
                iterationCount++
                if (iterationCount >= iterationsBeforePause) {
                    iterationCount = 0 // Reset iteration count

                    // Pause the pendulum and move to neutral
                    rotatePendulum.pause()

                    // Smoothly animate the view to the neutral position (0°)
                    val toNeutralAnimator = ValueAnimator.ofFloat(this@startRolyPolyAnimationWithPauseAtNeutral.rotation, 0f)
                    toNeutralAnimator.duration = 300 // Time to reach neutral
                    toNeutralAnimator.addUpdateListener { anim ->
                        this@startRolyPolyAnimationWithPauseAtNeutral.rotation = anim.animatedValue as Float
                    }
                    toNeutralAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}

                        override fun onAnimationEnd(animation: Animator) {
                            // After reaching neutral, resume the pendulum animation after the pause
                            this@startRolyPolyAnimationWithPauseAtNeutral.postDelayed({
                                rotatePendulum.resume()
                            }, pauseDuration)
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}
                    })

                    // Start the neutral animation
                    toNeutralAnimator.start()
                }
            }
        })

        // Start the pendulum animation
        rotatePendulum.start()
    }
}



fun Fragment.renderPdfPageToImageView(pdfFile: File, pageIndex: Int, imageView: ImageView) {
    imageView.visible()
    // Open the file descriptor
    val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)

    // Create a PdfRenderer instance
    val pdfRenderer = PdfRenderer(fileDescriptor)

    // Check if the page index is within bounds
    if (pageIndex < 0 || pageIndex >= pdfRenderer.pageCount) {
        pdfRenderer.close()
        fileDescriptor.close()
        return
    }

    // Open the specified page
    val page = pdfRenderer.openPage(pageIndex)

    // Create a bitmap to hold the page image
    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)

    // Render the page to the bitmap
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

    // Set the bitmap to the ImageView
    imageView.setImageBitmap(bitmap)
    imageView.visible()

    // Close the page and renderer
    page.close()
    pdfRenderer.close()
    fileDescriptor.close()
}











