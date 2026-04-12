package kz.stierlitz.skillcinema.features.register.components

import android.R.attr.contentDescription
import android.R.attr.singleLine
import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = SkillTheme.typography.graphiksRegular,
            fontSize = 16.sp,
            color = Color(0xff555555)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .defaultMinSize(minHeight = 52.dp),
            value = value,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = SkillTheme.typography.graphiksRegular,
                    color = Color(0xffA9ABB7)
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xfff5f4ff),
                unfocusedContainerColor = Color.White,

                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color(0xFFE0E0E0),

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red
            ),
            trailingIcon = {
                if (isPassword) {
                    val icon = if (isPasswordVisible) R.drawable.show else R.drawable.hidden
                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = description,
                        tint = Color(0xFFE0E0E0),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onPasswordVisibilityChange(!isPasswordVisible) }
                            )
                    )
                }
            },
            singleLine = true,
            interactionSource = interactionSource,
            keyboardOptions = keyboardOptions
        )
    }
}