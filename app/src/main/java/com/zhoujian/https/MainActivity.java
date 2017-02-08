package com.zhoujian.https;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread()
        {
            public void run()
            {
                try
                {
                    getConnection();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getConnection() throws IOException, KeyManagementException, NoSuchAlgorithmException,
            UnrecoverableKeyException, CertificateException, KeyStoreException {

        // https://github.com/zeke123/ConstraintLayout
        // http://my.csdn.net/my/mycsdn
        URL url = new URL("https://github.com/zeke123/ConstraintLayout");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5 * 1000);
        connection.setReadTimeout(5 * 1000);
        connection.setRequestMethod("GET");

        Log.e("zhoujian", "url==" + url);
        Log.e("zhoujian", " 是否是https请求==" + (connection instanceof HttpsURLConnection));
        if (connection instanceof HttpsURLConnection) {
            SSLContext sslContext = HttpsUtil.getSSLContextWithCer();
			 //SSLContext sslContext = HttpsUtil.getSSLContextWithoutCer();
            if (sslContext != null) {
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
               ((HttpsURLConnection) connection).setDefaultSSLSocketFactory(sslSocketFactory);
				//((HttpsURLConnection) connection).setHostnameVerifier(HttpsUtil.hostnameVerifier);
            }
        }
        int responseCode = connection.getResponseCode();
        Log.e("zhoujian", "responseCode==" + responseCode);
        if (responseCode == 200) {
            InputStream is = connection.getInputStream();
            Log.e("zhoujian", "is==" + is);
            is.close();
        }
        connection.disconnect();
    }
}

