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

    stage('Unit Tests') {
      steps {
        sh './gradlew testDebug'
      }
    }
  }
}