package kz.stierlitz.skillcinema.presentation.register.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun GoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier
            .background(Color.White)
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF333333),
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            enabled = enabled,
            border = BorderStroke(1.dp, Color(0xFFe5e5e5))
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = kz.stierlitz.skillcinema.R.drawable.ic_google),
                    contentDescription = "Google icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = "Google",
                    style = SkillTheme.typography.graphiksMedium,
                    fontSize = 16.sp,
                )
            }
        }
    }
}