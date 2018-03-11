package com.example.dream.gre;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class StudyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    WebView myWebView;
    SwipeRefreshLayout swipe;
    String token="";
    String cookie,cook;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final static String LOGIN_URL="http://www.easyschoolgroup.com/login";

    private FragmentCommunicationInterface communicationInterface;
    private OnFragmentInteractionListener mListener;


    public StudyFragment() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyFragment newInstance(String param1, String param2) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Handler handler= new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{webViewGoBack();
                }break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

          View view = inflater.inflate(R.layout.fragment_study, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        myWebView = (WebView)view.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSaveFormData(false);

        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("x-auth-token", token);

        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view,String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.equals(LOGIN_URL)){
                    CookieSyncManager syncManager = CookieSyncManager.createInstance(myWebView.getContext());
                    CookieManager cookieManager = CookieManager.getInstance();

                    cookie = cookieManager.getCookie(url);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("cookie", cookie);
                    //editor.putBoolean("loggedIn",true);
                    editor.commit();
                    syncManager.sync();

                }
                else if (url.equals("https://www.easyschoolgroup.com/")){
                    CookieSyncManager syncManager = CookieSyncManager.createInstance(myWebView.getContext());
                    CookieManager cookieManager = CookieManager.getInstance();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear().apply();
                    cookieManager.removeAllCookie();
                    editor.commit();
                    syncManager.sync();
                }
                Toast.makeText(getContext(), "page finished", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Toast.makeText(getContext(), "page Started", Toast.LENGTH_SHORT).show();
                if (url.equals("https://www.easyschoolgroup.com/")){
                    CookieSyncManager syncManager = CookieSyncManager.createInstance(myWebView.getContext());
                    CookieManager cookieManager = CookieManager.getInstance();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear().apply();
                    cookieManager.removeAllCookie();
                    editor.commit();
                    syncManager.sync();
                }
                super.onPageStarted(view, url, favicon);
            }
        });

        SharedPreferences.Editor editor = prefs.edit();
        cook = prefs.getString("cookie",cookie);
        Boolean login =prefs.getBoolean("loggedIn",false);
        editor.commit();



        if (cook != null){
            myWebView.loadUrl("http://www.easyschoolgroup.com/home");
            communicationInterface.respond(cook);
        }
        else {
            myWebView.loadUrl(LOGIN_URL);

        }

        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_BACK
                        && keyEvent.getAction() == MotionEvent.ACTION_UP
                        && myWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }
        });




        return view;
    }

    private void webViewGoBack(){
        myWebView.goBack();
    }

    private String getCookies(){

        return cook;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        communicationInterface=(FragmentCommunicationInterface) getActivity();
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "StudyFragment Attached", Toast.LENGTH_SHORT).show();
        }
        communicationInterface=(FragmentCommunicationInterface) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //
   /* boolean isLoggedin,isRedirected;
    SharedPreferences pref =
            getActivity().getSharedPreferences("Login", MODE_PRIVATE);

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e(TAG, "should override url loading" + url);
        view.loadUrl(url);

        isRedirected = true;
        return true;
    }

    public void onPageFinished(WebView view, String url) {

        Log.e(TAG, "on page finished" + url);

        isLoggedin = pref.getBoolean("isLoggedin",false);


        if (url.equals(SyncStateContract.Constants.ACCOUNT_NAME) && url.equals(SyncStateContract.Constants.CONTENT_DIRECTORY) && !isLoggedin) {
            Log.e(TAG, "reload");
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("isLoggedin", true);
                    editor.commit();


            Toast toast = Toast.makeText(getContext(), "You have signed in", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            WebView webView = (WebView)view.findViewById(R.id.webView);
            webView.loadUrl("https://www.easyschoolgroup.com/login");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().getDisplayZoomControls();
            webView.setWebViewClient(new  WebViewClient());
            Log.e(TAG, "in get name-----------------");

            //String getnamejs = "(document.getElementById('input-firstname').value);";
            String getnamejs = "(document.getElementById('app_customer_name')[0].innerHTML);";
            webView.loadUrl("javascript: window.CallToAnAndroidFunction.getUserName" + getnamejs);
        }
        //if(url.equals(Constants.LOGOUTURL) && isLoggedin) {
        if (url.equals(SyncStateContract.Constants.class)) {
            Log.e(TAG, "logging out-----------------");

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isLoggedin", false);
            //editor.clear();
            editor.commit();


            Toast toast = Toast.makeText(getContext(), "You have logged out", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            //return;
        }*/
    //
}

