package com.gramzin.kilo.model

import android.net.Uri

data class User(var name: String? = null, var id:String? = null, var avatarURL: Uri? = null,
                var isConnected: Boolean? = null)
