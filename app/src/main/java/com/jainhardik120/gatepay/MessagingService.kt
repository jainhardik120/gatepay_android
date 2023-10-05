package com.jainhardik120.gatepay

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jainhardik120.gatepay.data.KeyValueStorage
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.UpdateTokenRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var gatepayAPI: GatepayAPI

    @Inject
    lateinit var keyValueStorage: KeyValueStorage

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        keyValueStorage.storeValue(KeyValueStorage.FIREBASE_TOKEN_KEY, token)
        keyValueStorage.getToken()?.let {
            scope.launch {
                gatepayAPI.updateToken(UpdateTokenRequest(token), it)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            it.body?.let {
                Log.d("FirebaseMessagingService", "onMessageReceived: $it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}