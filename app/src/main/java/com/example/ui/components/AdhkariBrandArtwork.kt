package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldMedium
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

/**
 * Custom-drawn Vector & Canvas emblem of the Adhkari App Logo:
 * - Golden Islamic Mihrab Arch
 * - Crescent Moon & Minarets Silhouette
 * - Open Holy Quran with Golden Light Pages
 * - "أذكاري" Calligraphy and Leaf Accents
 */
@Composable
fun AdhkariEmblemLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    showArabicText: Boolean = true
) {
    Column(
        modifier = modifier.testTag("adhkari_emblem_logo"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .shadow(10.dp, RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0F5A44),
                            Color(0xFF073B2C),
                            Color(0xFF031D15)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = this.size.width
                val h = this.size.height

                // 1. Draw subtle golden border of the badge
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFE5C158), Color(0xFF9E7722), Color(0xFFE5C158))
                    ),
                    style = Stroke(width = w * 0.02f),
                    cornerRadius = CornerRadius(w * 0.2f)
                )

                // 2. Draw Golden Mihrab Arch Frame
                val archPath = Path().apply {
                    val archLeft = w * 0.22f
                    val archRight = w * 0.78f
                    val archTop = h * 0.12f
                    val archBottom = h * 0.70f
                    val archMidX = w * 0.5f

                    moveTo(archLeft, archBottom)
                    lineTo(archLeft, archTop + (archBottom - archTop) * 0.45f)
                    // Arch curve
                    cubicTo(
                        archLeft, archTop + (archBottom - archTop) * 0.15f,
                        archMidX - w * 0.12f, archTop,
                        archMidX, archTop
                    )
                    cubicTo(
                        archMidX + w * 0.12f, archTop,
                        archRight, archTop + (archBottom - archTop) * 0.15f,
                        archRight, archTop + (archBottom - archTop) * 0.45f
                    )
                    lineTo(archRight, archBottom)
                    close()
                }

                // Arch inner background (richer night emerald)
                drawPath(
                    path = archPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF042B21), Color(0xFF0B4E3C))
                    )
                )

                // Arch gold stroke
                drawPath(
                    path = archPath,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFEEAA), Color(0xFFD4A338), Color(0xFF8C6615))
                    ),
                    style = Stroke(width = w * 0.028f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 3. Mosque silhouettes inside arch
                val domePath = Path().apply {
                    val midX = w * 0.5f
                    val baseY = h * 0.62f
                    val domeRadius = w * 0.12f
                    moveTo(midX - domeRadius, baseY)
                    cubicTo(
                        midX - domeRadius, baseY - domeRadius * 1.3f,
                        midX + domeRadius, baseY - domeRadius * 1.3f,
                        midX + domeRadius, baseY
                    )
                    close()
                }
                drawPath(domePath, color = Color(0xFF063327))

                // Minarets
                drawRect(
                    color = Color(0xFF063327),
                    topLeft = Offset(w * 0.32f, h * 0.38f),
                    size = Size(w * 0.038f, h * 0.24f)
                )
                drawRect(
                    color = Color(0xFF063327),
                    topLeft = Offset(w * 0.64f, h * 0.38f),
                    size = Size(w * 0.038f, h * 0.24f)
                )

                // 4. Glowing Crescent in the arch
                val crescentCenter = Offset(w * 0.5f, h * 0.26f)
                val crescentRadius = w * 0.075f
                val crescentPath = Path().apply {
                    addOval(
                        androidx.compose.ui.geometry.Rect(
                            center = crescentCenter,
                            radius = crescentRadius
                        )
                    )
                }
                val cutCrescentPath = Path().apply {
                    addOval(
                        androidx.compose.ui.geometry.Rect(
                            center = Offset(crescentCenter.x + crescentRadius * 0.45f, crescentCenter.y - crescentRadius * 0.25f),
                            radius = crescentRadius * 0.85f
                        )
                    )
                }
                val finalCrescent = Path().apply {
                    op(crescentPath, cutCrescentPath, androidx.compose.ui.graphics.PathOperation.Difference)
                }
                drawPath(
                    finalCrescent,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFF7D6), Color(0xFFFFD466))
                    )
                )

                // Stars dots in the arch
                drawCircle(Color(0xFFFFEEAA), radius = w * 0.009f, center = Offset(w * 0.38f, h * 0.28f))
                drawCircle(Color(0xFFFFEEAA), radius = w * 0.007f, center = Offset(w * 0.62f, h * 0.32f))
                drawCircle(Color(0xFFFFEEAA), radius = w * 0.008f, center = Offset(w * 0.44f, h * 0.34f))

                // 5. Open Quran (Holy Book) Base with Glowing Pages
                val bookSpine = Offset(w * 0.5f, h * 0.68f)
                val bookLeftWing = Offset(w * 0.24f, h * 0.60f)
                val bookRightWing = Offset(w * 0.76f, h * 0.60f)
                val bookBottomLeft = Offset(w * 0.28f, h * 0.76f)
                val bookBottomRight = Offset(w * 0.72f, h * 0.76f)
                val bookBottomMid = Offset(w * 0.5f, h * 0.79f)

                // Gold Quran stand / cover
                val bookCoverPath = Path().apply {
                    moveTo(bookLeftWing.x - w * 0.02f, bookLeftWing.y)
                    lineTo(bookBottomLeft.x - w * 0.02f, bookBottomLeft.y + h * 0.02f)
                    lineTo(bookBottomMid.x, bookBottomMid.y + h * 0.02f)
                    lineTo(bookBottomRight.x + w * 0.02f, bookBottomRight.y + h * 0.02f)
                    lineTo(bookRightWing.x + w * 0.02f, bookRightWing.y)
                    lineTo(bookSpine.x, bookSpine.y)
                    close()
                }
                drawPath(
                    path = bookCoverPath,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFE082), Color(0xFFD4A02A), Color(0xFF8C6615))
                    )
                )

                // Left Page (Glowing Cream/Gold)
                val leftPagePath = Path().apply {
                    moveTo(bookSpine.x, bookSpine.y - h * 0.02f)
                    cubicTo(
                        w * 0.42f, h * 0.56f,
                        w * 0.30f, h * 0.58f,
                        bookLeftWing.x, bookLeftWing.y
                    )
                    lineTo(bookBottomLeft.x, bookBottomLeft.y)
                    cubicTo(
                        w * 0.32f, h * 0.73f,
                        w * 0.42f, h * 0.72f,
                        bookBottomMid.x, bookBottomMid.y
                    )
                    close()
                }
                drawPath(
                    path = leftPagePath,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFFDF5), Color(0xFFFFF2D0), Color(0xFFE8D4A2))
                    )
                )

                // Right Page (Glowing Cream/Gold)
                val rightPagePath = Path().apply {
                    moveTo(bookSpine.x, bookSpine.y - h * 0.02f)
                    cubicTo(
                        w * 0.58f, h * 0.56f,
                        w * 0.70f, h * 0.58f,
                        bookRightWing.x, bookRightWing.y
                    )
                    lineTo(bookBottomRight.x, bookBottomRight.y)
                    cubicTo(
                        w * 0.68f, h * 0.73f,
                        w * 0.58f, h * 0.72f,
                        bookBottomMid.x, bookBottomMid.y
                    )
                    close()
                }
                drawPath(
                    path = rightPagePath,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFFDF5), Color(0xFFFFF2D0), Color(0xFFE8D4A2))
                    )
                )

                // Quran central gold spine line
                drawLine(
                    color = Color(0xFFC89B3C),
                    start = Offset(bookSpine.x, bookSpine.y - h * 0.02f),
                    end = bookBottomMid,
                    strokeWidth = w * 0.018f,
                    cap = StrokeCap.Round
                )

                // Subtle text lines on pages
                drawLine(
                    color = Color(0x33B88A22),
                    start = Offset(w * 0.32f, h * 0.64f),
                    end = Offset(w * 0.44f, h * 0.63f),
                    strokeWidth = w * 0.01f
                )
                drawLine(
                    color = Color(0x33B88A22),
                    start = Offset(w * 0.30f, h * 0.68f),
                    end = Offset(w * 0.43f, h * 0.67f),
                    strokeWidth = w * 0.01f
                )
                drawLine(
                    color = Color(0x33B88A22),
                    start = Offset(w * 0.56f, h * 0.63f),
                    end = Offset(w * 0.68f, h * 0.64f),
                    strokeWidth = w * 0.01f
                )
                drawLine(
                    color = Color(0x33B88A22),
                    start = Offset(w * 0.57f, h * 0.67f),
                    end = Offset(w * 0.70f, h * 0.68f),
                    strokeWidth = w * 0.01f
                )
            }
        }

        if (showArabicText) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "أَذْكَارِي",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = Color(0xFFFFF3D0)
                )
            )
            Text(
                text = "تذكّر قلبك.. ورتّل يومك",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GoldLight,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

/**
 * Beautiful Atmospheric Islamic Background matching the user's second image:
 * - Deep emerald night sky with glowing horizon
 * - Warm glowing lantern (Fanous) on the lower left
 * - Silhouette of mosque minarets & domes
 * - Golden crescent moon & night sky stars
 * - Elegant Arabic typography banner "تذكّر قلبك.. ورتّل يومك"
 */
@Composable
fun AdhkariHeroBackgroundCard(
    modifier: Modifier = Modifier,
    onExploreClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .testTag("adhkari_hero_background_card"),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF031E17), // Deep night emerald
                            Color(0xFF063327),
                            Color(0xFF0A4E3B),
                            Color(0xFF145E49),
                            Color(0xFF1A4537),
                            Color(0xFF041913)
                        )
                    )
                )
        ) {
            // Custom Canvas rendering mosque silhouette, glowing crescent, fanous, and stars
            Canvas(
                modifier = Modifier
                    .matchParentSize()
            ) {
                val w = size.width
                val h = size.height

                // 1. Distant glowing stars
                val stars = listOf(
                    Offset(w * 0.15f, h * 0.12f),
                    Offset(w * 0.28f, h * 0.08f),
                    Offset(w * 0.72f, h * 0.10f),
                    Offset(w * 0.88f, h * 0.18f),
                    Offset(w * 0.82f, h * 0.06f),
                    Offset(w * 0.40f, h * 0.15f),
                    Offset(w * 0.65f, h * 0.22f)
                )
                for (star in stars) {
                    drawCircle(Color(0xFFFFF8D6), radius = 2.5f, center = star)
                }

                // 2. Crescent Moon in upper right
                val moonCenter = Offset(w * 0.82f, h * 0.15f)
                val moonRadius = w * 0.07f
                val moonPath = Path().apply {
                    addOval(androidx.compose.ui.geometry.Rect(center = moonCenter, radius = moonRadius))
                }
                val moonCut = Path().apply {
                    addOval(
                        androidx.compose.ui.geometry.Rect(
                            center = Offset(moonCenter.x + moonRadius * 0.45f, moonCenter.y - moonRadius * 0.25f),
                            radius = moonRadius * 0.85f
                        )
                    )
                }
                val finalMoon = Path().apply {
                    op(moonPath, moonCut, androidx.compose.ui.graphics.PathOperation.Difference)
                }
                // Moon glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x66FFDE7A), Color.Transparent),
                        center = moonCenter,
                        radius = moonRadius * 2.2f
                    ),
                    radius = moonRadius * 2.2f,
                    center = moonCenter
                )
                drawPath(finalMoon, brush = Brush.linearGradient(colors = listOf(Color(0xFFFFFDE7), Color(0xFFFFD54F))))

                // 3. Mosque Domes and Minarets Silhouette across the lower horizon
                val horizonY = h * 0.88f

                // Central Grand Dome
                val mainDomeCenter = Offset(w * 0.52f, horizonY)
                val mainDomeRadius = w * 0.16f
                drawCircle(
                    color = Color(0xFF042018),
                    radius = mainDomeRadius,
                    center = mainDomeCenter
                )
                // Left dome
                drawCircle(
                    color = Color(0xFF031A13),
                    radius = w * 0.11f,
                    center = Offset(w * 0.28f, horizonY + h * 0.02f)
                )
                // Right dome
                drawCircle(
                    color = Color(0xFF031A13),
                    radius = w * 0.11f,
                    center = Offset(w * 0.74f, horizonY + h * 0.02f)
                )

                // Minarets
                drawRect(
                    color = Color(0xFF031A13),
                    topLeft = Offset(w * 0.84f, h * 0.60f),
                    size = Size(w * 0.045f, h * 0.35f)
                )
                drawRect(
                    color = Color(0xFF042018),
                    topLeft = Offset(w * 0.18f, h * 0.64f),
                    size = Size(w * 0.04f, h * 0.30f)
                )

                // 4. Lantern (Fanous) with Warm Radiant Glow on Lower Left
                val fanousX = w * 0.14f
                val fanousY = h * 0.78f
                val fanousWidth = w * 0.14f
                val fanousHeight = h * 0.18f

                // Warm Amber Light Radial Glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x99FFA000),
                            Color(0x55FFC107),
                            Color(0x22FFE082),
                            Color.Transparent
                        ),
                        center = Offset(fanousX + fanousWidth / 2, fanousY + fanousHeight / 2),
                        radius = fanousWidth * 2.2f
                    ),
                    radius = fanousWidth * 2.2f,
                    center = Offset(fanousX + fanousWidth / 2, fanousY + fanousHeight / 2)
                )

                // Lantern Glass & Candle inside
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFD54F), Color(0xFFFF8F00))
                    ),
                    topLeft = Offset(fanousX + fanousWidth * 0.15f, fanousY + fanousHeight * 0.25f),
                    size = Size(fanousWidth * 0.7f, fanousHeight * 0.55f),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // Lantern Metal Frame (Top dome & base)
                val lanternCap = Path().apply {
                    moveTo(fanousX, fanousY + fanousHeight * 0.25f)
                    cubicTo(
                        fanousX + fanousWidth * 0.2f, fanousY,
                        fanousX + fanousWidth * 0.8f, fanousY,
                        fanousX + fanousWidth, fanousY + fanousHeight * 0.25f
                    )
                    close()
                }
                drawPath(lanternCap, color = Color(0xFF3E2723))
                // Top ring
                drawCircle(
                    color = Color(0xFFC89B3C),
                    radius = fanousWidth * 0.12f,
                    center = Offset(fanousX + fanousWidth / 2, fanousY - fanousHeight * 0.05f),
                    style = Stroke(width = 3f)
                )

                // Bottom base
                drawRect(
                    color = Color(0xFF3E2723),
                    topLeft = Offset(fanousX, fanousY + fanousHeight * 0.8f),
                    size = Size(fanousWidth, fanousHeight * 0.12f)
                )
            }

            // Foreground Content Layout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Emblem Logo
                AdhkariEmblemLogo(
                    size = 92.dp,
                    showArabicText = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Brand Title Typography matching the image
                Text(
                    text = "أَذْكَارِي",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFF3D0),
                        letterSpacing = 1.5.sp
                    ),
                    textAlign = TextAlign.Center
                )

                // Golden flourish line
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(1.dp)
                            .background(Brush.horizontalGradient(listOf(Color.Transparent, GoldPrimary)))
                    )
                    Text(
                        text = " ❖ ",
                        color = GoldLight,
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(1.dp)
                            .background(Brush.horizontalGradient(listOf(GoldPrimary, Color.Transparent)))
                    )
                }

                // Slogan from the user's background artwork
                Text(
                    text = "تذكّر قلبك.. ورتّل يومك",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFFE0F2E9),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Daily Hadith quote pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x33000000),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33E5C158))
                ) {
                    Text(
                        text = "«أَلا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ»",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = GoldLight,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
