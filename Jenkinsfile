pipeline {
  agent any
  environment {
    GCLOUD_KEY_LOCATION = '/var/jenkins_home/gcloudkey.json'
  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew clean assembleDebug'
      }
      post {
        always {
          archiveArtifacts artifacts: '**/*.apk', fingerprint: true
        }
      }
    }

    stage('Unit Tests') {
      steps {
        sh './gradlew testDebug'
      }
      post {
        always {
          publishHTML(target: [
                  allowMissing         : false,
                  alwaysLinkToLastBuild: false,
                  keepAll              : true,
                  reportDir            : 'app/build/reports/tests/testDebugUnitTest',
                  reportFiles          : 'index.html',
                  reportName           : 'JUnit Report'
          ])
        }
      }
    }

    stage('UI Tests') {
      steps {
        sh './ci/run-testlab.sh'
      }
      post {
        always {
          publishHTML(target: [
                  allowMissing         : false,
                  alwaysLinkToLastBuild: false,
                  keepAll              : true,
                  reportDir            : 'testlab-artifacts-smoketest',
                  reportFiles          : '*',
                  reportName           : 'Smoke Test'
          ])
        }
      }
    }
  }
}