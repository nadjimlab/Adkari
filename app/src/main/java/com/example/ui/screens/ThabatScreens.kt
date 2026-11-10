package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ThabatState
import com.example.ui.viewmodel.ThabatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Emerald = Color(0xFF064E3B)
private val Gold = Color(0xFFD4AF37)
private val Ivory = Color(0xFFF8F6EF)

@Composable
fun ThabatApp(onOpenAdhkar: () -> Unit, viewModel: ThabatViewModel) {
    var selected by remember { mutableStateOf(0) }
    val items = listOf("الرئيسية" to Icons.Default.Home, "الصلاة" to Icons.Default.NightsStay, "ثباتي" to Icons.Default.Favorite, "القرآن" to Icons.Default.MenuBook, "حسابي" to Icons.Default.Person)
    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
            items.forEachIndexed { index, item -> NavigationBarItem(selected = selected == index, onClick = { selected = index }, icon = { Icon(item.second, null) }, label = { Text(item.first) }) }
        }
    }) { padding ->
        when (selected) {
            0 -> ThabatHomeScreen(viewModel, padding, onOpenAdhkar)
            1 -> PrayerScreen(viewModel, padding)
            2 -> RecoveryScreen(viewModel, padding)
            3 -> QuranScreen(padding, onOpenAdhkar)
            else -> AccountScreen(padding)
        }
    }
}

@Composable
private fun ThabatHomeScreen(viewModel: ThabatViewModel, padding: androidx.compose.foundation.layout.PaddingValues, onOpenAdhkar: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val date = SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar")).format(Date())
    LazyColumn(Modifier.fillMaxSize().background(Ivory).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("السلام عليكم", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Emerald); Text("رحلتك نحو الصلاة، الطمأنينة، والتغيير.", color = Color.DarkGray); Text(date, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
        item { Card(colors = CardDefaults.cardColors(containerColor = Emerald), shape = RoundedCornerShape(24.dp)) { Column(Modifier.padding(20.dp)) { Text("طمأنينة اليوم", color = Gold, fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); Text("واذكر ربك إذا نسيت", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); Text("خطوة صغيرة اليوم تصنع ثباتًا كبيرًا غدًا.", color = Color.White.copy(alpha = .8f)) } } }
        item { SectionTitle("لوحة اليوم") }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) { StatCard("الصلاة", "${state.prayers.completed}/5", Icons.Default.NightsStay, Modifier.weight(1f)); StatCard("رحلتي", "اليوم ${state.journeyDay}", Icons.Default.Favorite, Modifier.weight(1f)) } }
        item { Card(shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(16.dp)) { Text("الخطوة التالية", fontWeight = FontWeight.Bold); Text("أكمل وردك من القرآن أو أذكارك اليومية.", color = Color.Gray); Spacer(Modifier.height(10.dp)); Button(onClick = onOpenAdhkar, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.SelfImprovement, null); Text("فتح الأذكار") } } } }
        item { JourneyCard(state, viewModel) }
        item { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)), shape = RoundedCornerShape(18.dp)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Warning, null, tint = Gold); Spacer(Modifier.size(10.dp)); Column { Text("تحتاج دعمًا الآن؟", fontWeight = FontWeight.Bold); Text("استخدم خطة الطوارئ بهدوء ودون لوم.", color = Color.DarkGray) } } } }
    }
}

@Composable private fun JourneyCard(state: ThabatState, viewModel: ThabatViewModel) { Card(shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(16.dp)) { Text("رحلة 30 يومًا", fontWeight = FontWeight.Bold); Text("اليوم ${state.journeyDay} من 30", color = Emerald); Spacer(Modifier.height(8.dp)); Text(when (state.journeyDay) { in 1..7 -> "العودة إلى الصلوات اليومية"; in 8..14 -> "إزالة المحفزات وتحسين العادات الرقمية"; in 15..21 -> "تقوية القرآن والأذكار"; else -> "بناء انضباط روحي مستدام" }); Spacer(Modifier.height(8.dp)); Button(onClick = viewModel::completeToday, enabled = !state.completedDays.contains(state.journeyDay), modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.CheckCircle, null); Text(if (state.completedDays.contains(state.journeyDay)) "تم إنجاز اليوم" else "أنجز مهمة اليوم") } } } }

@Composable private fun PrayerScreen(viewModel: ThabatViewModel, padding: androidx.compose.foundation.layout.PaddingValues) { val state by viewModel.state.collectAsState(); val names = listOf("الفجر" to "05:10", "الظهر" to "12:20", "العصر" to "15:45", "المغرب" to "18:30", "العشاء" to "20:00"); LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Text("الصلاة", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("سجّل صلواتك بلطف، فالثبات رحلة.", color = Color.Gray); Spacer(Modifier.height(4.dp)); Text("الأوقات المعروضة تقريبية — اختر مدينتك من الحساب لضبطها.", style = MaterialTheme.typography.bodySmall, color = Gold) }; itemsIndexed(names) { index, (name, time) -> val checked = listOf(state.prayers.fajr, state.prayers.dhuhr, state.prayers.asr, state.prayers.maghrib, state.prayers.isha)[index]; Card(onClick = { viewModel.togglePrayer(index) }, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = if (checked) Color(0xFFE4F1EA) else MaterialTheme.colorScheme.surface)) { Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) { Icon(if (checked) Icons.Default.CheckCircle else Icons.Default.NightsStay, null, tint = if (checked) Emerald else Color.Gray, modifier = Modifier.size(28.dp)); Column(Modifier.weight(1f).padding(horizontal = 14.dp)) { Text(name, fontWeight = FontWeight.Bold); Text(time, color = Color.Gray) }; Text(if (checked) "تمت" else "تسجيل", color = if (checked) Emerald else Gold, fontWeight = FontWeight.Bold) } } } } }

