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
import androidx.lifecycle.Observer
import com.example.sudoku9x9.R
import com.example.sudoku9x9.SudokuApplication
import com.example.sudoku9x9.data.remote.UserProfile
import com.example.sudoku9x9.databinding.FragmentProfileBinding
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG_EMAIL
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG_NICKNAME
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REG_PASSWORD
import com.example.sudoku9x9.ui.profile.UserRegistrationDialogFragment.Companion.REQUEST_REGISTRATION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

const val PICTURE_REQUEST = 1

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var myRef: DatabaseReference


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
        storage = FirebaseStorage.getInstance()
//        https://sudoku9x9-276cf-default-rtdb.europe-west1.firebasedatabase.app/
        val database =
            FirebaseDatabase.getInstance("https://sudoku9x9-276cf-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference().child("sudoku")

//        myRef.setValue("Hello, World!")
//        myRef.child("sudoku").child("players")
//            .child("player_2").child("profile")
//            .child("nickname").setValue("DarkDessert")
//        myRef.child("players").child("player_3").child("profile")
//            .setValue(UserProfile(nickname = "BlueMars_69", gender = "female"))

        binding.profileCardAvatar.setOnClickListener {
            val picture = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            startActivityForResult(picture, PICTURE_REQUEST)
        }

        viewModel.profileData.observe(viewLifecycleOwner, Observer {
            Log.e("oooo", "4 - profileData-$it")
        })

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
                    sendUserData(
                        result.getString(REG_EMAIL),
                        result.getString(REG_PASSWORD),
                        result.getString(REG_NICKNAME)
                    )
                    reg.dismiss()
                }
            )
        }
        // Если почта успешно добавилась,

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }

    private fun sendUserData(email: String?, password: String?, nickname: String?) {

        if (email == null || password == null || nickname == null) return

//        if(email.isEmpty() || password.isEmpty()) return


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        user.uid
                        Log.e(
                            "search_null",
                            "1  -  nickname-$nickname  email-$email  password-$password  user.uid-${user.uid}"
                        )
                        val time = System.currentTimeMillis()
                        val userProfile = UserProfile(
                            userId = user.uid,
                            nickname = nickname,
                            email = email,
                            password = password,
                            signUp = true,
                            signUpTime = time,
                            country = "USA"
                        )
                        myRef.child("players").child(user.uid)
                            .child("profile")
                            .setValue(userProfile)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Log.e(
                                        "search_null",
                                        "2  -  nickname-$nickname  email-$email  password-$password  user.uid-${user.uid}"
                                    )
                                    viewModel.saveRegistration(
                                        uid = user.uid,
                                        nickname = nickname,
                                        email = email,
                                        password = password,
                                        signUp = true,
                                        signUpTime = time,
                                        country = "USA"
                                    )
                                }
                            }
                        Log.e(
                            "search_null",
                            "5  -  nickname-$nickname  email-$email  password-$password  user.uid-${user.uid}"
                        )
                    }
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
                val localAvatarUri = it.data
                Log.e("oooo", "1 -  it.data-${it.data} ")
                // Загружаем картинку в FirebaseStorage
                val player = storage.reference.child("players").child("123456")
                val userAvatar = player.child("test_avatar.jpg")

                // Получаем ее адрес
                // Передаем этот адрес в локальную базу данных
                localAvatarUri?.let {
                    userAvatar.putFile(it).addOnFailureListener {
                        Log.e("search_null", "6  -  localAvatarUri-$localAvatarUri")
                        viewModel.updateUserProfile(userAvatar = localAvatarUri.toString(), id = 1)
                    }.addOnSuccessListener {


                        val uriTask = userAvatar.downloadUrl
                        uriTask.addOnCompleteListener {
                            if (it.isSuccessful) {
//                                it.result

                                Log.e("search_null", "7  -  uri-${it.result}")
                                viewModel.updateUserProfile(it.result.toString(), id = 1)
                            } else {
                                null
                            }
                        }

                    }
                }


            }
        }
    }


}