package eam.edu.co.retrofitproject.ui.theme.models

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<PokemonType>
)

data class Sprites(
    val other: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("official-artwork")
    val official_artwork: FrontDefault?
)

data class FrontDefault(
    val front_default: String?
)

data class PokemonType(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)