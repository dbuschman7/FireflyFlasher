import android.Keys._

android.Plugin.androidBuild  

name := "firefly-flasher"
 
scalaVersion := "2.11.5"
 
proguardScala in Android := true

proguardOptions in Android ++= Seq(
  "-dontobfuscate",
  "-dontoptimize",
  "-ignorewarnings",
  "-keepattributes Signature",
  "-dontwarn scala.collection.**", // required from Scala 2.11.3
  "-dontwarn scala.collection.mutable.**", // required from Scala 2.11.0
  "-dontwarn javax.naming.InitialContext",
  "-dontnote org.slf4j.**",
  "-keep class scala.Dynamic",
  "-keep class myapp.**",
  "-keep class scala.collection.Seq.**",
  "-keep public class org.sqldroid.**",
  "-keep class scala.concurrent.Future$.**",
  "-keep class scala.slick.lifted.**",
  "-keep class scala.slick.jdbc.meta.**",
  "-keep class scala.slick.driver.JdbcProfile$Implicits",
  "-keep class org.scaloid.**",
  "-keep class org.scaloid.common.Logger",
  "-keep class scala.concurrent.Future",
  "-keep class macroid.**",
  "-keep class com.squareup.okhttp.**",
  "-keep class com.splunk.** { *; }"
)

proguardCache in Android ++= Seq(
  "com.typesafe.slick",
  "org.scaloid",
  "org.macroid",
  "com.google",
  "io.spray"
)

libraryDependencies += "org.scaloid" %% "scaloid" % "4.0"

scalacOptions in Compile += "-feature"
 
run <<= run in Android
 
install <<= install in Android

antLayoutDetector in Android := ()

EclipseKeys.withSource := true

