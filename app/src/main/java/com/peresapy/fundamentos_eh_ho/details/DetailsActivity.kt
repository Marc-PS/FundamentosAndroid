package com.peresapy.fundamentos_eh_ho.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.peresapy.fundamentos_eh_ho.databinding.ActivityDetailsBinding
import com.peresapy.fundamentos_eh_ho.di.DIProvider
import com.peresapy.fundamentos_eh_ho.model.Topic

class DetailsActivity : AppCompatActivity() {

    private val binding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailsViewModel by viewModels { DIProvider.detailsViewModelProvider }

    private val detailsAdapter = DetailsAdapter()

    private val topic: Topic by lazy { intent.getSerializableExtra(TOPICS_KEY) as Topic }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.posts
        recyclerView.adapter = detailsAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this@DetailsActivity, LinearLayout.VERTICAL))

        viewModel.state.observe(this) {
            when (it) {
                is DetailsViewModel.State.LoadingPost -> {
                    renderLoading(it)
                    hideEmptyState()
                }
                is DetailsViewModel.State.DetailsReceived -> {
                    detailsAdapter.submitList(it.posts)
                    hideEmptyState()
                    hideLoading()
                }
                is DetailsViewModel.State.NoPost -> {
                    hideLoading()
                    renderEmptyState()
                }
            }
        }
        binding.sfl.setOnRefreshListener { viewModel.loadPosts(topic) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPosts(topic)
    }

    private fun renderEmptyState() {
        binding.emptyState.isVisible = true
        binding.emptyState.text = "No posts printed"
    }

    private fun hideEmptyState() {
        binding.emptyState.isVisible = false
    }

    private fun hideLoading() {
        binding.viewLoading.root.isVisible = false
        binding.sfl.isRefreshing = false
    }

    private fun renderLoading(loadingState: DetailsViewModel.State.LoadingPost) {
        (loadingState as? DetailsViewModel.State.LoadingPost.LoadingWithPosts)?.let {
            detailsAdapter.submitList(it.posts)
        }
    }


    companion object {
        const val TOPICS_KEY = "TOPICS_KEY"
        @JvmStatic
        fun createIntent(context: Context, topic: Topic): Intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra(TOPICS_KEY, topic)
        }
    }
}