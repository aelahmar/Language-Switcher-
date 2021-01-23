package com.aelahmar.languageswitcher

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

class LanguageSwitcherWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context): ContextWrapper {
            val language = LanguageSwitcherFragment.getSelectedLanguage(context)

            val locale = Locale(language)
            Locale.setDefault(locale)

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                updateResourcesLocale(context, locale)
            else
                updateResourcesLocaleLegacy(context, locale)
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResourcesLocale(context: Context, locale: Locale): ContextWrapper {
            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return LanguageSwitcherWrapper(context.createConfigurationContext(configuration))
        }

        @Suppress("DEPRECATION")
        private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): ContextWrapper {
            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            context.resources.updateConfiguration(configuration, resources.displayMetrics)
            return LanguageSwitcherWrapper(context)
        }
    }
}