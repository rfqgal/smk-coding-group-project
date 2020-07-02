package id.smkcoding.teamalvan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var auth: FirebaseAuth? = null
    var callbackManager: CallbackManager? = null

    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        progress.visibility = View.GONE

        auth = FirebaseAuth.getInstance() //Mendapakan Instance Firebase Auth
        //printkeyhash()
        //Cek apakah sudah login atau belum
        if (auth!!.currentUser == null) {
        } else {
            //Jika sudah login langsung dilempar ke MainActivity
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_google_login.setOnClickListener(this)
        btn_facebook_login.setOnClickListener{
            signIn()
        }
    }

    private fun signIn() {
        btn_facebook_login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth!!.signInWithCredential(credential)
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                Log.e("ERROR_EDMT",e.message)
            }
            .addOnSuccessListener { result ->

                Toast.makeText(this, "Selamat Datang", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    }



    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN = kode permintaan yang diberikan ke startActivityForResult
        if (requestCode == RC_SIGN_IN) {
            //Jika Berhasil masuk
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else { //Jika gagal login
                progress.visibility = View.GONE
                Toast.makeText(this, "Login Dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
        callbackManager!!.onActivityResult(requestCode,resultCode,data)
    }

    override fun onClick(v: View?) {
        // Statement program untuk login/masuk
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN)
        progress.visibility = View.VISIBLE
    }

    private fun printkeyhash() {
        try {
            val info = packageManager.getPackageInfo("com.example.chalenge2", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }
    
}



