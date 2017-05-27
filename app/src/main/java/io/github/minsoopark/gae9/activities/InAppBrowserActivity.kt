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
import android.webkit.*
import io.github.minsoopark.gae9.R


class InAppBrowserActivity : AppCompatActivity() {

    private lateinit var wvBrowser: WebView
    private lateinit var pbContent: ContentLoadingProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)

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

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                pbContent.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pbContent.hide()
            }
        })

        wvBrowser.setWebChromeClient(object : WebChromeClient() {
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

        val url = intent.getStringExtra("url")
        wvBrowser.loadUrl(url)
    }

    override fun onBackPressed() {
        if (wvBrowser.canGoBack()) {
            wvBrowser.goBack()
        } else {
            super.onBackPressed()
        }
    }
}