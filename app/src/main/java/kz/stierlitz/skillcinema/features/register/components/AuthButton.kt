package kz.stierlitz.skillcinema.features.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun AuthButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier
            .background(Color.White)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0x82879626).copy(alpha = 0.15f)
        )
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            colors = ButtonColors(
                containerColor = Color(0xff3D3BFF),
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            enabled = enabled,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = text,
                style = SkillTheme.typography.graphiksBold,
                fontSize = 16.sp,
            )
        }
        Spacer(Modifier.height(10.dp))
    }
}