package kz.stierlitz.skillcinema.presentation.film.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.domain.model.Staff
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun StaffListRow(staffList: List<Staff>, onStaffClick: (Int) -> Unit = {}) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(staffList.take(20).chunked(4)) { columnStaff ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                columnStaff.forEach { staff ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(200.dp)
                            .clickable { onStaffClick(staff.staffId) }
                    ) {
                        AsyncImage(
                            model = staff.posterUrl,
                            contentDescription = staff.nameRu,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(48.dp)
                                .height(68.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = staff.nameRu ?: staff.nameEn ?: "", fontSize = 14.sp, style = SkillTheme.typography.graphiksRegular, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(Modifier.height(4.dp))
                            Text(text = staff.description ?: staff.professionText ?: "", fontSize = 12.sp, color = Color(0xFF838390), style = SkillTheme.typography.graphiksRegular, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}
