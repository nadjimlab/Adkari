package com.example.data.model

enum class AzkarCategory(
    val id: String,
    val titleArabic: String,
    val subtitleArabic: String,
    val iconName: String,
    val recommendedTime: String
) {
    MORNING(
        id = "morning",
        titleArabic = "أذكار الصباح",
        subtitleArabic = "تبدأ من بعد صلاة الفجر إلى طلوع الشمس",
        iconName = "wb_sunny",
        recommendedTime = "بعد الفجر"
    ),
    EVENING(
        id = "evening",
        titleArabic = "أذكار المساء",
        subtitleArabic = "تبدأ من بعد صلاة العصر إلى غروب الشمس",
        iconName = "nights_stay",
        recommendedTime = "بعد العصر"
    ),
    AFTER_PRAYER(
        id = "after_prayer",
        titleArabic = "أذكار بعد الصلاة",
        subtitleArabic = "الأذكار المشروعة دبر كل صلاة مكتوبة",
        iconName = "mosque",
        recommendedTime = "دبر كل فريضة"
    ),
    SLEEP(
        id = "sleep",
        titleArabic = "أذكار النوم",
        subtitleArabic = "أذكار وأدعية التحصين قبل النوم",
        iconName = "bedtime",
        recommendedTime = "قبل النوم"
    ),
    WAKE_UP(
        id = "wake_up",
        titleArabic = "أذكار الاستيقاظ",
        subtitleArabic = "الحمد لله الذي أحيانا بعد ما أماتنا",
        iconName = "alarm",
        recommendedTime = "عند الاستيقاظ"
    ),
    SELECTED_DUAS(
        id = "selected_duas",
        titleArabic = "أدعية مختارة",
        subtitleArabic = "أدعية قرآنية ونبوية جامعة للخير",
        iconName = "menu_book",
        recommendedTime = "في كل وقت"
    ),
    TASBIH(
        id = "tasbih",
        titleArabic = "تسبيح إلكتروني",
        subtitleArabic = "مسبحة رقمية مع عداد الأذكار المأثورة",
        iconName = "fingerprint",
        recommendedTime = "أوقات الفراغ"
    )
}
