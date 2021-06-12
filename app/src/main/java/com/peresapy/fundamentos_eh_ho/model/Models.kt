package com.peresapy.fundamentos_eh_ho.model

import java.io.Serializable

sealed class LogIn {
    data class Success(val userName: String) : LogIn()
    data class Error(val error: String) : LogIn()
}

data class Topic(
    val id: Int,
    val title: String,
    val lastPosterUsername: String,
    val excerpt: String,
    val replyCount: Int,
    val likes: Int,
    val views: Int,
    val pinned: Boolean,
    val bumped: Boolean,
    val lastPostedAt: String,
) : Serializable

data class Details(
    val id: Int,
    val message: String,
    val userName: String,
    val avatar: String,
)