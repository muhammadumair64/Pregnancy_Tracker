package com.example.pregnancytrackerignite.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.utils.PopupMenuCustomLayout
import com.example.pregnancytrackerignite.databinding.FragmentLoginBinding
import com.example.pregnancytrackerignite.presentation.viewModel.UserViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.iobits.rubik_cube_solver.data.utils.navigateSafe
import com.iobits.videocompressor.utils.handleBackPress
import com.iobits.videocompressor.utils.hideKeyboard
import com.iobits.videocompressor.utils.safeNavigate
import com.iobits.videocompressor.utils.showConsentDialog
import com.iobits.videocompressor.utils.showToast
import com.iobits.videocompressor.utils.singleToast


class LoginFragment : Fragment() {

    private val binding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    private val TAG = "LoginFragment"
    private var isChecked: Boolean = true
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        setPrivacyPolicyText(binding.spannableTxt)
        return binding.root
    }

    private fun initView() {
        showConsentDialog(requireActivity()){
   showPrivacy()
        }
        binding.apply {
            nameEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    nameBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    nameBg.setBackgroundResource(R.drawable.field_bg)
                }
            }
            nameEt.filters = arrayOf(InputFilter.LengthFilter(30))
            ageEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    ageBg.setBackgroundResource(R.drawable.selected_field_bg)
                } else {
                    ageBg.setBackgroundResource(R.drawable.field_bg)
                }
            }
            ageEt.filters = arrayOf(InputFilter.LengthFilter(2))
            selectorBg.setOnClickListener {
                hideKeyboard(it)
                val popupMenu = PopupMenuCustomLayout(
                    requireContext(), R.layout.popupchooserfirstchild
                ) { item ->
                    when (item) {
                        R.id.yes -> {
                            selectorText.text = "Yes"
                            selectorText.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(), R.color.black
                                )
                            )
                            checkConditionsAndUpdateButton()
                        }
                        R.id.no -> {
                            selectorText.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(), R.color.black
                                )
                            )
                            selectorText.text = "No"
                            checkConditionsAndUpdateButton()
                        }

                    }
                }
                popupMenu.showSettingDialog(upArrow)
            }

            checkbox.setOnClickListener {
                hideKeyboard(it)
                if (isChecked) {
                    Glide.with(requireContext()).load(R.drawable.unselected).into(checkbox)
                } else {
                    Glide.with(requireContext()).load(R.drawable.checked).into(checkbox)
                }
                isChecked = !isChecked
                checkConditionsAndUpdateButton()
            }

            startBtn.setOnClickListener {
                Log.d(TAG, "initView: ${nameEt.text.isNotEmpty() && ageEt.text.isNotEmpty()} _____ $isChecked")

                if (!isChecked) {
                    showToast("Accept the privacy policy to continue")
                } else {
                    Log.d(TAG, "initView: ${nameEt.text.isNotEmpty() && ageEt.text.isNotEmpty()}")
                    if (nameEt.text.isNotEmpty() && ageEt.text.isNotEmpty() ) {
                        if(ageEt.text.toString().toInt() > 14){
                            val user = CurrentUser(
                                nameEt.text.toString(),
                                ageEt.text.toString(),
                                isThisFirstChild = selectorText.text == "Yes"
                            )
                            userViewModel.addUser(user)
                            safeNavigate(
                                R.id.action_loginFragment_to_dueDateFragment,
                                R.id.loginFragment
                            )
                        }else{
                           requireContext().singleToast("Age must be greater then 14")
                        }

                    } else {
                        showToast("Fill the fields first...")
                    }
                }
            }


            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkConditionsAndUpdateButton()
                }
                override fun afterTextChanged(s: Editable?) {}
            }

            nameEt.addTextChangedListener(textWatcher)
            ageEt.addTextChangedListener(textWatcher)
        }
    }

    private fun showPrivacy() {
        val url = "https://igniteapps.blogspot.com/2024/10/privacy-policy-for-pregnancy-tracker-app.html"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun checkConditionsAndUpdateButton() {
        val nameText = binding.nameEt.text.toString()
        val ageText = binding.ageEt.text.toString()

        binding.apply {
            if (isChecked && (selectorText.text == "Yes" || selectorText.text == "No") && nameText.length > 1 && ageText.length > 1) {
                startBtn.alpha = 1f // or set the background directly if you have different drawable
                startBtn.isClickable = true
            } else {
                startBtn.alpha = 0.5f
                startBtn.isClickable = true
            }
        }
    }

    private fun setPrivacyPolicyText(textView: TextView) {
        // Get the string from resources
        val fullText = textView.context.getString(R.string.i_agree_to_the_handling_of_individual_information_in_understanding_with_the_privacy_policy)
        val spannableString = SpannableString(HtmlCompat.fromHtml(fullText, HtmlCompat.FROM_HTML_MODE_LEGACY))

        // Find the start and end of "Privacy Policy" in the string
        val privacyPolicyStart = fullText.indexOf("Privacy Policy")
        val privacyPolicyEnd = privacyPolicyStart + "Privacy Policy".length

        // Apply bold style to "Privacy Policy"
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            privacyPolicyStart,
            privacyPolicyEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply a ClickableSpan to "Privacy Policy"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
          showPrivacy()
            }
        }
        spannableString.setSpan(
            clickableSpan,
            privacyPolicyStart,
            privacyPolicyEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the text and enable movement method to make it clickable
        textView.text = spannableString
        textView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }

    override fun onResume() {
        super.onResume()
        handleBackPress {
            requireActivity().finish()
        }
    }
}
