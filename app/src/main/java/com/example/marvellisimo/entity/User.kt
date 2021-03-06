package com.example.marvellisimo.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid:String, val username: String, val imageUrl: String?, var  active: Boolean) : Parcelable {
    constructor(): this("","","", true)
}