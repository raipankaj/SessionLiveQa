package session.hyderabad.live.sessionliveqa.activites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_answer.*
import session.hyderabad.live.sessionliveqa.AppConstants
import session.hyderabad.live.sessionliveqa.R
import session.hyderabad.live.sessionliveqa.utils.getSavedString
import session.hyderabad.live.sessionliveqa.utils.showToast
import session.hyderabad.live.sessionliveqa.utils.trimmedText

class AnswerActivity : AppCompatActivity() {

    private val mDocumentId by lazy {
        intent?.extras?.getString(AppConstants.Keys.ID)
    }

    private val mPreviousAnswer by lazy {
        intent?.extras?.getString(AppConstants.Keys.ANSWER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val questionReference = FirebaseFirestore.getInstance().collection(AppConstants.FIRESTORE_QUESTION_COLLECTION)

        etAnswer.setText(mPreviousAnswer)
        etAnswer.setSelection(mPreviousAnswer?.length ?: 0)

        btSubmitAnswer.setOnClickListener {
            cvLoading.visibility = View.VISIBLE

            val map = mapOf(AppConstants.Keys.ANSWER to etAnswer.trimmedText(),
                    AppConstants.Keys.ANSWERED_BY to getSavedString(AppConstants.LoginUser.NAME))

            questionReference
                    .document(mDocumentId.toString())
                    .update(map)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(getString(R.string.success_toast_answer_posted))
                            finish()
                        } else {
                            cvLoading.visibility = View.GONE
                            showToast(getString(R.string.error_toast_unable_to_answer))
                        }
                    }
        }
    }
}