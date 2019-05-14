import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.httpGet
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

data class IP(val origin: String)

val jsonAdapter: JsonAdapter<IP> = moshi.adapter(IP::class.java)

object IpDeserializer : ResponseDeserializable<IP> {
    override fun deserialize(content: String) = jsonAdapter.fromJson(content)
}

fun main() = runBlocking<Unit> {
    getIp().fold(
        { data -> println(data) },
        { error -> println(error) }
    )
}

suspend fun getIp() = "https://httpbin.org/ip"
    .httpGet()
    .awaitObjectResult(IpDeserializer)