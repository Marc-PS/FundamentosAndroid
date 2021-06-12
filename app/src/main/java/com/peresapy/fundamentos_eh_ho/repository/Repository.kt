package com.peresapy.fundamentos_eh_ho.repository


import com.peresapy.fundamentos_eh_ho.model.LogIn
import com.peresapy.fundamentos_eh_ho.model.Details
import com.peresapy.fundamentos_eh_ho.model.Topic
import com.peresapy.fundamentos_eh_ho.network.Client

class Repository(private val client: Client) {
    private var userName: String = ""

    fun signIn(
        userName: String,
        password: String,
        callback: Callback<LogIn>
    ) {
        client.signIn(userName, password) {
            storeLogIn(it)
            callback.onResult(it)
        }
    }

    fun signup(
        username: String,
        email: String,
        password: String,
        callback: Callback<LogIn>,
    ) {
        client.signUp(username, email, password) {
            callback.onResult(it)
        }
    }

    fun getTopics(callback: Callback<Result<List<Topic>>>) {
        client.getTopics {
            callback.onResult(it)
        }
    }

    fun getDetail(topic: Topic, callback: Callback<Result<List<Details>>>) {
        client.getDetails(topic) {
            callback.onResult(it)
        }
    }

    private fun storeLogIn(logIn: LogIn) {
        userName = (logIn as? LogIn.Success)?.userName ?: userName
    }

    fun interface Callback<T> {
        fun onResult(t: T)
    }
}