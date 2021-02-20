package com.aelahmar.languageswitcher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aelahmar.languageswitcher.LanguageSwitcherFragment.Companion.getSelectedLanguage

internal class LanguagesAdapter(
    private val languages: MutableList<LanguageSwitcherFragment.Language>,
    private val callback: LanguagesAdapterCallback
) : RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.language_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = languages[position]
        val selectedLanguage = getSelectedLanguage(context)

        language.stringRes?.let {

            if (selectedLanguage == language.stringLanguageCode)
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.blue))
            else
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.gray))

            holder.title.setText(language.stringRes)
            holder.title.visibility = View.VISIBLE
        }

        language.drawableRes?.let {
            holder.icon.setImageResource(language.drawableRes)
            holder.icon.visibility = View.VISIBLE
        }

        holder.parentView.setOnClickListener {
            if (selectedLanguage != language.stringLanguageCode)
                callback.onLanguageSelected(position, language)
        }
    }

    override fun getItemCount() = languages.size

    class ViewHolder(val parentView: View) : RecyclerView.ViewHolder(parentView) {
        var title: AppCompatTextView = parentView.findViewById(R.id.item_language_string)
        var icon: AppCompatImageView = parentView.findViewById(R.id.item_language_icon)
    }

    interface LanguagesAdapterCallback {
        fun onLanguageSelected(position: Int, language: LanguageSwitcherFragment.Language)
    }
}