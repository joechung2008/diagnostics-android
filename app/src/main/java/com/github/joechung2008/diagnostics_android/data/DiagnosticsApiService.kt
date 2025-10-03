package com.github.joechung2008.diagnostics_android.data

import com.github.joechung2008.diagnostics_android.model.Diagnostics
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface DiagnosticsApiService {
    @GET
    suspend fun getDiagnostics(@Url url: String): Diagnostics
}

object DiagnosticsApi {
    private const val PUBLIC_URL = "https://hosting.portal.azure.net/api/diagnostics"
    private const val FAIRFAX_URL = "https://hosting.azureportal.usgovcloudapi.net/api/diagnostics"
    private const val MOONCAKE_URL = "https://hosting.azureportal.chinacloudapi.cn/api/diagnostics"

    fun getUrlForEnvironment(environment: com.github.joechung2008.diagnostics_android.model.Environment): String {
        return when (environment) {
            com.github.joechung2008.diagnostics_android.model.Environment.PUBLIC -> PUBLIC_URL
            com.github.joechung2008.diagnostics_android.model.Environment.FAIRFAX -> FAIRFAX_URL
            com.github.joechung2008.diagnostics_android.model.Environment.MOONCAKE -> MOONCAKE_URL
        }
    }

    // The polymorphic serializer module has been removed.
    // Deserialization is now handled by the @Serializable(with = NullableExtensionSerializer::class) annotation on the Extension interface.
    private val json = Json {
        ignoreUnknownKeys = true // To handle dynamic keys and API evolution
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://hosting.portal.azure.net/") // Base URL is required, but we use @Url for dynamic URLs
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val service: DiagnosticsApiService = retrofit.create(DiagnosticsApiService::class.java)
}