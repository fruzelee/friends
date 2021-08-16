package com.github.fruzelee.friends.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.fruzelee.friends.databinding.ItemFriendsListLayoutBinding
import com.github.fruzelee.friends.model.entities.RandomFriendsResponse
import com.github.fruzelee.friends.utils.loadImage

/**
 * @author Fazle Rabbi
 * github.com/fruzelee
 * web: fr.crevado.com
 */
class RandomFriendsListAdapter : RecyclerView.Adapter<RandomFriendsListAdapter.ViewHolder>() {

    private var friends: List<RandomFriendsResponse.Result> = listOf()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val binding: ItemFriendsListLayoutBinding =
            ItemFriendsListLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = differ.currentList[position]
        holder.bindTo(friend)
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int = differ.currentList.size

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: ItemFriendsListLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the View that will add each item to
        private val ivPortrait = view.ivPortrait
        private val tvFullName = view.tvFullName
        private val tvCountry = view.tvCountry

        @SuppressLint("SetTextI18n")
        fun bindTo(friend: RandomFriendsResponse.Result?) {
            // Load the friend image in the ImageView.
            ivPortrait.loadImage(friend!!.picture.medium)
            tvFullName.text = friend.name.title + " " + friend.name.first + " " + friend.name.last
            tvCountry.text = friend.location.country
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<RandomFriendsResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: RandomFriendsResponse.Result,
            newItem: RandomFriendsResponse.Result
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RandomFriendsResponse.Result,
            newItem: RandomFriendsResponse.Result
        ): Boolean {

            /**
             * As only the Portrait, Full Name, and Country would be shown,
             *
             * no need to add comparison between other contents
             */

            if (oldItem.location.country != newItem.location.country) {
                return false
            }

            if (oldItem.name.title != newItem.name.title) {
                return false
            }

            if (oldItem.name.first != newItem.name.first) {
                return false
            }

            if (oldItem.name.last != newItem.name.last) {
                return false
            }

            if (oldItem.picture.medium != newItem.picture.medium) {
                return false
            }

            return true
        }
    }


    /**
     * This will consume the list of users from the user list live data
     *
     * and will present the data in the UI
     */
    val differ = AsyncListDiffer(this, diffCallback)
}