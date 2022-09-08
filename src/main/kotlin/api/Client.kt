package api

import general.API_KEY
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*

val client = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.INFO
    }

    install(ContentEncoding) {
        gzip(1.0F)
    }

    //These next two seem to be required when making a lot of requests
    //Number values chosen arbitrarily, perhaps there's a better way?
    install(HttpTimeout) {
        connectTimeoutMillis = 100000
    }
    engine {
        requestTimeout = 150000
    }

    defaultRequest {
        header("Cookie", API_KEY)
    }
}

