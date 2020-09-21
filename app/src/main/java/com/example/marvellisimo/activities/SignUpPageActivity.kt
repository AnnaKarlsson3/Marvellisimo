    package com.example.marvellisimo.activities
    import android.content.Intent
    import android.os.Bundle
    import android.os.PersistableBundle
    import androidx.appcompat.app.AppCompatActivity
    import com.example.marvellisimo.R

    import kotlinx.android.synthetic.main.activity_signin.*

    class SignUpPageActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_signin)

            haveAccount_mainView.setOnClickListener{
                val intent = Intent(this, LoginPageActivity ::class.java)
                startActivity(intent)
            }
        }
    }
