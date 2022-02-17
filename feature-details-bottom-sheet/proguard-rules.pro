# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.kts.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.TorrentDescription {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIComponent {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIColumnModel {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIDividerModel {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIFontWeight {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIHiddenContentModel {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIImageModel {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUILinkModel {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUIRowModel {*;}
-keep class com.automotivecodelab.featuredetailsbottomsheet.domain.models.SDUITextModel {*;}