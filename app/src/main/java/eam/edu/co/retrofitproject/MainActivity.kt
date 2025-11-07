package eam.edu.co.retrofitproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    color = Color(0xFFF5F7FA)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFF5F7FA),
                                            Color(0xFFE8EDF2)
                                        )
                                    )
                                )
                        )

                        Column (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {

                            Spacer(modifier = Modifier.height(40.dp))

                            Image(
                                painter = painterResource(id = R.drawable.pokemon_title),
                                contentDescription = "Pokemon Title",
                                modifier = Modifier
                                    .height(80.dp)
                                    .padding(bottom = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(48.dp))

                            SearchBar(onSearch = { name ->
                                viewModel.loadPokemon(name)
                            })

                            Spacer(modifier = Modifier.height(32.dp))

                            AnimatedVisibility(
                                visible = pokemonState != null,
                                enter = fadeIn(animationSpec = tween(500)) +
                                        scaleIn(animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        ))
                            ) {
                                pokemonState?.let {
                                    PokemonCard(it)
                                }
                            }

                            if (pokemonState == null) {
                                Spacer(modifier = Modifier.height(60.dp))
                                Text(
                                    "Busca un Pokémon por su nombre",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF8E9BAE),
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
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
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    "Nombre del Pokémon",
                    fontSize = 15.sp,
                    color = Color(0xFF8E9BAE)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Buscar",
                    tint = Color(0xFF8E9BAE),
                    modifier = Modifier.size(22.dp)
                )
            },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color(0xFF2C3E50),
                unfocusedTextColor = Color(0xFF2C3E50)
            ),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = { onSearch(text) },
            modifier = Modifier
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Text(
                "Buscar",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFF0F4F8),
                                Color(0xFFE8EDF2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = pokemon.sprites.other?.official_artwork?.front_default,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(12.dp))

            val tipos = pokemon.types.joinToString(", ") {
                it.type.name.replaceFirstChar { char -> char.uppercase() }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F4F8))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    "Tipo: $tipos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5A6C7D)
                )
            }
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