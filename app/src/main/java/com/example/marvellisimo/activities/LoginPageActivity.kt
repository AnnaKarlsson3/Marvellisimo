package com.example.marvellisimo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        noAccount.setOnClickListener{
            val intent = Intent(this, SignUpPageActivity ::class.java)
            startActivity(intent)
        }
    }
}