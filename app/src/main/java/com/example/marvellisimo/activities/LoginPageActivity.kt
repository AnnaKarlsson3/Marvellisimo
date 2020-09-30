package com.example.marvellisimo.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*



class LoginPageActivity : AppCompatActivity() {
    var notConnected = false

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            notConnected = intent.getBooleanExtra(
                ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false
            )
                if (notConnected) {
                    if (!notConnected) {
                        Toast.makeText(
                            applicationContext,
                            "you are connected now ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    disconnected()
                } else {
                    connected()
                }
            }
        }

        override fun onStart() {
            super.onStart()
            registerReceiver(
                broadcastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }

        override fun onStop() {
            super.onStop()
            unregisterReceiver(broadcastReceiver)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            button_signin_signupView.setOnClickListener {
                Log.d("login", "clicking on signin")
                val email = email_signupView.text.toString()
                val password = password_signupView.text.toString()

                //check if email or password-field is empty/null
                if (email.isEmpty() || password.isEmpty()) {
                    //Print warning text to user
                    Toast.makeText(this, "Please enter an email and password", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener() {

                            if (!it.isSuccessful) return@addOnCompleteListener
                            Log.d("Main", "Login successful")

                            //set boolean active in db to true when logged in:
                            val ref = FirebaseDatabase.getInstance().getReference("/users")
                            val user = Firebase.auth.currentUser
                            val userid = user?.uid

                            if (userid != null) {
                                ref.child(userid).child("active").setValue(true)
                            }
                            //start new intent:
                            val intent = Intent(this, ComicsPageActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener() {
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                        }
            }

            noAccount.setOnClickListener {
                val intent = Intent(this, SignUpPageActivity::class.java)
                startActivity(intent)
            }
        }

        private fun disconnected() {
            Log.d("networkaccess", "disconnected")
            Toast.makeText(
                applicationContext,
                "Internet Not Available, Cross Check Your Internet Connectivity and Try Again! ",
                Toast.LENGTH_LONG
            ).show()
        }

        private fun connected() {
            Log.d("networkaccess", "connected")
        }
}