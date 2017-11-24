node {
  stage "Checkout"
  checkout scm

  stage "Build"
  sh './gradlew clean assembleDebug'

  stage "Archive"
  archiveArtifacts artifacts: '**/*.apk', fingerprint: true
}
