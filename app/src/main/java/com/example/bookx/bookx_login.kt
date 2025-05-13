package com.example.bookx

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivityBookxLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class bookx_login : AppCompatActivity() {

    private lateinit var binding: ActivityBookxLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private var currentVerificationId: String? = null
    private var currentResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var currentPhoneNumber: String? = null

    private var countDownTimer: CountDownTimer? = null
    private val OTP_TIMEOUT_SECONDS = 60L

    private lateinit var otpEditTexts: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Redirect if already logged in
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityBookxLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otpEditTexts = arrayOf(
            binding.otpDigit1, binding.otpDigit2, binding.otpDigit3,
            binding.otpDigit4, binding.otpDigit5, binding.otpDigit6
        )
        setupOtpInputListeners()

        val storedPhoneNumber = getPhoneNumberFromPreferences()
        if (!storedPhoneNumber.isNullOrEmpty()) {
            binding.editTextNumber2.setText(storedPhoneNumber)
        }

        showPhoneNumberLayout()

        binding.loginbtn.setOnClickListener {
            val phoneNumber = binding.editTextNumber2.text.toString().trim()
            if (phoneNumber.isNotEmpty() && phoneNumber.startsWith("+") && phoneNumber.length > 10) {
                currentPhoneNumber = phoneNumber
                savePhoneNumberToPreferences(phoneNumber)
                showLoading(true, "Sending OTP...")
                sendOtp(phoneNumber)
            } else {
                Toast.makeText(this, "Please enter a valid phone number with country code (e.g., +1234567890)", Toast.LENGTH_LONG).show()
            }
        }

        binding.verifyotp.setOnClickListener {
            val otpValue = otpEditTexts.joinToString("") { it.text.toString() }
            if (otpValue.length == 6) {
                currentVerificationId?.let { verificationId ->
                    showLoading(true, "Verifying OTP...")
                    val credential = PhoneAuthProvider.getCredential(verificationId, otpValue)
                    signInWithPhoneAuthCredential(credential)
                } ?: run {
                    Toast.makeText(this, "Verification process not started. Please request OTP first.", Toast.LENGTH_SHORT).show()
                    showPhoneNumberLayout()
                }
            } else {
                Toast.makeText(this, "Please enter the complete 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.resendOtpText.setOnClickListener {
            currentPhoneNumber?.let { phoneNumber ->
                currentResendToken?.let { token ->
                    showLoading(true, "Resending OTP...")
                    resendOtp(phoneNumber, token)
                } ?: run {
                    Log.w("bookx_login", "Resend token is null, attempting fresh OTP send.")
                    sendOtp(phoneNumber)
                }
            } ?: run {
                Toast.makeText(this, "Phone number not available for resend.", Toast.LENGTH_SHORT).show()
                showPhoneNumberLayout()
            }
        }

        binding.changeNumberText.setOnClickListener {
            showPhoneNumberLayout()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupOtpInputListeners() {
        for (i in otpEditTexts.indices) {
            otpEditTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpEditTexts.size - 1) {
                        otpEditTexts[i + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            otpEditTexts[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (otpEditTexts[i].text.isEmpty() && i > 0) {
                        otpEditTexts[i - 1].requestFocus()
                    }
                }
                false
            }
        }
    }

    private fun showPhoneNumberLayout() {
        binding.textView8.visibility = View.VISIBLE
        binding.editTextNumber2.visibility = View.VISIBLE
        binding.editTextNumber2.text.clear()
        binding.editTextNumber2.requestFocus()
        binding.phoneButtonLayout.visibility = View.VISIBLE

        binding.otp.visibility = View.GONE
        binding.otpDigitsLayout.visibility = View.GONE
        binding.timerText.visibility = View.GONE
        binding.resendOtpText.visibility = View.GONE
        binding.verifyotp.visibility = View.GONE
        binding.changeNumberText.visibility = View.GONE

        binding.progressBarLayout.visibility = View.GONE
        stopOtpTimer()
    }

    private fun showOtpLayout() {
        binding.textView8.visibility = View.GONE
        binding.editTextNumber2.visibility = View.GONE
        binding.phoneButtonLayout.visibility = View.GONE

        binding.otp.visibility = View.VISIBLE
        binding.otpDigitsLayout.visibility = View.VISIBLE
        otpEditTexts.forEach { it.text.clear() }
        otpEditTexts[0].requestFocus()
        binding.timerText.visibility = View.VISIBLE
        binding.resendOtpText.visibility = View.VISIBLE
        binding.verifyotp.visibility = View.VISIBLE
        binding.changeNumberText.visibility = View.VISIBLE

        binding.progressBarLayout.visibility = View.GONE
        startOtpTimer()
    }

    private fun showLoading(isLoading: Boolean, message: String = "Loading...") {
        if (isLoading) {
            binding.progressBarLayout.visibility = View.VISIBLE
            binding.progressMessage.text = message
            binding.loginbtn.isEnabled = false
            binding.verifyotp.isEnabled = false
            binding.resendOtpText.isEnabled = false
            binding.changeNumberText.isEnabled = false
        } else {
            binding.progressBarLayout.visibility = View.GONE
            binding.loginbtn.isEnabled = true
            binding.verifyotp.isEnabled = true
            binding.resendOtpText.isEnabled = binding.timerText.text != "Resend OTP in 0s"
            binding.changeNumberText.isEnabled = true
        }
    }

    private fun startOtpTimer() {
        binding.resendOtpText.isEnabled = false
        binding.timerText.visibility = View.VISIBLE

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(OTP_TIMEOUT_SECONDS * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.timerText.text = "Resend OTP in ${secondsRemaining}s"
            }

            override fun onFinish() {
                binding.timerText.text = "Didn't receive code?"
                binding.resendOtpText.isEnabled = true
                Toast.makeText(this@bookx_login, "OTP timeout. You can now resend.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun stopOtpTimer() {
        countDownTimer?.cancel()
        binding.timerText.visibility = View.GONE
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            showLoading(false)
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            showLoading(false)
            val errorMessage = when {
                e is FirebaseAuthInvalidCredentialsException -> "Invalid phone number. Please check it."
                e.message?.contains("TOO_MANY_REQUESTS", ignoreCase = true) == true -> "Too many requests. Try again later."
                else -> "Verification failed. Please try again."
            }
            Toast.makeText(this@bookx_login, errorMessage, Toast.LENGTH_LONG).show()
            showPhoneNumberLayout()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            showLoading(false)
            currentVerificationId = verificationId
            currentResendToken = token
            currentPhoneNumber?.let {
                Toast.makeText(this@bookx_login, "OTP sent to $it", Toast.LENGTH_SHORT).show()
            }
            showOtpLayout()
        }
    }

    private fun sendOtp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(OTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendOtp(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(OTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Toast.makeText(this, "Resending OTP to $phoneNumber", Toast.LENGTH_SHORT).show()
        startOtpTimer()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, bookx_signup::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        otpEditTexts.forEach { it.text.clear() }
                        otpEditTexts[0].requestFocus()
                        "Invalid OTP. Please try again."
                    } else {
                        "Authentication failed: ${task.exception?.message ?: "Unknown error"}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ✅ Prevent crash if binding is not initialized
        if (this::binding.isInitialized) {
            stopOtpTimer()
        }
    }

    private fun savePhoneNumberToPreferences(phoneNumber: String) {
        val sharedPreferences = getSharedPreferences("bookx_preferences", MODE_PRIVATE)
        sharedPreferences.edit().putString("user_phone_number", phoneNumber).apply()
    }

    private fun getPhoneNumberFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("bookx_preferences", MODE_PRIVATE)
        return sharedPreferences.getString("user_phone_number", null)
    }
}
