# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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

# Preserve application classes
-keep class com.example.qld.** { *; }

# Preserve models
-keep class com.example.qld.models.** { *; }

# Preserve database classes
-keep class com.example.qld.database.** { *; }

# Preserve activities
-keep class com.example.qld.activities.** { *; }

# Preserve adapters
-keep class com.example.qld.adapters.** { *; }

# Preserve utilities
-keep class com.example.qld.utils.** { *; }

# Keep custom Application class
-keep class com.example.qld.MyApplication { *; }

# For serialization
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# For OpenCSV library
-keep class com.opencsv.** { *; }
-keep interface com.opencsv.** { *; }

# For iText7 library
-keep class com.itextpdf.** { *; }
-keep interface com.itextpdf.** { *; }

# Keep annotations
-keepattributes *Annotation*

# For RecyclerView and other support library components
-keep class androidx.recyclerview.widget.RecyclerView { *; }
-keep class androidx.appcompat.widget.Toolbar { *; }
-keep class com.google.android.material.** { *; }

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile