package com.example.znanykonultant.tools

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View

class FormDialogs {

    private var title = ""
    private var message = ""
    var confirmedAction: Boolean = false
    var noPressed: Boolean = false

    private fun info() {
        title = "Informacja"
        message = "Jeżeli jesteś pewien, że zmieniłeś termin kliknij jeszcze raz przycisk " +
                "\n'Poproś o zmianę terminu'"
    }
    private fun delete() {
        title = "Informacja"
        message = "Jesteś pewien, że chcesz anulować spotkanie? "
    }
    private fun nth() {
        title = ""
        message = ""
    }

    /**
     * Create dialogs
     * @params type:
     *         0 - confirm datetime fields
     *         1 - confirm appointment delete
     */
    fun createDialog(context: Context, type : Int): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        when(type){
            0 -> info()
            else -> nth()
        }

        builder.setTitle(title)
        builder.setMessage(message)

        val positiveButtonClick = { _: DialogInterface, _: Int ->
            confirmedAction = true
        }
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        return builder
    }

    fun createYesNoDialog(context: Context, type : Int, positiveButtonClick : (DialogInterface, Int) -> Unit) : AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        when(type){
            0 -> delete()
            else -> nth()
        }

        builder.setTitle(title)
        builder.setMessage(message)

        val noButtonClick = { _: DialogInterface, _: Int ->
            noPressed = true
        }
        builder.setPositiveButton("Tak", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.setNegativeButton("Nie", noButtonClick)

        return builder
    }


}