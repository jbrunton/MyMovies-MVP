node {
  stage "Checkout"
  checkout scm

  stage "Build"
  sh './gradlew clean assembleDebug'

  stage "Archive"
  archiveArtifacts artifacts: '**/*.apk', fingerprint: true

  stage "UI Tests"
  sh './gradlew spoon'
  publishHTML (target: [
          allowMissing: false,
          alwaysLinkToLastBuild: false,
          keepAll: true,
          reportDir: 'app/build/spoon-output/debug',
          reportFiles: 'index.html',
          reportName: "Spoon Report"
  ])
}
