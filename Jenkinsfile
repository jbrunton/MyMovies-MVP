node {
  stage "Checkout"
  checkout scm

  stage "Build"
  //sh './gradlew clean assembleDebug'

  stage "Archive"
  //archiveArtifacts artifacts: '**/*.apk', fingerprint: true

  stage "UI Tests"
  sh '$ANDROID_HOME/tools/emulator @CI_x86_android-25 -no-boot-anim -skin 768x1280'
  sh 'adb -s adb -s CI_x86_android-25 emu kill'
  sh './gradlew spoon'
}
