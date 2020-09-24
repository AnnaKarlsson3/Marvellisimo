package com.example.marvellisimo.activities

import android.content.Intent
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
            }
            else
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(){
                    if (!it.isSuccessful)return@addOnCompleteListener
                    Log.d("Main","Login successful")

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
                .addOnFailureListener(){
                    Toast.makeText(this,"${it.message}", Toast.LENGTH_LONG).show()

                }

        }

        noAccount.setOnClickListener{
            //val intent = Intent(this, SignUpPageActivity ::class.java)
            finish()
        }
    }

//    suspend fun logInWithEmail(firebaseAuth: FirebaseAuth,
//                               email:String,password:String): AuthResult? {
//        return try{
//            val data = firebaseAuth
//                .signInWithEmailAndPassword(email,password)
//                .await()
//            return data
//        }catch (e : Exception){
//            return null
//        }
//    }
//

}