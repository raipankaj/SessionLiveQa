package session.hyderabad.live.sessionliveqa.activites

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import session.hyderabad.live.sessionliveqa.AppConstants
import session.hyderabad.live.sessionliveqa.R
import session.hyderabad.live.sessionliveqa.adapters.QuestionAdapter
import session.hyderabad.live.sessionliveqa.data.Questions
import session.hyderabad.live.sessionliveqa.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var mFirebaseReference: FirebaseFirestore

    private val mQuestionList by lazy {
        ArrayList<Questions>()
    }

    private var mQuestionAdapter: QuestionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseReference = FirebaseFirestore.getInstance()

        observeNewQuestions()

        fabPostQuestion.setOnClickListener {
            startActivity(Intent(this@MainActivity, PostQuestionActivity::class.java))
        }
    }

    private fun observeNewQuestions() {
        var questions: Questions
        mFirebaseReference.collection(AppConstants.FIRESTORE_QUESTION_COLLECTION)
                .orderBy(AppConstants.Keys.TIMESTAMP)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    querySnapshot?.let {

                        for (question in it.documentChanges) {

                            when(question.type.name) {
                                AppConstants.REMOVED -> {
                                    for (counter in 0.until(mQuestionList.size)) {
                                        if (mQuestionList[counter].id.equals(question.document.id)) {
                                            mQuestionList.removeAt(counter)
                                            mQuestionAdapter?.notifyItemRemoved(counter)
                                            break

                                        }
                                    }
                                }

                                AppConstants.MODIFIED -> {
                                    for (counter in 0.until(mQuestionList.size)) {
                                        if (mQuestionList[counter].id.equals(question.document.id)) {
                                            mQuestionList.removeAt(counter)
                                            questions = question.document.toObject(Questions::class.java)
                                            questions.id = question?.document?.id
                                            mQuestionList.add(questions)
                                            mQuestionAdapter?.updateList(mQuestionList)
                                            break
                                        }
                                    }
                                }

                                else -> {
                                    questions = question.document.toObject(Questions::class.java)
                                    questions.id = question?.document?.id
                                    mQuestionList.add(questions)

                                    if (mQuestionAdapter == null) {
                                        mQuestionAdapter = QuestionAdapter(mQuestionList, getSavedBoolean(AppConstants.Admin.IS_ADMIN), mOnModeClickListener)
                                        rvQuestions.adapter = mQuestionAdapter
                                    } else {
                                        mQuestionAdapter?.updateList(mQuestionList)
                                    }
                                }
                            }
                        }

                       /* when {
                            it.documentChanges[0]?.type?.name == AppConstants.REMOVED -> {
                                for (question in it.documentChanges) {

                                }
                            }
                            it.documentChanges[0]?.type?.name == AppConstants.MODIFIED -> {
                                for (question in it.documentChanges) {

                                }
                            }

                            else -> {
                                for (questionDocument in it.documentChanges) {

                                }


                            }
                        }*/
                    }
                }
    }

    private val mOnModeClickListener = object : QuestionAdapter.OnModeClick {
        override fun onClick(questionAdapterSeal: QuestionAdapterSeal) {
            when (questionAdapterSeal) {

                is Edit -> {
                    val intent = Intent(this@MainActivity, AnswerActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(AppConstants.Keys.ID, questionAdapterSeal.docId)
                    bundle.putString(AppConstants.Keys.ANSWER, questionAdapterSeal.answer)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                is Delete -> {
                    showAlertDialog {
                        setMessage(getString(R.string.alert_message_delete_question))
                        positiveButton {
                            mFirebaseReference.collection(AppConstants.FIRESTORE_QUESTION_COLLECTION)
                                    .document(questionAdapterSeal.docId)
                                    .delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            showToast(getString(R.string.success_toast_question_deleted))
                                        } else {
                                            showToast(getString(R.string.error_toast_delete_question))
                                        }
                                    }
                        }

                        negativeButton { }
                    }
                }

                is Answer -> {
                    val intent = Intent(this@MainActivity, AnswerActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString(AppConstants.Keys.ID, questionAdapterSeal.docId)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.admin -> {
                showToast(getString(R.string.wait_toast_message))
                identifyAdmin()
            }
            R.id.logout -> {
                showAlertDialog {
                    setMessage(getString(R.string.alert_message_logout))
                    positiveButton {

                        clearAllSavedData()
                        AuthUI.getInstance()
                                .signOut(this@MainActivity)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        startActivity(Intent(this@MainActivity, LoginScreenActivity::class.java))
                                        finish()
                                    } else {
                                        showToast(getString(R.string.toast_message_unable_to_logout))
                                    }
                                }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun identifyAdmin() {
        val currentUserEmail = getSavedString(AppConstants.LoginUser.EMAIL)

        mFirebaseReference.collection(AppConstants.FIRESTORE_ADMIN)
                .whereEqualTo(AppConstants.Admin.EMAIL, currentUserEmail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {

                        val adminList = task.result.documents.asSequence().filter { documentSnapshot ->
                            documentSnapshot.data?.get(AppConstants.Admin.EMAIL)?.equals(currentUserEmail)
                                    ?: false
                        }.toList()

                        if (adminList.isNotEmpty()) {
                            rvQuestions.adapter = QuestionAdapter(mQuestionList, true, mOnModeClickListener)
                            saveBoolean(AppConstants.Admin.IS_ADMIN, true)
                        } else {
                            showToast(getString(R.string.toast_message_contact_admin))
                        }
                    }
                }
    }
}
