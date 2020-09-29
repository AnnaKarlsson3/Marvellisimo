package com.example.marvellisimo.entity


class Inbox(val id: String, val fromId: String, var seen: Boolean) {
    constructor() : this("","", false)
}