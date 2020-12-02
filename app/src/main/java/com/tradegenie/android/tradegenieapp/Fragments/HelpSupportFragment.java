package com.tradegenie.android.tradegenieapp.Fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpSupportFragment extends Fragment {


    @BindView(R.id.Lnr_help)
    LinearLayout LnrHelp;
    @BindView(R.id.Lnr_about)
    LinearLayout LnrAbout;
    @BindView(R.id.Lnr_privacy)
    LinearLayout LnrPrivacy;
    @BindView(R.id.webview)
    WebView webView;
    Unbinder unbinder;


    private static final int REQUEST_FILE_PICKER = 1;
    @BindView(R.id.txt_about_us_help)
    TextView txtAboutUsHelp;
    @BindView(R.id.txt_privacy_help)
    TextView txtPrivacyHelp;
    @BindView(R.id.txt_help_help)
    TextView txtHelpHelp;
    @BindView(R.id.txt_privacy_about)
    TextView txtPrivacyAbout;
    @BindView(R.id.txt_help_about)
    TextView txtHelpAbout;
    @BindView(R.id.txt_about_about)
    TextView txtAboutAbout;
    @BindView(R.id.txt_help_privacy)
    TextView txtHelpPrivacy;
    @BindView(R.id.txt_about_privacy)
    TextView txtAboutPrivacy;
    @BindView(R.id.txt_privacy_privacy)
    TextView txtPrivacyPrivacy;
    private ValueCallback<Uri> mFilePathCallback4;
    private ValueCallback<Uri[]> mFilePathCallback5;


    public HelpSupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_support, container, false);
        unbinder = ButterKnife.bind(this, view);
        Constants.mMainActivity.setToolBarPostEnquiryHide();
        webView.setWebViewClient(new WebViewClient());
        Constants.mMainActivity.setToolBar(VISIBLE, GONE, GONE, GONE);
        Constants.mMainActivity.setToolBarName(getString(R.string.help_support));
        webView.getSettings().setAllowFileAccess(true);//the lines of code added

        webView.getSettings().setJavaScriptEnabled(true);
        webView.canGoBack();
        webView.loadUrl(Constants.BASE_URL + "term-and-condition");

        webView.setWebChromeClient(new WebChromeClient() {
            public void openFileChooser(ValueCallback<Uri> filePathCallback) {
                mFilePathCallback4 = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
            }

            @SuppressWarnings("unchecked")
            public void openFileChooser(ValueCallback filePathCallback, String acceptType) {
                mFilePathCallback4 = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
            }

            public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
                mFilePathCallback4 = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback5 = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
                return true;
            }
        });


        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        final ProgressDialog progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage("Please wait...");


        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    try {
                        progressBar.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    try {
                        progressBar.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    try {
                        progressBar.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    /*    webView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });*/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
/*

    @OnClick({R.id.Lnr_help, R.id.Lnr_about, R.id.Lnr_privacy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Lnr_help:

                LnrHelp.setVisibility(View.VISIBLE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.GONE);


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "about-us");
                break;
            case R.id.Lnr_about:

                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.VISIBLE);
                LnrPrivacy.setVisibility(View.GONE);


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "about-us");


                break;
            case R.id.Lnr_privacy:

                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.VISIBLE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "privacy-policy");
                break;
        }
    }
*/

    @OnClick({R.id.txt_about_us_help, R.id.txt_privacy_help, R.id.txt_help_help, R.id.txt_privacy_about, R.id.txt_help_about, R.id.txt_about_about, R.id.txt_help_privacy, R.id.txt_about_privacy, R.id.txt_privacy_privacy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_about_us_help:


                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.VISIBLE);
                LnrPrivacy.setVisibility(View.GONE);


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "about-us");

                break;
            case R.id.txt_privacy_help:

                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.VISIBLE);


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "privacy-policy");
                break;
            case R.id.txt_help_help:

                LnrHelp.setVisibility(View.VISIBLE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.GONE);


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "term-and-condition");

                break;
            case R.id.txt_privacy_about:
                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.VISIBLE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "privacy-policy");
                break;
            case R.id.txt_help_about:
                LnrHelp.setVisibility(View.VISIBLE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.GONE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "term-and-condition");

                break;
            case R.id.txt_about_about:
                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.VISIBLE);
                LnrPrivacy.setVisibility(View.GONE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "about-us");

                break;
            case R.id.txt_help_privacy:

                LnrHelp.setVisibility(View.VISIBLE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.GONE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "term-and-condition");

                break;
            case R.id.txt_about_privacy:
                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.VISIBLE);
                LnrPrivacy.setVisibility(View.GONE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "about-us");

                break;
            case R.id.txt_privacy_privacy:

                LnrHelp.setVisibility(View.GONE);
                LnrAbout.setVisibility(View.GONE);
                LnrPrivacy.setVisibility(View.VISIBLE);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAllowFileAccess(true);//the lines of code added

                webView.getSettings().setJavaScriptEnabled(true);
                webView.canGoBack();
                webView.loadUrl(Constants.BASE_URL + "privacy-policy");
                break;
        }
    }
}
