package eam.edu.co.retrofitproject.ui.theme.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eam.edu.co.retrofitproject.ui.theme.models.Pokemon
import eam.edu.co.retrofitproject.ui.theme.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon = _pokemon.asStateFlow()

    fun loadPokemon(name: String) {
        viewModelScope.launch {
            val result = PokemonRepository.getPokemon(name)
            _pokemon.value = result
        }
    }
}
