package com.peresapy.fundamentos_eh_ho.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.peresapy.fundamentos_eh_ho.common.TextChangedWatcher
import com.peresapy.fundamentos_eh_ho.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSignUpBinding.inflate(inflater, container, false).apply {
            labelSignIn.setOnClickListener {
                viewModel.moveToSignIn()
            }

            viewModel.signUpData.observe(viewLifecycleOwner) {
                inputEmail.apply {
                    setText(it.email)
                    setSelection(it.email.length)
                }
                inputUsername.apply {
                    setText(it.userName)
                    setSelection(it.userName.length)
                }
                inputPassword.apply {
                    setText(it.password)
                    setSelection(it.password.length)
                }
                inputConfirmPassword.apply {
                    setText(it.confirmPassword)
                    setSelection(it.confirmPassword.length)
                }
            }

            viewModel.signUpEnabled.observe(viewLifecycleOwner) {
                buttonSignUp.isEnabled = it
            }
            inputEmail.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignUpEmail))
            }
            inputUsername.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignUpUserName))
            }
            inputPassword.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignUpPassword))
            }
            inputConfirmPassword.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignUpConfirmPassword))
            }

            buttonSignUp.setOnClickListener {
                println("JcLog: clicking signup button")
                viewModel.signUp()
            }

        }.root
    }

    companion object {
        fun newInstance(): SignUpFragment = SignUpFragment()
    }
}