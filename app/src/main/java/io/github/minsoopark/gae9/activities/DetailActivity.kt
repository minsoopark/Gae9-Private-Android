package io.github.minsoopark.gae9.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.ImageListAdapter
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

        initView()
        loadData()
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvImages.layoutManager = layoutManager
    }

    private fun loadData() {
        val imageUrls = ArrayList<String>()

        trend?.let {
            downloadDocumentObservable(it.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { document ->
                        val elements = document.select(".post-content").select("img")
                        elements.forEachIndexed { index, element ->
                            if (index != elements.size - 1) {
                                val src = element.attr("src")
                                imageUrls.add(src)
                            }
                        }
                        rvImages.adapter.notifyDataSetChanged()
                    }
        }

        val adapter = ImageListAdapter(this, imageUrls)
        rvImages.adapter = adapter
    }

    private fun downloadDocumentObservable(trendId: String): Observable<Document> {
        return Observable.defer {
            val document = Jsoup.connect("http://m.gae9.com/trend/$trendId").get()
            Observable.just(document)
        }
    }
}