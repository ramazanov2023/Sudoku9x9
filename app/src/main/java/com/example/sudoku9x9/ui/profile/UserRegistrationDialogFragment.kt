package com.example.sudoku9x9.ui.profile

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.example.sudoku9x9.R


class UserRegistrationDialogFragment:DialogFragment() {
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.dialog_fragment_user_registration,container,false)
        inputEmail = root.findViewById(R.id.reg_email)
        inputPassword = root.findViewById(R.id.reg_password)
        signUpButton = root.findViewById(R.id.reg_sign_up_button)

        signUpButton.setOnClickListener {
            setResult(inputEmail.text.toString(),inputPassword.text.toString())
        }

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView)
            .hide(WindowInsetsCompat.Type.navigationBars())
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun setResult(email:String,password:String){
        parentFragmentManager.setFragmentResult(REQUEST_REGISTRATION, bundleOf(Pair(REG_EMAIL,email),Pair(REG_PASSWORD,password)))
    }

    companion object{
        val REG = UserRegistrationDialogFragment::class.java.simpleName
        val REQUEST_REGISTRATION = "UserRegistration"
        val REG_PASSWORD = "UserPassword"
        val REG_EMAIL = "UserEmail"
    }
}