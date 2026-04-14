package kz.stierlitz.skillcinema.features.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.features.register.RegistrationContract
import kz.stierlitz.skillcinema.features.register.components.AuthButton
import kz.stierlitz.skillcinema.features.register.components.AuthTextField
import kz.stierlitz.skillcinema.features.register.components.GoogleButton
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun LoginScreen(
    state: LoginContract.State,
    effectFlow: Flow<LoginContract.SideEffect>,
    onIntent: (LoginContract.Intent) -> Unit,
    onNavigateToLoader: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        effectFlow.collectLatest { effect ->
            when (effect) {
                is LoginContract.SideEffect.NavigateToLoader -> onNavigateToLoader()
                is LoginContract.SideEffect.NavigateToRegister -> onNavigateToRegister()
                is LoginContract.SideEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .padding(horizontal = 26.dp),
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_title),
                    contentDescription = "Skill cinema",
                    modifier = Modifier
                        .width(120.dp)
                        .height(18.24152183532715.dp)
                )
            }

            Spacer(modifier = Modifier.height(55.dp))
            Image(
                painter = painterResource(id = R.drawable.onboarding1),
                contentDescription = "Onboarding image 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .padding(20.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "С возвращением!",
                style = SkillTheme.typography.graphiksMedium,
                fontWeight = FontWeight.W500,
                color = Color(0xFF272727),
                fontSize = 32.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Войдите в свой аккаунт",
                style = SkillTheme.typography.graphiksRegular,
                color = Color(0xffA9ABB7),
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))

            AuthTextField(
                label = "EMAIL",
                value = state.email,
                placeholder = "example@mail.com",
                onValueChange = { onIntent(LoginContract.Intent.OnEmailChange(it)) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextField(
                label = "ПАРОЛЬ",
                value = state.password,
                placeholder = "Введите пароль",
                isPassword = true,
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityChange = { onIntent(LoginContract.Intent.OnTogglePasswordVisibility(it)) },
                onValueChange = { onIntent(LoginContract.Intent.OnPasswordChange(it)) },
            )
            Spacer(modifier = Modifier.height(16.dp))

            AuthButton(
                text = "Войти",
                onClick = { onIntent(LoginContract.Intent.OnLoginWithEmailClick) },
                enabled = !state.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Нет аккаунта?",
                    style = SkillTheme.typography.graphiksRegular,
                    color = Color.Gray,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Зарегистрироваться",
                    style = SkillTheme.typography.graphiksRegular,
                    color = Color(0xff3D3BFF),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onNavigateToRegister()
                    }
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(
                    color = Color(0xFFeaf2e6),
                    thickness = 1.dp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "или войти через",
                    style = SkillTheme.typography.graphiksRegular,
                    color = Color(0xFFbbbbbb),
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(
                    color = Color(0xFFeaf2e6),
                    thickness = 1.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            GoogleButton(
                onClick = onGoogleSignInClick,
            )
        }
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Вход", style = MaterialTheme.typography.titleLarge)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = state.email,
//            onValueChange = { onIntent(LoginContract.Intent.OnEmailChange(it)) },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = state.password,
//            onValueChange = { onIntent(LoginContract.Intent.OnPasswordChange(it)) },
//            label = { Text("Пароль") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = { onIntent(LoginContract.Intent.OnLoginWithEmailClick) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !state.isLoading
//        ) {
//            Text("Войти")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(
//            onClick = onGoogleSignInClick,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Вход через Google")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextButton(onClick = onNavigateToRegister) {
//            Text("Нет аккаунта? Зарегистрироваться")
//        }
//
//        if (state.isLoading) {
//            Spacer(modifier = Modifier.height(16.dp))
//            CircularProgressIndicator()
//        }
//    }
}

