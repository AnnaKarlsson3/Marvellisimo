package com.example.marvellisimo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.marvellisimo.activities.SendMessageActivity
import com.example.marvellisimo.entity.ChatMessage
import com.example.marvellisimo.entity.Inbox
import com.example.marvellisimo.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmInit : Application() {
    private val CHANNEL_ID = "channel_id_01"
    private val notificationsID = 101

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("comicDb")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)

        createNotificationsChannel()

        val user = Firebase.auth.currentUser
        val userid = user?.uid
        val inboxRefrence = FirebaseDatabase.getInstance().getReference("/inbox/$userid")



             inboxRefrence.addChildEventListener(object: ChildEventListener {
                     override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                         val inboxItem = p0.getValue(Inbox::class.java)

                            val uid = inboxItem?.uid
                         Log.d("kul", "$uid")

                            if(inboxItem?.seen == false) {
                                sendNotifications()
                            }



                     }

                     override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                     }

                     override fun onChildRemoved(snapshot: DataSnapshot) {
                         TODO("Not yet implemented")
                     }

                     override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                         TODO("Not yet implemented")
                     }

                     override fun onCancelled(error: DatabaseError) {
                         TODO("Not yet implemented")
                     }
                 })



    }

        //makes notifications pop up
        private fun createNotificationsChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "notificationTitle"
                val descriptionText = "Notification Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText

                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

            }

        }

        private fun sendNotifications() {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Marvelisimo")
                .setContentText("New message")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(notificationsID, builder.build())
            }

        }



}