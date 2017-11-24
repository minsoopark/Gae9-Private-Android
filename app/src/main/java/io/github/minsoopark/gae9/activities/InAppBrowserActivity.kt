package io.github.minsoopark.gae9.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import io.github.minsoopark.gae9.R


class InAppBrowserActivity : AppCompatActivity() {

    private lateinit var wvBrowser: WebView
    private lateinit var pbContent: ContentLoadingProgressBar

    private var url: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)

        supportActionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
        }

        wvBrowser = findViewById(R.id.wv_browser) as WebView
        pbContent = findViewById(R.id.pb_content) as ContentLoadingProgressBar

        val webSettings = wvBrowser.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true

        wvBrowser.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url
                val urlString = url.toString()

                if (url.scheme in arrayOf("http", "https")) {
                    view.loadUrl(urlString)
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                    startActivity(intent)
                }
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                pbContent.show()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                pbContent.hide()
            }
        })

        wvBrowser.setWebChromeClient(object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                AlertDialog.Builder(this@InAppBrowserActivity)
                        .setMessage(message)
                        .setPositiveButton(
                                android.R.string.ok,
                                { dialog, _ ->
                                    result.confirm()
                                    dialog.dismiss()
                                })
                        .setCancelable(true)
                        .create()
                        .show()

                return true
            }

            override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                val wvWindow = WebView(this@InAppBrowserActivity)

                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = wvWindow
                resultMsg.sendToTarget()

                return true
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pbContent.progress = newProgress
            }
        })

        url = intent.getStringExtra("url")
        wvBrowser.loadUrl(url)
    }

    override fun onBackPressed() {
        if (wvBrowser.canGoBack()) {
            wvBrowser.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menus_browser, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "text/plain"
                }
                startActivity(intent)
            }
            R.id.action_open_in_browser -> {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}