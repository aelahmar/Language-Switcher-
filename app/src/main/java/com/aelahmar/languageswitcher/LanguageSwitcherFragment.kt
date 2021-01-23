package com.aelahmar.languageswitcher

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aelahmar.languageswitcher.databinding.FragmentLanguageSwitcherBinding
import java.io.Serializable

class LanguageSwitcherFragment : Fragment() {

    private var _binding: FragmentLanguageSwitcherBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val SELECTED_LANGUAGE_KEY = "UserLanguageKey"
        private const val LANGUAGES_KEY = "LanguagesKey"

        private var languagesList: MutableList<Language> = mutableListOf()

        fun initArEnLanguageSwitcher(languages: MutableList<Language>) =
                LanguageSwitcherFragment().apply {
                    val bundle = Bundle()
                    bundle.putSerializable(LANGUAGES_KEY, languages as Serializable)
                    arguments = bundle
                }

        fun getSelectedLanguage(context: Context): String {
            PreferenceUtil(context).apply {
                return getStringValue(SELECTED_LANGUAGE_KEY, languagesList[0].stringLanguageCode)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        inflater.inflate(R.layout.fragment_language_switcher, container, false)
        _binding = FragmentLanguageSwitcherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if (it.containsKey(LANGUAGES_KEY))
                languagesList = it.getSerializable(LANGUAGES_KEY) as MutableList<Language>
        }

        if (languagesList.isEmpty() || languagesList.size > 2) {
            throw IllegalArgumentException("LanguageSwitcher require not empty Languages List and support only 2 languages now :'( ")
        }

        initUi()
    }

    private fun initUi() {
        val language = languagesList.first { it.stringLanguageCode == getSelectedLanguage(requireContext()) }

        language.drawableRes?.let {
            binding.selectedLanguageIcon.setImageResource(language.drawableRes)
        }

        language.stringRes?.let {
            binding.selectedLanguageString.text = getString(it)
        }

        if (language.drawableRes == null && language.stringRes == null) {
            throw IllegalArgumentException("Language require drawableRes or stringRes")
        }

        binding.selectedLanguageIcon.setOnClickListener {
            showAlertDialogButtonClicked(language)
        }
    }

    private fun restartActivity() {
        val intent = activity?.intent
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun showAlertDialogButtonClicked(language: Language) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_app_langugae))
        builder.setMessage(getString(R.string.change_langugae_content))

        builder.setPositiveButton(getString(R.string.yes)) { p0, p1 ->
            val index = languagesList.indexOf(language)
            val selectedLanguage = if (index == 0) {
                languagesList[index + 1].stringLanguageCode
            } else {
                languagesList[index - 1].stringLanguageCode
            }

            setSelectedLanguage(requireContext(), selectedLanguage)

            restartActivity()
        }

        builder.setNegativeButton(getString(R.string.cancel), null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun setSelectedLanguage(context: Context, language: String) {
        PreferenceUtil(context).apply {
            setStringValue(SELECTED_LANGUAGE_KEY, language)
        }
    }

    data class Language(
            @StringRes val stringRes: Int? = null,
            @DrawableRes val drawableRes: Int? = null,
            val stringLanguageCode: String
    ) : Serializable
}