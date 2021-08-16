package com.github.fruzelee.friends.view.activities

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.fruzelee.friends.R
import com.github.fruzelee.friends.databinding.ActivityFriendsListBinding
import com.github.fruzelee.friends.model.entities.RandomFriendsResponse
import com.github.fruzelee.friends.view.adapters.RandomFriendsListAdapter
import com.github.fruzelee.friends.viewmodel.RandomFriendsViewModel

class RandomFriendsListActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityFriendsListBinding
    private lateinit var mRandomFriendsViewModel: RandomFriendsViewModel
    private lateinit var mRandomFriendsListAdapter: RandomFriendsListAdapter
    // A global variable for Progress Dialog
    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Initialize the ViewModel variable.
        mRandomFriendsViewModel = ViewModelProvider(this).get(RandomFriendsViewModel::class.java)
        mRandomFriendsViewModel.getRandomFriendsFromAPI()
        randomFriendsViewModelObserver()
        createRecyclerView()
        setUpSwipeRefreshListener()
    }

    private fun setUpSwipeRefreshListener() {
        /**
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mBinding.srlRandomFriends.setOnRefreshListener {
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            mRandomFriendsViewModel.getRandomFriendsFromSwipeRefresh()
        }
    }

    /**
     * A function to get the data in the observer after the API is triggered.
     */
    private fun randomFriendsViewModelObserver() {

        mRandomFriendsViewModel.randomFriendsResponse?.observe(
            this,
            { randomFriendsResponse ->
                randomFriendsResponse?.let {
                    Log.i("Random Friends Response", "$randomFriendsResponse")

                    if (mBinding.srlRandomFriends.isRefreshing) {
                        mBinding.srlRandomFriends.isRefreshing = false
                    }

                    setRandomFriendsResponseInUI(randomFriendsResponse)
                }
            })

        mRandomFriendsViewModel.randomFriendsLoadingError.observe(
            this,
            { dataError ->
                dataError?.let {
                    Log.i("Random Friend API Error", "$dataError")

                    if (mBinding.srlRandomFriends.isRefreshing) {
                        mBinding.srlRandomFriends.isRefreshing = false
                    }
                }
            })

        mRandomFriendsViewModel.loadRandomFriends.observe(this, { loadRandomFriends ->
            loadRandomFriends?.let {
                Log.i("Random Friends Loading", "$loadRandomFriends")

                /**
                 * Show the progress dialog if the SwipeRefreshLayout is not visible
                 * while loading random friends and hide when the usage is completed.
                 */
                if (loadRandomFriends && !mBinding.srlRandomFriends.isRefreshing) {
                    showCustomProgressDialog() // Used to show the progress dialog
                } else {
                    hideProgressDialog()
                }
            }
        })
    }

    private fun setRandomFriendsResponseInUI(randomFriendsResponse: RandomFriendsResponse.Friend) {
        val list: List<RandomFriendsResponse.Result> = randomFriendsResponse.results

        /**
         * Make the adapter differ consume the friends list
         */

        mRandomFriendsListAdapter.differ.submitList(list)
    }

    private fun createRecyclerView() {

        mBinding.rvFriendsList.apply {

            // span count in grid view
            val gridViewItemAmountLandscape = 4
            val gridViewItemAmountPortrait = 2

            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    /**
                     * If the device is in landscape mode,
                     * recycler view  will show a grid view
                     * with 4 span count.
                     */

                    GridLayoutManager(
                        this@RandomFriendsListActivity,
                        gridViewItemAmountLandscape
                    ) // for landscape mode
                } else {

                    /**
                     * If the device is is in portrait mode,
                     * recycler view will show a grid view
                     * with 2 span count
                     */

                    GridLayoutManager(
                        this@RandomFriendsListActivity,
                        gridViewItemAmountPortrait
                    ) // for portrait mode
                }

            //initialize random friends list adapter
            mRandomFriendsListAdapter = RandomFriendsListAdapter()

            adapter = mRandomFriendsListAdapter

            overScrollMode = View.OVER_SCROLL_NEVER // hide the overscroll effect

        }
    }

    /**
     * A function is used to show the Custom Progress Dialog.
     */
    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this@RandomFriendsListActivity)

        mProgressDialog?.let {
            /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
            it.setContentView(R.layout.dialog_custom_progress)

            //Start the dialog and display it on screen.
            it.show()
        }
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    private fun hideProgressDialog() {
        mProgressDialog?.let {
            it.dismiss()
            mProgressDialog = null
        }
    }

}