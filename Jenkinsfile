node {
  stage "Checkout"
  checkout scm

  stage "Build"
  //sh './gradlew clean assembleDebug'

  stage "Archive"
  //archiveArtifacts artifacts: '**/*.apk', fingerprint: true

  stage "UI Tests"
  sh '$ANDROID_HOME/tools/emulator -list-avds'
  sh '$ANDROID_HOME/tools/emulator @CI_x86_android-26 -no-boot-anim -no-window -skin 768x1280 &'
  sh './gradlew spoon'
  publishHTML (target: [
          allowMissing: false,
          alwaysLinkToLastBuild: false,
          keepAll: true,
          reportDir: 'app/build/spoon-output/debug',
          reportFiles: 'index.html',
          reportName: "Spoon Report"
  ])
  //sh 'adb -s adb -s CI_x86_android-26 emu kill'
}
