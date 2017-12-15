pipeline {
  agent any
  environment {
    GCLOUD_PROJECT = 'mymovies-7e138'
    GCLOUD_KEY_LOCATION = "${env.JENKINS_HOME}/gcloudkey.json"
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

    stage('UI Smoke Tests') {
      steps {
        sh 'gcloud config set project $GCLOUD_PROJECT'
        sh './ci/run-ui-tests.sh'
      }
    }
  }
}