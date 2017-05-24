package io.github.minsoopark.gae9.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.TrendListAdapter
import io.github.minsoopark.gae9.network.ApiManager
import io.github.minsoopark.gae9.network.models.Trend
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var containerRefresh: SwipyRefreshLayout
    private lateinit var rvTrends: RecyclerView

    private val currentTrends = ArrayList<Trend>()

    private var nextMaxId: Int = 0

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_best -> {
                onBest()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_new -> {
                onNew()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_daily -> {
                onDaily()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        containerRefresh = findViewById(R.id.container_refresh) as SwipyRefreshLayout
        rvTrends = findViewById(R.id.rv_trends) as RecyclerView

        initView()
        initEvent()

        onBest()
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrends.layoutManager = layoutManager

        val adapter = TrendListAdapter(this, currentTrends)
        rvTrends.adapter = adapter
    }

    private fun initEvent() {
        containerRefresh.setOnRefreshListener {
            loadNewData(nextMaxId)
        }
    }

    private fun loadTrendData() {
        val trendService = ApiManager.getTrendService()
        val bestTrendsObservable = trendService.getTrend()

        bestTrendsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    val trendListData = result.response
                    val trends = trendListData.trends

                    currentTrends.clear()
                    currentTrends.addAll(trends)

                    rvTrends.adapter.notifyDataSetChanged()
                }
    }

    private fun loadNewData(maxId: Int) {
        val trendService = ApiManager.getTrendService()
        val newTrendsObservable = trendService.getNew(maxId)

        newTrendsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    val trendListData = result.response
                    val trends = trendListData.trends
                    nextMaxId = trendListData.nextMaxId ?: 0

                    if (maxId == 0) {
                        currentTrends.clear()
                    }
                    currentTrends.addAll(trends)

                    rvTrends.adapter.notifyDataSetChanged()

                    containerRefresh.isRefreshing = false
                }
    }

    private fun clearList() {
        currentTrends.clear()
        rvTrends.adapter.notifyDataSetChanged()
        rvTrends.scrollToPosition(0)
    }

    private fun onBest() {
        containerRefresh.isEnabled = false
        clearList()

        loadTrendData()
    }

    private fun onNew() {
        containerRefresh.isEnabled = true
        clearList()

        nextMaxId = 0
        loadNewData(0)
    }

    private fun onDaily() {
        containerRefresh.isEnabled = false
        clearList()
    }
}
