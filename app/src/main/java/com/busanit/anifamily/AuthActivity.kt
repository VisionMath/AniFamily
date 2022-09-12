package com.busanit.anifamily

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.busanit.anifamily.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.nhn.android.naverlogin.OAuthLogin
import kotlinx.coroutines.*


class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    private lateinit var context: Context

    private var clientId = "2rvp4dUT7gXZQrsrpwJa"
    private var clientSecret = "lajtI04ehF"
    private var clientName = "AniFamily"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        init()

        if (MyApplication.checkAuth() or !MyApplication.nickname.isNullOrBlank()) {
            changeVisibility("login")
        } else {
            changeVisibility("logout")
        }

        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            //구글 로그인 결과 처리...........................
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            MyApplication.email = account.email
                            changeVisibility("login")
                        } else {
                            changeVisibility("logout")
                        }
                    }
            } catch (e: ApiException) {
                changeVisibility("logout")
            }

        }

        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")
        }

        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        binding.googleLoginBtn.setOnClickListener {
            //구글 로그인....................
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)

        }

        binding.signBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) {
                                    Toast.makeText(baseContext, "회원가입에서 성공, 전송된 메일을 확인해 주세요",
                                        Toast.LENGTH_SHORT).show()
                                    changeVisibility("logout")
                                } else {
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT)
                                        .show()
                                    changeVisibility("logout")
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        changeVisibility("logout")
                    }
                }


        }

        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        if (MyApplication.checkAuth()) {
                            MyApplication.email = email
                            changeVisibility("login")
                        } else {
                            Toast.makeText(baseContext,
                                "전송된 메일로 이메일 인증이 되지 않았습니다.",
                                Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun changeVisibility(mode: String) {

        if (mode === "login") {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    while (MyApplication.email.isNullOrBlank())
                        delay(100)
                }
//                Log.d("Rog", "${MyApplication.email}")

                binding.run {
                    authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                    logoutBtn.visibility = View.VISIBLE
                    goSignInBtn.visibility = View.GONE
                    googleLoginBtn.visibility = View.GONE
                    buttonOAuthLoginImg.visibility = View.GONE
                    authEmailEditView.visibility = View.GONE
                    authPasswordEditView.visibility = View.GONE
                    signBtn.visibility = View.GONE
                    loginBtn.visibility = View.GONE
                }
            }

        } else if (mode === "logout") {
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                buttonOAuthLoginImg.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        } else if (mode === "signin") {
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                buttonOAuthLoginImg.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }
    }

    private fun init() {
        NaverIdLoginSDK.apply {
            showDevelopersLog(true)
            initialize(context, clientId, clientSecret, clientName)
            isShowMarketLink = true
            isShowBottomTab = true
        }

//        var naverToken :String? = ""
        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                MyApplication.email = response.profile?.email
                MyApplication.nickname = response.profile?.nickname
                Toast.makeText(context, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(context, "errorCode: ${errorCode}\n" +
                        "errorDescription: $errorDescription", Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        binding.buttonOAuthLoginImg.setOAuthLoginCallback(object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(profileCallback)
                changeVisibility("login")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    context,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
                changeVisibility("logout")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

        })
        // 로그인
//        binding.login.setOnClickListener {
//            NaverIdLoginSDK.behavior = NidOAuthBehavior.DEFAULT
//            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    updateView()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//            })
//        }
//
//        // 로그아웃
//        binding.logout.setOnClickListener {
//            NaverIdLoginSDK.logout()
//            updateView()
//        }
//
//        // 연동 끊기
//        binding.deleteToken.setOnClickListener {
//            NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    updateView()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    updateView()
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//            })
//        }
//
//        // 토큰 갱신
//        binding.refreshToken.setOnClickListener {
//            NidOAuthLogin().callRefreshAccessTokenApi(context, object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    updateView()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    updateView()
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//
//            })
//        }
//
//        // Api 호출
//        binding.profileApi.setOnClickListener {
//            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
//                override fun onSuccess(response: NidProfileResponse) {
//                    Toast.makeText(
//                        context,
//                        "$response",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    binding.tvApiResult.text = response.toString()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    binding.tvApiResult.text = ""
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//            })
//
//        }
//
//        // 네이버앱 로그인
//        binding.loginWithNaverapp.setOnClickListener {
//            NaverIdLoginSDK.behavior = NidOAuthBehavior.NAVERAPP
//            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    updateView()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//
//            })
//        }
//
//        // 커스텀탭 로그인
//        binding.loginWithCustomtabs.setOnClickListener {
//            NaverIdLoginSDK.behavior = NidOAuthBehavior.CUSTOMTABS
////            OAuthLogin.getInstance().setCustomTabReAuth(false) // 무조건 재인증시 true
//            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    updateView()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//
//            })
//        }
//
//        // 웹뷰 로그인
//        binding.loginWithWebView.setOnClickListener {
//            NaverIdLoginSDK.behavior = NidOAuthBehavior.WEBVIEW
//            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    updateView()
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Toast.makeText(
//                        context,
//                        "errorCode:$errorCode, errorDesc:$errorDescription",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//
//            })
//        }
//
//        // ClientSpinner
//
//        val oauthClientSpinnerAdapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.client_list,
//            android.R.layout.simple_spinner_item
//        )
//        oauthClientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        binding.consumerListSpinner.prompt = "샘플에서 이용할 client 를 선택하세요"
//        binding.consumerListSpinner.adapter = oauthClientSpinnerAdapter
//        binding.consumerListSpinner.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(parents: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//                Toast.makeText(this@MainActivity,
//                    oauthClientSpinnerAdapter.getItem(pos).toString() + "가 선택됨",
//                    Toast.LENGTH_SHORT
//                ).show()
//                if (oauthClientSpinnerAdapter.getItem(pos) == "네이버아이디로로그인") {
//                    clientId = "jyvqXeaVOVmV"
//                    clientSecret = "527300A0_COq1_XV33cf"
//                    clientName = "네이버 아이디로 로그인"
//                } else if (oauthClientSpinnerAdapter.getItem(pos) == "소셜게임(12G)") {
//                    clientId = "5875kZ1sZ_aL"
//                    clientSecret = "509C949A_yi7jOzKU4Pg"
//                    clientName = "소셜게임"
//                } else if (oauthClientSpinnerAdapter.getItem(pos) == "ERROR_NO_NAME") {
//                    clientId= "5875kZ1sZ_aL"
//                    clientSecret = "509C949A_yi7jOzKU4Pg"
//                    clientName = ""
//                } else if (oauthClientSpinnerAdapter.getItem(pos) == "ERROR_CLIENT_ID") {
//                    clientId = "5875kZ1sZ_a"
//                    clientSecret = "509C949A_yi7jOzKU4Pg"
//                    clientName = "ERROR_CLIENT_ID"
//                } else if (oauthClientSpinnerAdapter.getItem(pos) == "ERROR_CLIENT_SECRET") {
//                    clientId = "jyvqXeaVOVmV"
//                    clientSecret = "509C949Ayi7jOzKU4Pg"
//                    clientName = "ERROR_CLIENT_SECRET"
//                } else {
//                    return
//                }
//                updateUserData()
//                NaverIdLoginSDK.initialize(context, clientId, clientSecret, clientName)
//            }
//
//            override fun onNothingSelected(parents: AdapterView<*>?) {
//                // do nothing
//            }
//
//        }
//
//        // Client 정보 변경
//        binding.buttonOAuthInit.setOnClickListener {
//            clientId = binding.oauthClientid.text.toString()
//            clientSecret = binding.oauthClientsecret.text.toString()
//            clientName = binding.oauthClientname.text.toString()
//
//            NaverIdLoginSDK.initialize(context, clientId, clientSecret, clientName)
//
//            updateUserData()
//
//        }
//
//        updateUserData()
//    }
//
//    private fun updateView() {
//        binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
//        binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
//        binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
//        binding.tvType.text = NaverIdLoginSDK.getTokenType()
//        binding.tvState.text = NaverIdLoginSDK.getState().toString()
//    }
//
//    private fun updateUserData() {
//        binding.oauthClientid.setText(clientId)
//        binding.oauthClientsecret.setText(clientSecret)
//        binding.oauthClientname.setText(clientName)
//    }

    }
}