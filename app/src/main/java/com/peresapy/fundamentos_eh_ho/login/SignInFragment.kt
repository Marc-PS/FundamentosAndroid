package com.peresapy.fundamentos_eh_ho.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.peresapy.fundamentos_eh_ho.R
import com.peresapy.fundamentos_eh_ho.common.TextChangedWatcher
import com.peresapy.fundamentos_eh_ho.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSignInBinding.inflate(inflater, container, false).apply {
            labelCreateAccount.setOnClickListener {
                viewModel.moveToSignUp()
            }

            viewModel.signInData.observe(viewLifecycleOwner) {
                inputUsername.apply {
                    setText(it.userName)
                    setSelection(it.userName.length)
                }
                inputPassword.apply {
                    setText(it.password)
                    setSelection(it.password.length)
                }
            }

            viewModel.signInEnabled.observe(viewLifecycleOwner) {
                buttonLogin.isEnabled = it
            }

            viewModel.validUsername.observe(viewLifecycleOwner) {
                usernameLayout.error = null
                if (!it) usernameLayout.error = getString(R.string.error_username)
            }

            viewModel.validPassword.observe(viewLifecycleOwner) {
                passwordLayout.error = null
                if (!it) passwordLayout.error = getString(R.string.error_password)
            }

            inputUsername.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignInUserName))
            }

            inputPassword.apply {
                addTextChangedListener(TextChangedWatcher(viewModel::onNewSignInPassword))
            }

            buttonLogin.setOnClickListener {

                viewModel.signIn()
            }




        }.root
    }

    companion object {
        fun newInstance(): SignInFragment = SignInFragment()
    }
}