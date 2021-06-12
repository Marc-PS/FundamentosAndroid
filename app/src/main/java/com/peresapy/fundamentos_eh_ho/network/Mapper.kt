package com.peresapy.fundamentos_eh_ho.network


import com.peresapy.fundamentos_eh_ho.model.LogIn
import com.peresapy.fundamentos_eh_ho.model.Details
import com.peresapy.fundamentos_eh_ho.model.Topic
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun Response.toSignInModel(): LogIn = when (this.isSuccessful) {
    true -> LogIn.Success(
        JSONObject(this.body?.string()).getJSONObject("user").getString("username")
    )
    false -> LogIn.Error(this.body?.string() ?: "Some Error parsing response")
}

fun IOException.toSignInModel(): LogIn = LogIn.Error(this.toString())

fun Response.toSignUpModel(): LogIn = when (this.isSuccessful) {
    true -> LogIn.Success(
        JSONObject(this.body?.string()).getJSONObject("user").getString("username")
    )
    false -> LogIn.Error(this.body?.string() ?: "Some Error parsing response")
}

fun IOException.toSignUpModel(): LogIn = LogIn.Error(this.toString())

fun Response.toTopicsModel(): Result<List<Topic>> = when (this.isSuccessful) {
    true -> Result.success(parseTopics(body?.string()))
        .also { println("JcLog: BackendResult -> $it") }
    false -> Result.failure(IOException(this.body?.string() ?: "Some Error parsing response"))
}

fun Response.toDetailsModel(baseUrl: String): Result<List<Details>> = when (this.isSuccessful) {
    true -> Result.success(parseDetails(baseUrl, body?.string()))
        .also { println("JcLog: BackendResult -> $it") }
    false -> Result.failure(IOException(this.body?.string() ?: "Some Error parsing response"))
}

fun IOException.toTopicsModel(): Result<List<Topic>> = Result.failure(this)
fun IOException.toDetailsModel(): Result<List<Details>> = Result.failure(this)

fun parseTopics(json: String?): List<Topic> = json?.let {
    val topicsJsonArray: JSONArray = JSONObject(it).getJSONObject("topic_list").getJSONArray("topics")
    (0 until topicsJsonArray.length()).map { index ->
        val topicJsonObject = topicsJsonArray.getJSONObject(index)
        Topic(
            id = topicJsonObject.getInt("id"),
            title = topicJsonObject.getString("title"),
            lastPosterUsername = topicJsonObject.getString("last_poster_username"),
            excerpt = topicJsonObject.optString("excerpt"),
            likes = topicJsonObject.getInt("like_count"),
            views = topicJsonObject.getInt("views"),
            pinned = topicJsonObject.getBoolean("pinned"),
            bumped = topicJsonObject.getBoolean("bumped"),
            replyCount = topicJsonObject.getInt("reply_count"),
            lastPostedAt = topicJsonObject.getString("last_posted_at"),
        )
    }
} ?: emptyList<Topic>()


fun parseDetails(baseUrl: String, json: String?): List<Details> = json?.let {
    val postsJsonArray: JSONArray = JSONObject(it).getJSONObject("post_stream").getJSONArray("posts")
    (0 until postsJsonArray.length()).map { index ->
        val postJSONObject = postsJsonArray.getJSONObject(index)
        Details(
            id = postJSONObject.getInt("id"),
            message = postJSONObject.getString("cooked")
                .replace("<p>", "")
                .replace("</p>", ""),
            userName = postJSONObject.getString("name"),
            avatar = "https://$baseUrl" + postJSONObject.getString("avatar_template").replace("{size}", "100")
        )
    }
} ?: emptyList<Details>()