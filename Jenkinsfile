pipeline {
  agent any
  environment {
    GCLOUD_PROJECT = 'mymovies-7e138'
    GCLOUD_KEY_LOCATION = '$JENKINS_HOME/gcloudkey.json'
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
        gcloud config set project $GCLOUD_PROJECT
        gcloud auth activate-service-account --key-file $GCLOUD_KEY_LOCATION

        echo 'Running UI smoke tests...'
        sh './ci/run-test-matrix.sh smoketest'

        if (env.BRANCH == 'master') {
          echo 'On master, running UI road tests...'
          sh './ci/run-test-matrix.sh roadtest'
        } else {
          echo 'Not on master, skipping UI road tests.'
        }
      }
    }
  }
}