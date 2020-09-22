package com.example.marvellisimo.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marvellisimo.ComicsPageActivity
import com.example.marvellisimo.R
import com.example.marvellisimo.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_signin.*
import java.util.*

class SignUpPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        haveAccount_mainView.setOnClickListener {
            val intent = Intent(this, LoginPageActivity::class.java)
            startActivity(intent)
        }

        button_register_mainView.setOnClickListener {
            performRegister()
        }

        button_imageSelector_mainView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotouri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //check what the selected image is
            selectedPhotouri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotouri)
            circle_image_view.setImageBitmap(bitmap)
            button_imageSelector_mainView.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            button_imageSelector_mainView.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val userName = username_mainView.text.toString()
        val email = email_mainView.text.toString()
        val password = password_mainView.text.toString()

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username/email/password", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                //else if successful
                Log.d(
                    "SignUpActivity",
                    "Successfully created user with uid: ${it.result?.user?.uid}"
                )
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotouri == null) return
        val fileName = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance().getReference("/images/${fileName}")
        reference.putFile(selectedPhotouri!!).addOnSuccessListener {
            Log.d("SignUpActivity", "image is uploaded: ${it.metadata?.path}")
            reference.downloadUrl.addOnSuccessListener {
                Log.d("SignUpActivity", "Image Location: ${it.toString()}")
                saveUserToFirebaseDatabase(it.toString())
            }
        }
            .addOnFailureListener {
                //todo
            }

    }

    private fun saveUserToFirebaseDatabase(imageUri: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid")

        val user = User(uid, username_mainView.text.toString(), imageUri)

        reference.setValue(user)
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User is saved to firebase database")
                val intent = Intent(this, ComicsPageActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("SignUpActivity","Fail to store user: ${it.message}")
            }
    }
}
