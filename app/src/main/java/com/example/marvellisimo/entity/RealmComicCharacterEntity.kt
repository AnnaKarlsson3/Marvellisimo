package com.example.marvellisimo.entity

import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


@RealmClass
open  class RealmComicEntity:RealmObject(){
    @PrimaryKey
    var id: Int? = null
    var title: String? = null
    var description: String?= null
    var thumbnail: String? =null
    var urls: RealmList<UrlDb>? = RealmList()
    var favorite: Boolean = false

}
@Parcelize
@RealmClass
open class RealmCharacterEntity:RealmObject(),Parcelable {
    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var description: String?= null
    var thumbnail: String? =null
    var urls: RealmList<UrlDb>? = RealmList()
    var favorite: Boolean  = false
}


@RealmClass
open class UrlDb: RealmObject(){
    var type: String? = null
    var url:String? = null
}