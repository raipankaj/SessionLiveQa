package session.hyderabad.live.sessionliveqa.utils

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

fun EditText.trimmedText() = this.text.toString().trim()

fun AppCompatActivity.showToast(message: String, toastLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, toastLength).show()
}

fun ViewGroup.loadAdapterLayout(layoutId: Int) =
        LayoutInflater.from(this.context).inflate(layoutId, this, false)

fun AppCompatActivity.showAlertDialog(dialogBuilder: AlertDialog.Builder.() -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.dialogBuilder()
    val dialog = builder.create()

    dialog.show()
}

fun AlertDialog.Builder.positiveButton(text: String = "Okay", handleClick: (which: Int) -> Unit = {}) {
    this.setPositiveButton(text, { dialogInterface, which -> handleClick(which) })
}

fun AlertDialog.Builder.negativeButton(text: String = "Cancel", handleClick: (which: Int) -> Unit = {}) {
    this.setNegativeButton(text, { dialogInterface, which -> handleClick(which) })
}