package com.example.cft_test

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class MainActivityViewModel(private val db: RecentDao): ViewModel() {
    val recent = db.getRecentList().asLiveData()
    val curCard = MutableLiveData<CardItem>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData(false)
    private val api = BinNumberApi.create()

    fun clearRecentSearch() = viewModelScope.launch(Dispatchers.IO) {
        db.deleteAll()
    }

    fun getUriOfLocationForMaps(): Uri = Uri.parse("geo:${curCard.value!!.country!!.latitude}," +
            "${curCard.value!!.country!!.longitude}?q=${curCard.value!!.country!!.name}")

    fun getCardInfoFromApi(number: String) = viewModelScope.launch(Dispatchers.IO) {
        loading.postValue(true)
        db.insert(number)

        val response = try {
            Log.d("Api",number)
            api.getCardNumberInfo(number)
        } catch (e: IOException) {
            errorMessage.postValue("Error: Internet connection is corrupted!")

            Log.e("Api", "IOException, you might not have internet connection")
            loading.postValue(false)
            return@launch
        } catch (e: HttpException) {
            errorMessage.postValue("Error: Internet connection is corrupted!")

            Log.e("Api", "HttpException, unexpected response")
            loading.postValue(false)
            return@launch
        } catch (e: Exception) {
            loading.postValue(false)
            errorMessage.postValue("Error: Some troubles on server! Try later!")
            Log.e("Api","Error: Some troubles on server! Try later!")
            Log.e("Api",e.message.toString())
            return@launch
        }

        if (response.isSuccessful && response.body() != null) {
            val card: CardItem = response.body()!!
            if (card.bank == null) card.bank = CardItem.Bank()
            if (card.brand == null) card.brand = "?"
            if (card.country == null) card.country = CardItem.Country()
            if (card.number == null) card.number = CardItem.Number()
            if (card.type == null) card.type = "?"
            if (card.prepaid == null) card.prepaid = false
            if (card.scheme == null) card.scheme ="?"
            Log.d("Api", card.toString())
            Log.d("Api", response.message())
            curCard.postValue(card)
        } else {
            Log.e("Api", "Response not successful")
            errorMessage.postValue("Such number cannot be found! Empty response of server")
        }

        loading.postValue(false)
    }

    class ViewModelFactory(private val db: RecentDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(db) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}