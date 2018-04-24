package session.hyderabad.live.sessionliveqa.activites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post_question.*
import session.hyderabad.live.sessionliveqa.AppConstants
import session.hyderabad.live.sessionliveqa.R
import session.hyderabad.live.sessionliveqa.utils.getSavedString
import session.hyderabad.live.sessionliveqa.utils.showToast

class PostQuestionActivity : AppCompatActivity() {

    companion object {
        private const val NA = "N/A"
        private const val MOBILE_CROSS = "XXXXXX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_question)

        val firestoreQuestionCollection = FirebaseFirestore.getInstance()
                .collection(AppConstants.FIRESTORE_QUESTION_COLLECTION)

        btSubmitQuestion.setOnClickListener {

            val name = getSavedString(AppConstants.LoginUser.NAME)
            val email = getSavedString(AppConstants.LoginUser.EMAIL)
            val phoneNumber = getSavedString(AppConstants.LoginUser.PHONE)?.dropLast(6).plus(MOBILE_CROSS)

            val sendToServerName = when {
                name.isNullOrEmpty().not() -> name
                phoneNumber.isNotEmpty() -> phoneNumber
                else -> NA
            }

            val sendToServerEmail = when {
                email.isNullOrEmpty().not() -> email
                phoneNumber.isNotEmpty() -> phoneNumber
                else -> NA
            }

            val formedQuestion = mapOf(
                    AppConstants.Keys.QUESTION to etQuestion.text.toString(),
                    AppConstants.Keys.NAME to sendToServerName,
                    AppConstants.Keys.EMAIL to sendToServerEmail,
                    AppConstants.Keys.TIMESTAMP to FieldValue.serverTimestamp())

            cvLoading.visibility = View.VISIBLE
            firestoreQuestionCollection.add(formedQuestion).addOnCompleteListener { task ->
                cvLoading.visibility = View.GONE
                if (task.isSuccessful) {
                    showToast(getString(R.string.success_toast_message_posted))
                    onBackPressed()
                } else {
                    showToast(getString(R.string.error_toast_unable_to_post_question))
                }
            }
        }
    }
}