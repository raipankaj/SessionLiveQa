package session.hyderabad.live.sessionliveqa

object AppConstants {

    const val FIRESTORE_QUESTION_COLLECTION = "questions"
    const val FIRESTORE_ADMIN = "admin"
    const val REMOVED = "REMOVED"
    const val MODIFIED = "MODIFIED"

    object Admin {
        const val IS_ADMIN = "is_admin"
        const val EMAIL = "email"
    }

    object LoginUser {
        const val EMAIL = "login_email"
        const val NAME = "login_name"
        const val PHONE = "login_phone"
        const val USER_LOGGED_IN = "user_logged_in"
    }

    object Keys {
        const val ID = "id"
        const val ANSWER = "answer"
        const val ANSWERED_BY = "answeredBy"
        const val TIMESTAMP = "timestamp"
        const val QUESTION = "question"
        const val NAME = "name"
        const val EMAIL = "email"
    }

}