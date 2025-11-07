package eam.edu.co.retrofitproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import eam.edu.co.retrofitproject.ui.theme.RetrofitProjectTheme
import eam.edu.co.retrofitproject.ui.theme.models.Pokemon
import eam.edu.co.retrofitproject.ui.theme.viewModels.PokemonViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitProjectTheme {
                val pokemonState by viewModel.pokemon.collectAsStateWithLifecycle()

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.pokemon_title),
                            contentDescription = "Pokemon Title",
                            modifier = Modifier
                                .height(100.dp)
                                .padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        SearchBar(onSearch = { name ->
                            viewModel.loadPokemon(name)
                        })

                        Spacer(modifier = Modifier.height(24.dp))

                        pokemonState?.let {
                            PokemonCard(it)
                        } ?: Text("Busca un Pokémon por su nombre")
                    }
                }

            }
        }
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Nombre del Pokémon") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onSearch(text) }) {
            Text("Buscar")
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.sprites.other?.official_artwork?.front_default,
                contentDescription = pokemon.name,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pokemon.name.uppercase(),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            val tipos = pokemon.types.joinToString(", ") { it.type.name.capitalize() }
            Text("Tipo: $tipos")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitProjectTheme {
        Greeting("Android")
    }
}