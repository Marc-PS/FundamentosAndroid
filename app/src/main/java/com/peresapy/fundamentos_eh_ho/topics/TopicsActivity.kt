package com.peresapy.fundamentos_eh_ho.topics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.peresapy.fundamentos_eh_ho.databinding.ActivityTopicsBinding
import com.peresapy.fundamentos_eh_ho.details.DetailsActivity
import com.peresapy.fundamentos_eh_ho.di.DIProvider
import com.peresapy.fundamentos_eh_ho.model.Topic

class TopicsActivity : AppCompatActivity() {

    private val binding: ActivityTopicsBinding by lazy {
        ActivityTopicsBinding.inflate(
            layoutInflater
        )
    }
    private val topicsAdapter = TopicsAdapter(::openTopic)
    private val viewModel: TopicsViewModel by viewModels { DIProvider.topicsViewModelProviderFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topics.apply {
            adapter = topicsAdapter
            addItemDecoration(DividerItemDecoration(this@TopicsActivity, LinearLayout.VERTICAL))
        }

        viewModel.state.observe(this) {
            when (it) {
                is TopicsViewModel.State.LoadingTopics -> renderLoading(it)
                is TopicsViewModel.State.TopicsReceived -> topicsAdapter.submitList(it.topics)
                is TopicsViewModel.State.NoTopics -> renderEmptyState()
            }
        }

        binding.swipe.setOnRefreshListener {
            viewModel.loadTopics()
            binding.swipe.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTopics()
    }

    private fun renderEmptyState() {
        binding.emptyText.isVisible = true
        binding.emptyText.text = "No se han encontrado topics"
    }

    private fun renderLoading(loadingState: TopicsViewModel.State.LoadingTopics) {
        (loadingState as? TopicsViewModel.State.LoadingTopics.LoadingWithTopics)?.let {
            topicsAdapter.submitList(it.topics)
        }
    }

    private fun openTopic(topic: Topic) {
        startActivity(DetailsActivity.createIntent(this, topic))
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, TopicsActivity::class.java)
    }

}