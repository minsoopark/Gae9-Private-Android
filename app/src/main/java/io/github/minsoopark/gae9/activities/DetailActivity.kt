package io.github.minsoopark.gae9.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.ImageListAdapter
import io.github.minsoopark.gae9.adapters.compats.Timeline
import io.github.minsoopark.gae9.network.models.Trend
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

class DetailActivity : AppCompatActivity() {

    private lateinit var rvImages: RecyclerView

    private var trend: Trend? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        rvImages = findViewById(R.id.rv_images) as RecyclerView

        trend = intent.getSerializableExtra("trend") as Trend

        supportActionBar?.let {
            it.title = trend?.title ?: getString(R.string.label_untitled)
            it.setDisplayHomeAsUpEnabled(true)
        }

        initView()
        loadData()
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvImages.layoutManager = layoutManager
    }

    private fun loadData() {
        val imageUrls = ArrayList<String>()
        val timelines = ArrayList<Timeline>()

        trend?.let {
            downloadDocumentObservable(it.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { document ->
                        val imgElements = document.select(".post-content").select("img")
                        imgElements.forEachIndexed { index, element ->
                            if (index != imgElements.size - 1) {
                                val src = element.attr("src")
                                imageUrls.add(src)
                            }
                        }

                        val timelineElements = document.select("ul.timeline").select("li")
                        timelineElements.forEach {
                            val anchor = it.select("a")[0]

                            val title = anchor.attr("title")
                            val url = anchor.attr("href")
                            val meta = it.select("span")[0].ownText()

                            val timeline = Timeline(title, meta, url)
                            timelines.add(timeline)
                        }

                        val adapter = ImageListAdapter(this, imageUrls, timelines)
                        rvImages.adapter = adapter
                    }
        }
    }

    private fun downloadDocumentObservable(trendId: String): Observable<Document> {
        return Observable.defer {
            val document = Jsoup.connect("http://m.gae9.com/trend/$trendId").get()
            Observable.just(document)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}