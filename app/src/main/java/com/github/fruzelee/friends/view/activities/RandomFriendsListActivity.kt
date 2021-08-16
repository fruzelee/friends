package com.github.fruzelee.friends.view.activities

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.fruzelee.friends.databinding.ActivityFriendsListBinding
import com.github.fruzelee.friends.model.entities.RandomFriendsResponse
import com.github.fruzelee.friends.view.adapters.RandomFriendsListAdapter
import com.github.fruzelee.friends.viewmodel.RandomFriendsViewModel

class RandomFriendsListActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityFriendsListBinding
    private lateinit var mRandomFriendsViewModel: RandomFriendsViewModel
    private lateinit var mRandomFriendsListAdapter: RandomFriendsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Initialize the ViewModel variable.
        mRandomFriendsViewModel = ViewModelProvider(this).get(RandomFriendsViewModel::class.java)
        mRandomFriendsViewModel.getRandomFriendsFromAPI()
        randomFriendsViewModelObserver()
        createRecyclerView()

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

                    setRandomFriendsResponseInUI(randomFriendsResponse)
                }
            })

        mRandomFriendsViewModel.randomFriendsLoadingError.observe(
            this,
            { dataError ->
                dataError?.let {
                    Log.i("Random Friend API Error", "$dataError")
                }
            })

        mRandomFriendsViewModel.loadRandomFriends.observe(this, { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random Friends Loading", "$loadRandomDish")
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

}