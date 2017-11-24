node {
  stage "Environment"
  sh 'env'

  stage "Checkout"
  checkout scm

  stage "Build"
  sh './gradlew clean assembleDebug'
  archiveArtifacts artifacts: '**/*.apk', fingerprint: true
}
