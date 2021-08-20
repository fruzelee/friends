package com.github.fruzelee.friends.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.fruzelee.friends.R
import com.github.fruzelee.friends.databinding.ActivityFriendDetailsBinding
import com.github.fruzelee.friends.model.entities.FriendsListResponse
import com.github.fruzelee.friends.utils.Constants

class FriendDetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityFriendDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpToolbar()
        getFriendDetails()
    }

    /**
     * gets the friends data sent by the previous intent
     *
     * and loads the UI if the friends data can be retrieved
     */
    private fun getFriendDetails() {
        val friendDetails =
            intent.getSerializableExtra(Constants.FRIEND_DETAILS) as? FriendsListResponse.Result

        if (friendDetails != null) {
            loadUi(friendDetails)
        } else {
            Toast.makeText(
                this, "Friend details could not be retrieved.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun loadUi(result: FriendsListResponse.Result) {
        setPortrait(result.picture.large)
        mBinding.includeContentDetail.tvFullName.text = "${result.name.first} ${result.name.last}"
        mBinding.includeContentDetail.includeAddressLayout.street.text =
            "${result.location.street.number}, ${result.location.street.name}"
        mBinding.includeContentDetail.includeAddressLayout.city.text = result.location.city
        mBinding.includeContentDetail.includeAddressLayout.state.text = result.location.state
        mBinding.includeContentDetail.includeAddressLayout.country.text = result.location.country
        mBinding.includeContentDetail.includeEmailLayout.email.text = result.email
        mBinding.includeContentDetail.includePhoneLayout.cell.text = result.cell
        mBinding.includeContentDetail.includePhoneLayout.phone.text = result.phone
        mBinding.includeContentDetail.includeEmailLayout.email.setOnClickListener {
            try {
                navigateToEmail(result.email)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // sets the action bar title to the first name of the user
        supportActionBar?.subtitle = "${result.name.first} ${result.name.last}"
    }

    // set the friend's portrait
    private fun setPortrait(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_image_downloading)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mBinding.ivPortrait)
    }

    // Tapping on the friend’s email should open the mail app to send them an email.
    @SuppressLint("QueryPermissionsNeeded")
    private fun navigateToEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }

        /**
         * Verify that there are applications registered to handle this intent
         *
         * (resolveActivity returns null if none are registered)
         */
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, "Send email"))
        }
    }

    private fun setUpToolbar() {
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        mBinding.toolbar.setNavigationOnClickListener { finish() }
    }
}