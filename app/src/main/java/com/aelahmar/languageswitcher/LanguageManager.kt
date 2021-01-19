package com.aelahmar.languageswitcher

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.badeea.balligni.R
import com.badeea.balligni.app.data.Session

internal class LanguageManager {
    companion object {

        fun changeUserLanguage(activity: Activity) {
            val mPreferenceUtil = PreferenceUtil(activity)

            val language = mPreferenceUtil.getStringValue(
                LanguageSwitcherFragment.SELECTED_LANGUAGE_KEY,
                "ar"
            )

            val currentLanguage = getCurrentLanguage(session.userLanguage)

            val myItems = getLanguagesLit(activity)

            MaterialDialog(activity).show {
                listItems(
                    items = myItems,
                    waitForPositiveButton = false,
                    disabledIndices = arrayOf(currentLanguage).toIntArray()
                ) { dialog, index, _ ->
                    dialog.dismiss()
                    onLanguagePicked(index, session, activity)
                }
                title(R.string.language)
                negativeButton(R.string.cancel)
                cancelable(false)
                cancelOnTouchOutside(false)
                noAutoDismiss()
            }
        }

        fun onLanguagePicked(
            index: Int,
            session: Session,
            activity: Activity,
            returnLanguagePage: Boolean = false
        ) {
            val currentLanguage = getCurrentLanguage(session.userLanguage)

            if (index != currentLanguage) {

                val language = when (index) {
                    Language.ARABIC.index -> {
                        Language.ARABIC.code
                    }
                    Language.ENGLISH.index -> {
                        Language.ENGLISH.code
                    }
                    else -> {
                        ""
                    }
                }
                session.userLanguage = language

                restartActivity(activity, returnLanguagePage)
            }
        }

        fun getLanguagesLit(activity: Activity): List<CharSequence> {
            val arabic: CharSequence = activity.getString(R.string.arabic)
            val english: CharSequence = activity.getString(R.string.english)

            return listOf(arabic, english)
        }
    }
}