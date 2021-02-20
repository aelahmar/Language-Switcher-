package com.aelahmar.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aelahmar.languageswitcher.LanguageSwitcherFragment
import com.aelahmar.sampleapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initIconLanguagesList()
    }

    private fun initIconLanguagesList() {
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
}