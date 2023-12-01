package com.jainhardik120.gatepay.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jainhardik120.gatepay.Constants
import com.jainhardik120.gatepay.data.remote.GatepayAPI
import com.jainhardik120.gatepay.data.remote.dto.AmountRequest
import com.jainhardik120.gatepay.data.remote.dto.RazorpayInfo
import com.jainhardik120.gatepay.ui.presentation.screens.login.LoadingDialog
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
            GatePayTheme(dynamicColor = false) {
                Surface(Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    LaunchedEffect(key1 = Unit, block = {
                        viewModel.uiEvent.collect {
                            if (it is UiEvent.ShowToast) {
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Enter amount to add to wallet", fontSize = 20.sp)
                        Column(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight(), verticalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = state.rechargeAmount,
                                onValueChange = viewModel::updateRechargeAmount,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                        Button(onClick = {
                            viewModel.createNewCheckoutRequest(activity)
                        }) {
                            Text(text = "Recharge Now")
                        }
                    }
                }
                if (state.loading) {
                    LoadingDialog()
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
    private val api: GatepayAPI
) : BaseViewModel() {


    private var _state = mutableStateOf(RechargeState())
    val state: State<RechargeState> = _state


    fun updateRechargeAmount(newAmount: String) {
        _state.value = _state.value.copy(rechargeAmount = newAmount)
    }


    override fun apiPreExecuting() {
        _state.value = _state.value.copy(loading = true)
    }

    override fun apiDoneExecuting() {
        _state.value = _state.value.copy(loading = false)
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
    val rechargeAmount: String = "",
    val loading: Boolean = false
)