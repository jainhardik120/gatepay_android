package com.jainhardik120.gatepay.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.jainhardik120.gatepay.Constants
import com.jainhardik120.gatepay.data.remote.AuthApi
import com.jainhardik120.gatepay.data.remote.dto.AmountRequest
import com.jainhardik120.gatepay.data.remote.dto.RazorpayInfo
import com.jainhardik120.gatepay.ui.theme.GatePayTheme
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class RechargeActivity : ComponentActivity(), PaymentResultWithDataListener {

    private val viewModel: RechargeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity: Activity = this
        setContent {
            val state = viewModel.state.value
            GatePayTheme {
                Surface {
                    val context = LocalContext.current
                    LaunchedEffect(key1 = Unit, block = {
                        viewModel.uiEvent.collect {
                            when (it) {
                                is UiEvent.ShowToast -> {
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }

                                else -> {

                                }
                            }
                        }
                    })
                    Column {
                        OutlinedTextField(
                            value = state.rechargeAmount,
                            onValueChange = viewModel::updateRechargeAmount
                        )
                        Button(onClick = {
                            viewModel.createNewCheckoutRequest(activity)
                        }) {
                            Text(text = "Recharge Now")
                        }
                    }
                }
            }
        }
        Checkout.preload(applicationContext)
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        p1?.let {
            viewModel.processCompletedPayment(it)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {

    }
}

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val api: AuthApi
) : BaseViewModel() {


    private var _state = mutableStateOf(RechargeState())
    val state: State<RechargeState> = _state


    fun updateRechargeAmount(newAmount: String) {
        _state.value = _state.value.copy(rechargeAmount = newAmount)
    }

    fun createNewCheckoutRequest(activity: Activity) {
        makeApiCall({
            api.checkout(AmountRequest(_state.value.rechargeAmount.toDouble()))
        }) {
            val co = Checkout()
            co.setKeyID(Constants.RAZORPAY_KEY)
            val options = JSONObject(
                mapOf(
                    Pair("key", Constants.RAZORPAY_KEY),
                    Pair("amount", String.format("%.2f", it.amount).replace(".", "")),
                    Pair("current", "INR"),
                    Pair("name", "Gatepay"),
                    Pair("order_id", it.orderId),
                )
            )
            co.open(activity, options)
        }
    }

    fun processCompletedPayment(paymentData: PaymentData) {
        makeApiCall({
            api.verifyPayment(
                RazorpayInfo(
                    paymentData.orderId,
                    paymentData.paymentId,
                    paymentData.signature
                )
            )
        }) {
            sendUiEvent(UiEvent.ShowToast("Updated Balance is INR. ${it.balance}"))
        }
    }
}

data class RechargeState(
    val rechargeAmount: String = ""
)