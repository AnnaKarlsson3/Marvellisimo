package com.example.marvellisimo.entity


class Inbox(val fromId: String, val seen: Boolean) {
    constructor() : this("", false)
}