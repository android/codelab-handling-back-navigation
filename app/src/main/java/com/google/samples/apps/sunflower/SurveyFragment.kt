/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback

class SurveyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_survey, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = view.findViewById(R.id.webView) as WebView

        // Use the existing form or replace it with your own
        webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSflribfz_k78HvGPW-yDLg3b6j2PG52s0QQF6zCy7RPT79FwA/viewform?usp=sf_link")
        webView.settings.javaScriptEnabled = true

        // Create the callback, note it's initially set to false since the webview will have a single
        // page in the page stack, when the fragment opens
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    webView.canGoBack() -> webView.goBack()
                }
            }
        }

        // Register the callback with the onBackPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        disableOnBackPressedCallback(webView, onBackPressedCallback)
    }

    private fun disableOnBackPressedCallback(webView: WebView, onBackPressedCallback: OnBackPressedCallback) {
        webView.webViewClient = object: WebViewClient() {
             // Use webView.canGoBack() to determine whether or not the OnBackPressedCallback is enabled.
             // if the callback is enabled, the app takes control and determines what to do. If the
             // callbacks is disabled; the back nav gesture will go back to the topmost activity/fragment
             // in the back stack.
            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                onBackPressedCallback.isEnabled = webView.canGoBack()
            }
        }
    }
}