package com.aelahmar.languageswitcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.aelahmar.languageswitcher.databinding.FragmentLanguageSwitcherBinding
import java.io.Serializable

class LanguageSwitcherFragment : Fragment() {

    private lateinit var mPreferenceUtil: PreferenceUtil

    private var languagesList: MutableList<Language> = mutableListOf()

    private var _binding: FragmentLanguageSwitcherBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val SELECTED_LANGUAGE_KEY = "UserLanguageKey"
        private const val LANGUAGES_KEY = "LanguagesKey"

        fun initArEnLanguageSwitcher(languages: MutableList<Language>) =
            LanguageSwitcherFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(LANGUAGES_KEY, languages as Serializable)
                arguments = bundle
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

        mPreferenceUtil = PreferenceUtil(requireContext())

        arguments?.let {
            if (it.containsKey(LANGUAGES_KEY))
                languagesList = it.getSerializable(LANGUAGES_KEY) as MutableList<Language>
        }

        if (languagesList.isEmpty()) {
            throw IllegalArgumentException("LanguageSwitcher require not empty Languages List")
        }

        initUi()
    }

    private fun initUi() {
        val language = languagesList.first { it.stringLanguageCode == selectedLanguage }

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

        }
    }

    var selectedLanguage: String
        get() = mPreferenceUtil.getStringValue(
            SELECTED_LANGUAGE_KEY,
            languagesList[0].stringLanguageCode
        )
        set(language) = mPreferenceUtil.setStringValue(SELECTED_LANGUAGE_KEY, language)

    data class Language(
        @StringRes val stringRes: Int? = null,
        @DrawableRes val drawableRes: Int? = null,
        val stringLanguageCode: String
    ) : Serializable
}