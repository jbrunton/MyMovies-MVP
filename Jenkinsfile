node {
  stage "Checkout"
  checkout scm

  stage "Build"
  sh './gradlew clean assembleDebug'
  step([$class: 'ArtifactArchiver', artifacts: '**/apk/app-debug.apk', fingerprint: true])
}
