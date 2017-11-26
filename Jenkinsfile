pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './gradlew clean assembleDebug'
      }
    }

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: '**/*.apk', fingerprint: true
      }
    }

    stage('UI Tests') {
      steps {
        sh './gradlew -Pversion=24 prepareEmulator'
        sh './gradlew spoon'
      }
      post {
        always {
          publishHTML(target: [
                  allowMissing         : false,
                  alwaysLinkToLastBuild: false,
                  keepAll              : true,
                  reportDir            : 'app/build/spoon-output/debug',
                  reportFiles          : 'index.html',
                  reportName           : "Spoon Report"
          ])
        }
      }
    }
  }
}