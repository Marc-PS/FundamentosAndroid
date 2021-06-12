package com.peresapy.fundamentos_eh_ho.details

import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.peresapy.fundamentos_eh_ho.databinding.ViewDetailsBinding
import com.peresapy.fundamentos_eh_ho.extensions.inflater
import com.peresapy.fundamentos_eh_ho.model.Details

class DetailsAdapter(difUtilItemCallback: DiffUtil.ItemCallback<Details> = DIFF) :
    ListAdapter<Details, DetailsAdapter.DetailsViewHolder>(difUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder = DetailsViewHolder(parent)

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Details>() {
            override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean {
                return oldItem.message == newItem.message
            }

            override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean {
                return oldItem == newItem
            }
        }
    }

    class DetailsViewHolder(
        parent: ViewGroup,
        private val binding: ViewDetailsBinding = ViewDetailsBinding.inflate(
            parent.inflater,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Details) {
            binding.detailName.text = post.userName
            binding.detailMessage.text = Html.fromHtml(post.message)
            binding.detailId.text = post.id.toString()
            binding.detailAvatar.load(post.avatar) {
                transformations(CircleCropTransformation())
            }
        }
    }
}