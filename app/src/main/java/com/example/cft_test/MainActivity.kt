package com.example.cft_test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cft_test.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), RecentWordsAdapter.Listener {
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.ViewModelFactory((application as AppInit).database.getRecentDao())
    }
    private lateinit var binding: ActivityMainBinding

    override fun onClick(word: String) {
        binding.searchField.setQuery(word,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recentWordsAdapter = RecentWordsAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recentRv.adapter = recentWordsAdapter
        binding.recentRv.layoutManager = linearLayoutManager

        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("search","onQueryTextSubmit: " + query as String)
                viewModel.getCardInfoFromApi(query)
                return false
            }
        })

        binding.clearBtn.setOnClickListener {
            viewModel.clearRecentSearch()
            Toast.makeText(this, "List of recent requests was cleared!", Toast.LENGTH_LONG).show()
        }

        binding.countryData.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, viewModel.getUriOfLocationForMaps()))
        }

        viewModel.recent.observe(this) {
            Log.d("db", it.toString())
            recentWordsAdapter.reload(it)
            binding.recentRv.scrollToPosition(0)
        }
        viewModel.loading.observe(this) {
            binding.loading.isVisible = it
            binding.loadingBack.isVisible = it
        }

        viewModel.errorMessage.observe(this) {
            setUnknownMode()
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        }

        viewModel.curCard.observe(this) {
            binding.schemeData.text = it.scheme
            binding.brandData.text = it.brand
            binding.cardLengthData.text = it.number?.length.toString()
            if (it.number?.luhn != null) {
                if (it.number?.luhn!!) binding.cardLuhnData.text = "Yes"
                else binding.cardLuhnData.text = "No"
            }
            binding.typeData.text = it.type
            if (it?.prepaid != null) {
                if (it.prepaid!!) binding.prepaidData.text = "Yes"
                else binding.prepaidData.text = "No"
            }
            val str = it.country!!.emoji + " " + it.country!!.name
            binding.countryData.text = str
            binding.countryLatitudeData.text = it.country!!.latitude.toString()
            binding.countryLongitudeData.text = it.country!!.longitude.toString()
            binding.bankNameData.text = it.bank!!.name
            binding.bankCityData.text = it.bank!!.city
            binding.bankPhoneData.text = it.bank!!.phone
            binding.bankUrlData.text = it.bank!!.url
        }
    }

    private fun setUnknownMode() {
        binding.schemeData.text = "?"
        binding.brandData.text = "?"
        binding.cardLengthData.text = "?"
        binding.cardLuhnData.text = "?"
        binding.typeData.text = "?"
        binding.prepaidData.text = "?"
        binding.countryData.text = "?"
        binding.countryLatitudeData.text = "?"
        binding.countryLongitudeData.text = "?"
        binding.bankNameData.text = "?"
        binding.bankCityData.text = "?"
        binding.bankPhoneData.text = "?"
        binding.bankUrlData.text = "?"
    }
}