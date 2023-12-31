package com.capstone.foodresq.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.capstone.foodresq.R
import com.capstone.foodresq.databinding.ActivityRegisterBinding
import com.capstone.foodresq.ui.login.LoginActivity
import com.capstone.foodresq.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterActivity : AppCompatActivity() {

    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private var itemSelected : String? = null

    private val registerViewModel:RegisterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dropDown = binding.dropdownUser

        val typeUser = resources.getStringArray(R.array.type_user)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, typeUser)
        dropDown.setAdapter(arrayAdapter)
        dropDown.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, l ->
            itemSelected = adapterView.getItemAtPosition(position).toString()
        }

        onTextFieldChanged()
        setButtonRegisterEnabled()
        setLoginHandler()
        buttonRegisterHandler()

        registerViewModel.loading.observe(this){
            showLoading(it)
        }
    }

    private fun onTextFieldChanged(){
        binding.textInputEditEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnRegister.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonRegisterEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.textInputEditPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnRegister.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonRegisterEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setButtonRegisterEnabled() {
        val email = binding.textInputEditEmail.text.toString()
        val password = binding.textInputEditPassword.text.toString()
        val name=binding.textInputEditName.text.toString()

        binding.btnRegister.isEnabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && Utils.isValidEmail(email) && Utils.isValidPassword(password)
    }

    private fun buttonRegisterHandler(){
        binding.btnRegister.setOnClickListener {
            if (itemSelected == null){
                binding.dropDownTypeUser.error = "Please define your user type"
            } else{
                val name=binding.textInputEditName.text.toString()
                val email=binding.textInputEditEmail.text.toString()
                val password=binding.textInputEditPassword.text.toString()
                registerViewModel.register(name,email,password)
                registerViewModel.errorMessage.observe(this){
                    if(it!=null){
                        lifecycleScope.launch {
                            suspendCoroutine<Unit> { continuation ->
                                showAlertDialog("Error", it) {
                                    continuation.resume(Unit)
                                }
                            }
                        }
                    }
                }
                registerViewModel.successMessage.observe(this){
                    if(it!=null){
                        lifecycleScope.launch {
                            suspendCoroutine<Unit> { continuation ->
                                showAlertDialog("Success", "Your new account is already set. Let's get in!") {
                                    continuation.resume(Unit)
                                }
                            }

                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }


    private fun setLoginHandler() {
        binding.txtNavigateToLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showAlertDialog(title: String, message: String, callback: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                callback.invoke()
            }
            .setCancelable(false)
            .show()
    }

    private fun showLoading(load : Boolean){
        binding.layoutLoading.progressBar.isVisible = load == true
    }
}