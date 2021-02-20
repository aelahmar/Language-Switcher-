package com.aelahmar.languageswitcher

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aelahmar.languageswitcher.databinding.FragmentLanguageSwitcherBinding
import java.io.Serializable

class LanguageSwitcherFragment : Fragment() {

    private var _binding: FragmentLanguageSwitcherBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val SELECTED_LANGUAGE_KEY = "UserLanguageKey"
        private const val LANGUAGES_KEY = "LanguagesKey"
        private const val LIST_KEY = "ListKey"

        private var languagesList: MutableList<Language> = mutableListOf()

        fun initArEnLanguageSwitcher(languages: MutableList<Language>) =
            LanguageSwitcherFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(LANGUAGES_KEY, languages as Serializable)
                arguments = bundle
            }

        fun initListLanguageSwitcher(languages: MutableList<Language>) =
            LanguageSwitcherFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(LANGUAGES_KEY, languages as Serializable)
                bundle.putBoolean(LIST_KEY, true)
                arguments = bundle
            }

        fun getSelectedLanguage(context: Context): String {
            PreferenceUtil(context).apply {
                val language = if (languagesList.isEmpty()) {
                    "ar"
                } else {
                    languagesList[0].stringLanguageCode
                }

                return getStringValue(SELECTED_LANGUAGE_KEY, language)
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

        var isList = false

        arguments?.let {
            if (it.containsKey(LANGUAGES_KEY))
                languagesList = it.getSerializable(LANGUAGES_KEY) as MutableList<Language>

            if (it.containsKey(LIST_KEY))
                isList = it.getBoolean(LIST_KEY)
        }

        if (languagesList.isEmpty() || languagesList.size > 2) {
            throw IllegalArgumentException("LanguageSwitcher require not empty Languages List and support only 2 languages now :'( ")
        }

        if (isList)
            initListUi()
        else
            initIconTextUi()
    }

    private fun initListUi() {
        val adapter =
            LanguagesAdapter(languagesList, object : LanguagesAdapter.LanguagesAdapterCallback {
                override fun onLanguageSelected(position: Int, language: Language) {
                    showAlertDialogButtonClicked { _, _ ->
                        setSelectedLanguage(requireContext(), language.stringLanguageCode)

                        restartActivity()
                    }

                }
            })

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.languagesRecyclerView.layoutManager = linearLayoutManager
        binding.languagesRecyclerView.adapter = adapter
    }

    private fun initIconTextUi() {
        val language =
            languagesList.first { it.stringLanguageCode == getSelectedLanguage(requireContext()) }

        val index = languagesList.indexOf(language)
        val viewLanguage = if (index == 0) {
            languagesList[index + 1]
        } else {
            languagesList[index - 1]
        }

        viewLanguage.drawableRes?.let {
            binding.selectedLanguageIcon.setImageResource(viewLanguage.drawableRes)
        }

        viewLanguage.stringRes?.let {
            binding.selectedLanguageString.text = getString(it)
        }

        if (viewLanguage.drawableRes == null && viewLanguage.stringRes == null) {
            throw IllegalArgumentException("Language require drawableRes or stringRes")
        }

        if (viewLanguage.drawableRes != null && viewLanguage.stringRes != null) {
            binding.selectedLanguageString.visibility = View.INVISIBLE
        }

        binding.languageSwitcherContent.setOnClickListener {
            showAlertDialogButtonClicked { _, _ ->
                val selectedLanguage = if (index == 0) {
                    languagesList[index + 1].stringLanguageCode
                } else {
                    languagesList[index - 1].stringLanguageCode
                }

                setSelectedLanguage(requireContext(), selectedLanguage)

                restartActivity()
            }
        }
    }

    private fun restartActivity() {
        val intent = activity?.intent
        activity?.finish()
        activity?.startActivity(intent)
    }

    private fun showAlertDialogButtonClicked(listener: DialogInterface.OnClickListener) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_app_langugae))
        builder.setMessage(getString(R.string.change_langugae_content))

        builder.setPositiveButton(getString(R.string.yes), listener)

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