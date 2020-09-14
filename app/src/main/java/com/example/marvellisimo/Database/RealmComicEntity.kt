package com.example.marvellisimo.Database

import com.example.marvellisimo.ViewModel.Url
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class RealmComicEntity:RealmObject() {
    @PrimaryKey
    var id: Int? = null
    var title: String? = null
    var description: String?= null
    var thumbnail: String? =null
    var urls: RealmList<UrlDb>? = RealmList()
}

@RealmClass
open class UrlDb: RealmObject(){
    var type: String? = null
    var url:String? = null
}