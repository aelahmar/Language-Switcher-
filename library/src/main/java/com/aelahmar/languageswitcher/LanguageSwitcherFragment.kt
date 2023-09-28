package com.aelahmar.languageswitcher

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aelahmar.languageswitcher.databinding.FragmentLanguageSwitcherBinding
import java.io.Serializable

class LanguageSwitcherFragment : Fragment() {

    private var _binding: FragmentLanguageSwitcherBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding not initialized")

    private val languagesKey = "LanguagesKey"
    private val listKey = "ListKey"

    private var languagesList: ArrayList<Language> = arrayListOf()

    companion object {
        private const val selectedLanguageKey = "UserLanguageKey"

        fun initArEnLanguageSwitcher(languages: ArrayList<Language>) =
            LanguageSwitcherFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(languagesKey, languages)
                arguments = bundle
            }

        fun initListLanguageSwitcher(languages: ArrayList<Language>) =
            LanguageSwitcherFragment().apply {
                val bundle = Bundle()
                bundle.putSerializable(languagesKey, languages)
                bundle.putBoolean(listKey, true)
                arguments = bundle
            }

        fun getSelectedLanguage(context: Context, defaultLanguage: String): String {
            return PreferenceUtil(context).getStringValue(selectedLanguageKey, defaultLanguage)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageSwitcherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            languagesList = it.getCompatSerializable(languagesKey) ?: arrayListOf()
            val isList = it.getBoolean(listKey, false)

            validateLanguagesList()

            if (isList) initListUi() else initIconTextUi()
        }
    }

    private fun validateLanguagesList() {
        if (languagesList.isEmpty() || languagesList.size > 2) {
            throw IllegalArgumentException("LanguageSwitcher requires a non-empty Languages List and supports only 2 languages currently.")
        }
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
        val language = languagesList.first { language ->
            language.stringLanguageCode == getSelectedLanguage(
                requireContext(),
                languagesList[0].stringLanguageCode,
            )
        }

        val index = languagesList.indexOf(language)
        val viewLanguage = if (index == 0) {
            languagesList[1]
        } else {
            languagesList[index - 1]
        }

        viewLanguage.drawableRes?.let {
            binding.selectedLanguageIcon.setImageResource(it)
        }

        viewLanguage.stringRes?.let {
            binding.selectedLanguageString.text = getString(it)
        }

        if (viewLanguage.drawableRes == null && viewLanguage.stringRes == null) {
            throw IllegalArgumentException("Language requires drawableRes or stringRes")
        }

        if (viewLanguage.drawableRes != null && viewLanguage.stringRes != null) {
            binding.selectedLanguageString.visibility = View.INVISIBLE
        }

        binding.selectedLanguageString.setTextColor(
            ContextCompat.getColor(
                requireContext(), viewLanguage.selectedLanguageColorRes
            )
        )

        binding.languageSwitcherContent.setOnClickListener {
            showAlertDialogButtonClicked { _, _ ->
                val selectedLanguage = if (index == 0) {
                    languagesList[1].stringLanguageCode
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
        PreferenceUtil(context).setStringValue(selectedLanguageKey, language)
    }

    data class Language(
        @StringRes val stringRes: Int? = null,
        @DrawableRes val drawableRes: Int? = null,
        @ColorRes val selectedLanguageColorRes: Int = R.color.ls_selected_language_color,
        val stringLanguageCode: String
    ) : Serializable
}
