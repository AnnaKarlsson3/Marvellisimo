package com.example.marvellisimo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.marvellisimo.entity.Inbox
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
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

                         if(inboxItem?.seen == false) {
                                sendNotifications()
                            }
                     }
                     override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                     }
                     override fun onChildRemoved(snapshot: DataSnapshot) {
                     }
                     override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                     }
                     override fun onCancelled(error: DatabaseError) {
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
                .setSmallIcon(R.drawable.marvelissimo_logga)
                .setContentTitle("Marvelisimo")
                .setContentText("New message")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(notificationsID, builder.build())
            }
        }
}