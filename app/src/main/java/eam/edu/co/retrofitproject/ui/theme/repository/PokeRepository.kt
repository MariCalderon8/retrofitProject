package eam.edu.co.retrofitproject.ui.theme.repository

import eam.edu.co.retrofitproject.ui.theme.models.Pokemon
import eam.edu.co.retrofitproject.ui.theme.retrofit.RetrofitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PokemonRepository {
    suspend fun getPokemon(name: String): Pokemon? = withContext(Dispatchers.IO) {
        val response = RetrofitProvider.api.getPokemon(name.lowercase())
        if (response.isSuccessful) response.body() else null
    }
}