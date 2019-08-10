package xyz.fadli.pasbeli.model

import java.util.*

/**
 * @author Mahendri
 */

data class User(
        var id : Int,
        var status : Int,
        var name: String,
        var email: String,
        var email_verified_at: Date,
        var password: String,
        var remember_token: String,
        var created_at:Date,
        var updated_at:Date
)