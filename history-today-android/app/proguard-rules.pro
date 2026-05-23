-keep class com.historytoday.** { *; }
-keep class androidx.compose.** { *; }
-keep class androidx.navigation.** { *; }
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class *
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keep class androidx.room.** { *; }
-keep class com.historytoday.data.local.** { *; }
-keep class com.historytoday.domain.model.** { *; }
