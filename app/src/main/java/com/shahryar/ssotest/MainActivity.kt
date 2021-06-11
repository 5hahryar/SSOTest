package com.shahryar.ssotest

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.openid.appauth.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginPressed(view: View) {
        val authConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://demo.identityserver.io/connect/authorize"),
            Uri.parse("https://demo.identityserver.io/connect/token"))



        val authRequest = AuthorizationRequest.Builder(authConfig, "interactive.confidential", ResponseTypeValues.CODE, Uri.parse("com.shahryar.ssotest://redirect"))
            .setScope("openid")
            .build()

        val authService = AuthorizationService(this)
        val intent = authService.getAuthorizationRequestIntent(authRequest)
        startActivityForResult(intent, 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11) {
            Log.d("SSO", "request 11 received")
            if (data != null) {
                Log.d("SSO", "request 11 not null")
                val resp = AuthorizationResponse.fromIntent(data)
                val ex = AuthorizationException.fromIntent(data)
                if(resp != null) {
                    Log.d("SSO", "response state: ${resp.accessToken}")

                    val response = "access token: ${resp.accessToken} \n" +
                            "state: ${resp.state} \n" +
                            "access token expiration time: ${resp.accessTokenExpirationTime} \n" +
                            "auth code: ${resp.authorizationCode} \n" +
                            "id token: ${resp.idToken} \n" +
                            "additional params: ${resp.additionalParameters} \n"
                    findViewById<TextView>(R.id.result).text = response
                }
            }
        }
    }
}