    package com.example.marvellisimo.activities
    import android.content.Intent
    import android.os.Bundle
    import android.os.PersistableBundle
    import android.util.Log
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.example.marvellisimo.R
    import com.google.firebase.auth.FirebaseAuth

    import kotlinx.android.synthetic.main.activity_signin.*

    class SignUpPageActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_signin)

            haveAccount_mainView.setOnClickListener{
                val intent = Intent(this, LoginPageActivity ::class.java)
                startActivity(intent)
            }

            button_register_mainView.setOnClickListener {
                performRegister()
            }
        }

        private fun performRegister(){
            val userName = username_mainView.text.toString()
            val email = email_mainView.text.toString()
            val password = password_mainView.text.toString()

            if(userName.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter username/email/password", Toast.LENGTH_LONG).show()
                return
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    //else if successful
                    Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this,"Failed to create user: ${it.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }
