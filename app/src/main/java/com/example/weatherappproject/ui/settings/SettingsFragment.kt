package com.example.weatherappproject.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappproject.R
import com.example.weatherappproject.databinding.FragmentSettingsBinding
import com.example.weatherappproject.util.MySharedPreference
import java.util.*
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        if (MySharedPreference.getLanguage() == "en")
            binding.languageRadioGroup.check(R.id.english_btn)
        else
            binding.languageRadioGroup.check(R.id.arabic_btn)



        when (MySharedPreference.getUnits()) {
            "default" -> {
                binding.tempBtn.check(R.id.kelvin_btn)
            }
            "metric" -> {
                binding.tempBtn.check(R.id.celsius_btn)
            }
            "imperial" -> {
                binding.tempBtn.check(R.id.fahrenheit_btn)
            }
        }

        binding.languageRadioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i == R.id.arabic_btn && MySharedPreference.getLanguage()=="en") {
                MySharedPreference.setLanguage("ar")
                setLocal("ar")
            } else if (i == R.id.english_btn && MySharedPreference.getLanguage()=="ar") {
                MySharedPreference.setLanguage("en")
                setLocal("en")
            }
        }
        binding.tempBtn.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (i) {
                R.id.kelvin_btn -> {
                    MySharedPreference.setUnit("default")
                }
                R.id.celsius_btn -> {
                    MySharedPreference.setUnit("metric")
                }
                R.id.fahrenheit_btn -> {
                    MySharedPreference.setUnit("imperial")
                }
            }
        }


        val root: View = binding.root
        return root
    }
    private fun setLocal(lang: String) {
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        requireActivity().baseContext.resources.updateConfiguration(
            config,
            requireActivity().baseContext.resources.displayMetrics
        )

        // Determine layout direction based on selected language
        val layoutDirection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        } else {
            View.LAYOUT_DIRECTION_LTR
        }

        // Set layout direction on the root view
        requireActivity().window.decorView.layoutDirection = layoutDirection
        this.requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}