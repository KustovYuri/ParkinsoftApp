package com.farma.parkinsoftapp.di

import com.farma.parkinsoftapp.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import io.ktor.client.plugins.logging.*
import io.ktor.http.HttpHeaders
import io.ktor.network.tls.CIOCipherSuites
import io.ktor.network.tls.addKeyStore
import java.net.InetSocketAddress
import java.net.Proxy

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideKtorHttpClient(): HttpClient {
       return HttpClient(CIO) {
           engine {
               maxConnectionsCount = 1000
               endpoint {
                   maxConnectionsPerRoute = 100
                   pipelineMaxSize = 20
                   keepAliveTime = 5000
                   connectTimeout = 5000
                   connectAttempts = 5
               }
               https {
                   serverName = "api.ktor.io"
                   cipherSuites = CIOCipherSuites.SupportedSuites
               }
           }
           install(Logging) {
               logger = Logger.DEFAULT
               level = LogLevel.HEADERS
               filter { request ->
                   request.url.host.contains("ktor.io")
               }
               sanitizeHeader { header -> header == HttpHeaders.Authorization }
           }
       }
    }
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .readTimeout(3000, TimeUnit.SECONDS)
            .connectTimeout(3000, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.10:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}