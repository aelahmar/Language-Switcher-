package com.aelahmar.sampleapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aelahmar.languageswitcher.LanguageSwitcherFragment
import com.aelahmar.languageswitcher.LanguageSwitcherWrapper
import com.aelahmar.sampleapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initIconLanguages()
        initTextLanguages()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageSwitcherWrapper.wrap(base))
    }

    private fun initIconLanguages() {
        val languages: MutableList<LanguageSwitcherFragment.Language> = mutableListOf()

        languages.add(
                LanguageSwitcherFragment.Language(
                        drawableRes = R.drawable.ic_sa,
                        stringLanguageCode = "ar"
                )
        )

        languages.add(
                LanguageSwitcherFragment.Language(
                        drawableRes = R.drawable.ic_gb,
                        stringLanguageCode = "en"
                )
        )

        val fragment = LanguageSwitcherFragment.initArEnLanguageSwitcher(languages)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.icons_language_layout, fragment, "fragmentIconTag")
                .commit()
    }

    private fun initTextLanguages() {
        val languages: MutableList<LanguageSwitcherFragment.Language> = mutableListOf()

        languages.add(
                LanguageSwitcherFragment.Language(
                        stringRes = R.string.arabic,
                        stringLanguageCode = "ar"
                )
        )

        languages.add(
                LanguageSwitcherFragment.Language(
                        stringRes = R.string.english,
                        stringLanguageCode = "en"
                )
        )

        val fragment = LanguageSwitcherFragment.initArEnLanguageSwitcher(languages)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.text_language_layout, fragment, "fragmentIconTag")
                .commit()
    }
}