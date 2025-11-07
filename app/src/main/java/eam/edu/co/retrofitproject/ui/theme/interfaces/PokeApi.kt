package eam.edu.co.retrofitproject.ui.theme.interfaces

import eam.edu.co.retrofitproject.ui.theme.models.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface PokeApi {
    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): Response<Pokemon>
}