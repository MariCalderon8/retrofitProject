package eam.edu.co.retrofitproject

import com.google.gson.GsonBuilder
import eam.edu.co.retrofitproject.ui.theme.interfaces.PokeApi
import eam.edu.co.retrofitproject.ui.theme.models.Pokemon
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isTrue

class PokeApiTest {

    private lateinit var server: MockWebServer;
    private lateinit var api: PokeApi

    @Before
    fun setUp(){
        server = MockWebServer(). apply(){ start()}

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().create()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PokeApi::class.java)
    }

    @After
    fun tearDown(){
        server.shutdown()
    }

    @Test

    fun getCorretlyPokemon(): Unit = runBlocking {
        val body = """
            {
              "id": 25,
              "name": "pikachu",
              "height": 4,
              "weight": 60,
              "sprites": {
                "other": {
                  "official-artwork": {
                    "front_default": "https://img.example/pika.png"
                  }
                }
              },
              "types": [
                { "type": { "name": "electric" } }
              ]
            }
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        )

        val response: Response<Pokemon> = api.getPokemon("pikachu")

        expectThat(response.isSuccessful).isTrue()
        val pokemon = response.body()
        expectThat(pokemon).isNotNull().and {
                get { id }.isEqualTo(25)
                get { name }.isEqualTo("pikachu")
                get { height }.isEqualTo(4)
                get { weight }.isEqualTo(60)
                get { sprites.other?.official_artwork?.front_default }.isEqualTo("https://img.example/pika.png")
                get { types[0].type.name }.isEqualTo("electric")
        }

        val recorded = server.takeRequest()
        expectThat(recorded.path).isEqualTo("/pokemon/pikachu")
    }

    @Test
    fun getIncorrectlyPokemon(): Unit = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404))

        val response = api.getPokemon("missingno")

        expectThat(response.isSuccessful).isFalse()
        expectThat(response.code()).isEqualTo(404)
        expectThat(response.body()).isNull()
    }
}