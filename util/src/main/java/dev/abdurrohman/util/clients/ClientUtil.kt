package dev.abdurrohman.util.clients

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ClientUtil {
    companion object {
        inline fun <reified T> createService(url: String, isDebug: Boolean): T {
            val okHttpClient by lazy {
                OkHttpClient().newBuilder().apply {
                    retryOnConnectionFailure(true)

                    connectTimeout(1, TimeUnit.MINUTES)
                    writeTimeout(1, TimeUnit.MINUTES)
                    readTimeout(1, TimeUnit.MINUTES)
                    callTimeout(1, TimeUnit.MINUTES)

                    addInterceptor {
                        return@addInterceptor it.proceed(it.request().newBuilder().apply {
                            addHeader("Content-Type", "application/json;charset=UTF-8")
                            addHeader("Accept", "application/json;charset=UTF-8")
                        }.build())
                    }

                    if (isDebug) {
                        addInterceptor(HttpLoggingInterceptor { message ->
                            Log.e("API-LOG", message)
                        }.apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }.build()
            }

            val retrofit by lazy {
                Retrofit.Builder().apply {
                    baseUrl(url)
                    client(okHttpClient)
                    addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                }.build()
            }

            return retrofit.create(T::class.java)
        }
    }
}