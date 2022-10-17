package remote.interfaces

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*

interface RemoteApi {
    val client get() = ktorClient
}

private val ktorClient = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.INFO
    }

    //These next two seem to be required when making a lot of requests
    //Number values chosen arbitrarily, perhaps there's a better way?
    install(HttpTimeout) {
        connectTimeoutMillis = 100000
    }
    engine {
        requestTimeout = 150000
    }
}