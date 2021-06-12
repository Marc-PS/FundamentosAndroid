package com.peresapy.fundamentos_eh_ho.topics


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.peresapy.fundamentos_eh_ho.databinding.ViewTopicPinnedBinding
import com.peresapy.fundamentos_eh_ho.databinding.ViewTopicBumpedBinding
import com.peresapy.fundamentos_eh_ho.databinding.ViewTopicBinding
import com.peresapy.fundamentos_eh_ho.extensions.inflater
import com.peresapy.fundamentos_eh_ho.model.Topic

private const val NORMAL_TOPIC_TYPE = 0
private const val PINNED_TOPIC_TYPE = 1
private const val BUMPED_TOPIC_TYPE = 2

class TopicsAdapter(private val onTopicClick: (Topic) -> Unit, diffUtilItemCallback: DiffUtil.ItemCallback<Topic> = DIFF) :
    ListAdapter<Topic, TopicsAdapter.TopicViewHolder>(diffUtilItemCallback) {

    override fun getItemViewType(position: Int): Int {
        val topic = getItem(position)
        return when {
            topic.pinned -> PINNED_TOPIC_TYPE
            topic.bumped -> BUMPED_TOPIC_TYPE
            else -> NORMAL_TOPIC_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder =
        when (viewType) {
            NORMAL_TOPIC_TYPE -> NormalTopicViewHolder(parent, onTopicClick)
            PINNED_TOPIC_TYPE -> PinnedTopicViewHolder(parent, onTopicClick)
            BUMPED_TOPIC_TYPE -> BumpedTopicViewHolder(parent, onTopicClick)
            else -> throw IllegalStateException("ViewType not supported yet")
        }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Topic>() {
            override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean = oldItem == newItem
        }
    }

    abstract class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(topic: Topic)
    }

    class NormalTopicViewHolder(
        parent: ViewGroup,
        private val onTopicClick: (Topic) -> Unit,
        private val binding: ViewTopicBinding = ViewTopicBinding.inflate(
            parent.inflater,
            parent,
            false
        )
    ) : TopicViewHolder(binding.root) {

        override fun bind(topic: Topic) {
            binding.title.text = topic.title
            binding.username.text = topic.lastPosterUsername
            binding.likes.text = topic.likes.toString()
            binding.replies.text = topic.views.toString()
            binding.root.setOnClickListener { onTopicClick(topic) }
        }
    }

    class PinnedTopicViewHolder(
        parent: ViewGroup,
        private val onTopicClick: (Topic) -> Unit,
        private val binding: ViewTopicPinnedBinding = ViewTopicPinnedBinding.inflate(
            parent.inflater,
            parent,
            false
        )
    ) : TopicViewHolder(binding.root) {

        override fun bind(topic: Topic) {
            binding.title.text = topic.title
            binding.welcomePinned.text = topic.excerpt
            binding.root.setOnClickListener { onTopicClick(topic) }
        }
    }

    class BumpedTopicViewHolder(
        parent: ViewGroup,
        private val onTopicClick: (Topic) -> Unit,
        private val binding: ViewTopicBumpedBinding = ViewTopicBumpedBinding.inflate(
            parent.inflater,
            parent,
            false
        )
    ) : TopicViewHolder(binding.root) {

        override fun bind(topic: Topic) {
            binding.title.text = topic.title
            binding.username.text = topic.lastPosterUsername
            binding.likes.text = topic.likes.toString()
            binding.replies.text = topic.views.toString()
            binding.root.setOnClickListener { onTopicClick(topic) }
        }
    }
}