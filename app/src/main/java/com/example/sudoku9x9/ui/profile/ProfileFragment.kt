package com.example.sudoku9x9.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.databinding.FragmentProfileBinding
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG_EMAIL
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG_PASSWORD
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REQUEST_REGISTRATION
import com.google.firebase.auth.FirebaseAuth

const val PICTURE_REQUEST = 1

class ProfileFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory((requireActivity().application as SudokuApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()


        binding.profileCardAvatar.setOnClickListener {
            val picture = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            startActivityForResult(picture, PICTURE_REQUEST)

        }

        binding.profileCardEmail.setOnClickListener {
            Log.e("mmmm", "1 - profileCardEmail")
            // 1 - Вызываем диалоговое окно с вводом номера телефона
            val reg = UserRegistrationDialogFragment()
            reg.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ClassicSelectionDialog)
            reg.show(parentFragmentManager, REG)

            parentFragmentManager.setFragmentResultListener(
                REQUEST_REGISTRATION,
                viewLifecycleOwner,
                FragmentResultListener { requestKey, result ->
                    //2 - Получаем номер телефона и отправляем на него пароль
                    //3 - Или получаем адрес почты и отправляем в AuthFirebase
                    Log.e("mmmm", "2 - result-$result")
                    sendUserData(result.getString(REG_EMAIL),result.getString(REG_PASSWORD))
                    reg.dismiss()
                }
            )
        }
        // Если почта успешно добавилась,




        return binding.root
    }

    private fun sendUserData(email: String?,password:String?) {

        if(email == null || password == null) return

        if(email.isEmpty() || password.isEmpty()) return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        user.uid
                        Log.e("sasasa","2  -  email-$email  password-$password  user.uid-${user.uid}")
                        viewModel.saveRegistration(uid = user.uid, email = email,password = password)
                    }
                    Log.e("sasasa","1  -  email-$email  password-$password")
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == PICTURE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
                it.data
                Log.e("oooo", "1 -  it.data-${it.data}")
            }
        }
    }


}