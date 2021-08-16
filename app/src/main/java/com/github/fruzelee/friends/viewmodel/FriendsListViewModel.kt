package com.github.fruzelee.friends.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.fruzelee.friends.model.entities.FriendsListResponse
import com.github.fruzelee.friends.model.network.FriendsListApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @author Fazle Rabbi
 * github.com/fruzelee
 * web: fr.crevado.com
 */
class FriendsListViewModel : ViewModel() {

    private val friendsListApiService = FriendsListApiService()

    /**
     * A disposable container that can hold onto multiple other Disposables and
     * offers time complexity for add(Disposable), remove(Disposable) and delete(Disposable)
     * operations.
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * Creates a MutableLiveData with null value assigned to it.
     */
    val loadRandomFriends = MutableLiveData<Boolean>()
    var randomFriendsResponse: MutableLiveData<FriendsListResponse.Friend>? = null
    val randomFriendsLoadingError = MutableLiveData<Boolean>()

    fun getRandomFriendsFromAPI() {

        /**
         * handling orientation changes - without needing to re-fetch the data from network
         */
        if (randomFriendsResponse == null) {
            randomFriendsResponse = MutableLiveData<FriendsListResponse.Friend>()
            loadData()
        }
    }

    fun getRandomFriendsFromSwipeRefresh() {

        /**
         * Loading new data from network call that is invoked
         * when the user performs a swipe-to-refresh gesture.
         */
        loadData()

    }

    private fun loadData() {
        // Define the value of the load random friends.
        loadRandomFriends.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(
            // Call the RandomFriends method of RandomFriendsApiService class.
            friendsListApiService.getRandomFriends()
                // Asynchronously subscribes SingleObserver to this Single on the specified Scheduler.
                /**
                 * Static factory methods for returning standard Scheduler instances.
                 *
                 * The initial and runtime values of the various scheduler types can be overridden via the
                 * {RxJavaPlugins.setInit(scheduler name)SchedulerHandler()} and
                 * {RxJavaPlugins.set(scheduler name)SchedulerHandler()} respectively.
                 */
                .subscribeOn(Schedulers.newThread())
                /**
                 * Signals the success item or the terminal signals of the current Single on the specified Scheduler,
                 * asynchronously.
                 *
                 * A Scheduler which executes actions on the Android main thread.
                 */
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<FriendsListResponse.Friend>() {
                    override fun onSuccess(value: FriendsListResponse.Friend?) {
                        // Update the values with response in the success method.
                        loadRandomFriends.value = false
                        randomFriendsResponse?.value = value!!
                        randomFriendsLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadRandomFriends.value = false
                        randomFriendsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }
}