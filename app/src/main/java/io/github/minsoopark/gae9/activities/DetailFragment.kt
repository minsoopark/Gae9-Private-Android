package io.github.minsoopark.gae9.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.ImageListAdapter
import io.github.minsoopark.gae9.adapters.compats.Timeline
import io.github.minsoopark.gae9.network.models.Trend
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var rvImages: RecyclerView

    private var trend: Trend? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            trend = arguments.getSerializable(ARG_TREND) as Trend?
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater?.let {
            val view = it.inflate(R.layout.fragment_detail, container, false)

            rvImages = view.findViewById(R.id.rv_images) as RecyclerView

            initView()
            loadData()

            return view
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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
                        if (activity.isDestroyed || activity.isFinishing) {
                            return@subscribe
                        }

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

                        val adapter = ImageListAdapter(activity, imageUrls, timelines)
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

    companion object {
        private val ARG_TREND = "trend"

        fun newInstance(trend: Trend): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_TREND, trend)
            fragment.arguments = args
            return fragment
        }
    }
}