@Composable private fun RecoveryScreen(viewModel: ThabatViewModel, padding: androidx.compose.foundation.layout.PaddingValues) { val state by viewModel.state.collectAsState(); var emergency by remember { mutableStateOf(false) }; var relapse by remember { mutableStateOf(false) }; LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { item { Text("ثباتي", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("رحلة التعافي", color = Emerald, fontWeight = FontWeight.Bold) }; item { Card(colors = CardDefaults.cardColors(containerColor = Emerald), shape = RoundedCornerShape(24.dp)) { Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("أيام الثبات الحالية", color = Color.White); Text("${state.recovery.streak}", color = Gold, fontSize = 52.sp, fontWeight = FontWeight.Bold); Text("التقدم محفوظ محليًا على جهازك", color = Color.White.copy(alpha = .8f)); Spacer(Modifier.height(10.dp)); Button(onClick = viewModel::markRecoveryDay) { Text("تسجيل يوم ثابت") } } } }; item { OutlinedButton(onClick = { emergency = true }, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Warning, null); Text("أنا أواجه رغبة الآن") } }; item { Card(shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(16.dp)) { Text("التقدم الكلي", fontWeight = FontWeight.Bold); Text("${state.recovery.lifetimeDays} يومًا محفوظًا عبر رحلتك", color = Color.Gray); Spacer(Modifier.height(8.dp)); Text("الانتكاسة لا تمسح إنجازاتك السابقة. سجّلها بهدوء وابدأ من جديد.", color = Color.DarkGray); TextButton(onClick = { relapse = true }) { Text("تسجيل حدث خاص") } } } } }; if (emergency) EmergencyDialog { emergency = false }; if (relapse) RelapseDialog(onDismiss = { relapse = false }, onSave = { viewModel.recordRelapse(it); relapse = false }) }

@Composable private fun EmergencyDialog(onDismiss: () -> Unit) { AlertDialog(onDismissRequest = onDismiss, title = { Text("خطة هادئة لهذه اللحظة") }, text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { listOf("أغلق المحتوى المحفّز", "غادر المكان الحالي", "ضع الهاتف بعيدًا", "تنفّس ببطء لدقيقتين", "امشِ أو اشغل نفسك بنشاط آخر", "اقرأ تذكيرًا روحيًا إن رغبت").forEachIndexed { i, text -> Text("${i + 1}. $text") } } }, confirmButton = { Button(onClick = onDismiss) { Text("سأبدأ الآن") } }) }

@Composable private fun RelapseDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) { var selected by remember { mutableStateOf("توتر") }; val options = listOf("وحدة", "توتر", "ملل", "ليل", "وسائل التواصل", "أخرى"); AlertDialog(onDismissRequest = onDismiss, title = { Text("تسجيل خاص دون لوم") }, text = { Column { Text("ما المحفز الأقرب؟"); options.forEach { FilterChip(selected = selected == it, onClick = { selected = it }, label = { Text(it) }, modifier = Modifier.padding(vertical = 3.dp)) } } }, confirmButton = { Button(onClick = { onSave(selected) }) { Text("حفظ") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء") } }) }

@Composable private fun QuranScreen(padding: androidx.compose.foundation.layout.PaddingValues, onOpenAdhkar: () -> Unit) { Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { Text("القرآن والأذكار", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Emerald)) { Column(Modifier.padding(20.dp)) { Icon(Icons.Default.MenuBook, null, tint = Gold, modifier = Modifier.size(36.dp)); Spacer(Modifier.height(8.dp)); Text("وردك اليومي", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); Text("ابدأ بصفحة واحدة بقلب حاضر.", color = Color.White.copy(alpha = .8f)); Spacer(Modifier.height(12.dp)); Button(onClick = onOpenAdhkar) { Icon(Icons.Default.PlayArrow, null); Text("فتح المحتوى المتاح") } } }; Card { Column(Modifier.padding(16.dp)) { Text("أذكار الصباح والمساء", fontWeight = FontWeight.Bold); Text("محتوى موثق وموجود في التطبيق، مع حفظ تقدم القراءة دون اتصال.", color = Color.Gray); Spacer(Modifier.height(8.dp)); OutlinedButton(onClick = onOpenAdhkar) { Text("استمرار القراءة") } } } }
}

@Composable private fun AccountScreen(padding: androidx.compose.foundation.layout.PaddingValues) { Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("حسابي", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("استخدام محلي وخصوصية أولًا", color = Emerald, fontWeight = FontWeight.Bold); listOf(Icons.Default.Notifications to "التذكيرات", Icons.Default.DarkMode to "المظهر", Icons.Default.Person to "المدينة وطريقة حساب الصلاة", Icons.Default.Book to "تصدير بياناتي").forEach { (icon, title) -> Card { Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = Emerald); Spacer(Modifier.size(14.dp)); Text(title, fontWeight = FontWeight.Medium) } } }; Text("لا تحتاج إلى إنشاء حساب. تبقى سجلات التعافي الخاصة على جهازك افتراضيًا.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) } }

@Composable private fun SectionTitle(text: String) { Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Emerald) }
@Composable private fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier) { Card(modifier, shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(16.dp)) { Icon(icon, null, tint = Gold); Spacer(Modifier.height(8.dp)); Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(title, color = Color.Gray) } } }
