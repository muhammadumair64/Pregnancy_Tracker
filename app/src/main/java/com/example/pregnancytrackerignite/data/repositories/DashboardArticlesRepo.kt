package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.MutableLiveData
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.DashboardArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardArticlesRepo @Inject constructor() {
    val listOfItems = MutableLiveData<List<DashboardArticles>>()


    suspend fun getArticles() {
        withContext(Dispatchers.IO) {
            val articles = listOf(
                DashboardArticles(
                    image = R.drawable.image1,
                ),
                DashboardArticles(
                    image = R.drawable.image2,
                ),
                DashboardArticles(
                    image = R.drawable.image3,
                ),
                DashboardArticles(
                    image = R.drawable.image4,
                ),
                DashboardArticles(
                    image = R.drawable.image5,
                ),
                DashboardArticles(
                    image = R.drawable.image6,
                ),
                DashboardArticles(
                    image = R.drawable.image7,
                ),
                DashboardArticles(
                    image = R.drawable.image8,
                ),
                DashboardArticles(
                    image = R.drawable.image9,
                ),
                DashboardArticles(
                    image = R.drawable.image10,
                ),
                DashboardArticles(
                    image = R.drawable.image11,
                ),
                DashboardArticles(
                    image = R.drawable.image12,
                ),
                DashboardArticles(
                    image = R.drawable.image13,
                ),
                DashboardArticles(
                    image = R.drawable.image14,
                ),
                DashboardArticles(
                    image = R.drawable.image15,
                ),
                DashboardArticles(
                    image = R.drawable.image16,
                ),
                DashboardArticles(
                    image = R.drawable.image17,
                ),
                DashboardArticles(
                    image = R.drawable.image18,
                ),
                DashboardArticles(
                    image = R.drawable.image19,
                ),
                DashboardArticles(
                    image = R.drawable.image20,
                ),
                DashboardArticles(
                    image = R.drawable.image21,
                ),
                DashboardArticles(
                    image = R.drawable.image22,
                ),
                DashboardArticles(
                    image = R.drawable.image23,
                ),
                DashboardArticles(
                    image = R.drawable.image24,
                ),
                DashboardArticles(
                    image = R.drawable.image25,
                ),
                DashboardArticles(
                    image = R.drawable.image26,
                ),
                DashboardArticles(
                    image = R.drawable.image27,
                ),
                DashboardArticles(
                    image = R.drawable.image28,
                ),
                DashboardArticles(
                    image = R.drawable.image29,
                ),
                DashboardArticles(
                    image = R.drawable.image30,
                ),
                DashboardArticles(
                    image = R.drawable.image31,
                ),
                DashboardArticles(
                    image = R.drawable.image32,
                ),
                DashboardArticles(
                    image = R.drawable.image33,
                ),
                DashboardArticles(
                    image = R.drawable.image34,
                ),
                DashboardArticles(
                    image = R.drawable.image35,
                ),
                DashboardArticles(
                    image = R.drawable.image36,
                ),
                DashboardArticles(
                    image = R.drawable.image37,
                ),
                DashboardArticles(
                    image = R.drawable.image38,
                ),
                DashboardArticles(
                    image = R.drawable.image39,
                ),
                DashboardArticles(
                    image = R.drawable.image40,
                ),
                )
            listOfItems.postValue(articles)
        }
    }
}