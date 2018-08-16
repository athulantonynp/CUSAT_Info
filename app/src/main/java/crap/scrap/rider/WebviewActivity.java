package crap.scrap.rider;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {

    WebView webView;
    String link;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView= findViewById(R.id.webview);
        link=getIntent().getStringExtra("link");
        snackbar=Snackbar.make(findViewById(R.id.root),"മികച്ച ഒരിത്.",
               Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        prepareWebView(link);
    }

    private void prepareWebView(String link) {
        try {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.loadUrl(link);
            webView.setWebChromeClient(new WebChromeClient());
            webView.requestFocus();
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    snackbar.dismiss();

                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    snackbar.setText("മൂഞ്ചി...!");
                    snackbar.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }
}
