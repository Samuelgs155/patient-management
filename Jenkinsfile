pipeline {
  agent any

  environment {
    DOCKERHUB_USER = "TU_USUARIO_DOCKERHUB"
    BACKEND_IMAGE  = "${DOCKERHUB_USER}/patient-backend"
    FRONTEND_IMAGE = "${DOCKERHUB_USER}/patient-frontend"
    SONAR_HOST_URL = "http://sonarqube:9000"
    SONAR_PROJECT_KEY = "patient-management"
  }

  options {
    timestamps()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend - Tests') {
      steps {
        dir('backend') {
          sh 'mvn -B clean test'
        }
      }
    }

    stage('SonarQube - Analyze (Backend)') {
      steps {
        dir('backend') {
          withSonarQubeEnv('SonarQube') {
            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
              sh """
                mvn -B sonar:sonar \
                  -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                  -Dsonar.host.url=${SONAR_HOST_URL} \
                  -Dsonar.login=$SONAR_TOKEN
              """
            }
          }
        }
      }
    }

    // Recomendado si configuras webhook Sonar->Jenkins (lo explico abajo)
    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t ${BACKEND_IMAGE}:latest ./backend"
        sh "docker build -t ${FRONTEND_IMAGE}:latest ./frontend/patients-frontend"
      }
    }

    stage('Docker Push') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
          sh """
            echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
            docker push ${BACKEND_IMAGE}:latest
            docker push ${FRONTEND_IMAGE}:latest
          """
        }
      }
    }

    stage('Deploy (docker compose)') {
      steps {
        // despliegue en el mismo host donde corre Jenkins
        // Asegúrate de que docker-compose.prod.yml existe en el workspace o está en el repo
        sh "docker compose -f docker-compose.prod.yml up -d"
      }
    }
  }

  post {
    always {
      sh 'docker logout || true'
    }
  }
}
