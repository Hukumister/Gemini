package com.haroncode.gemini.sample.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import com.haroncode.gemini.sample.domain.repository.ConnectivityRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

class ConnectivityRepositoryImpl @Inject constructor(
    context: Context
) : ConnectivityRepository {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!

    override fun observeConnectionState(): Flowable<Boolean> = Flowable.create(
        { emitter ->
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) = emitter.onNext(true)
                override fun onLost(network: Network) = emitter.onNext(false)
            }

            connectivityManager.registerNetworkCallback(networkRequest, callback)
            emitter.setCancellable {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        },
        BackpressureStrategy.LATEST
    )
}
