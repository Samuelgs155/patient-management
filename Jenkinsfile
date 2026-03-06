pipeline {
  agent any

  environment {
    DOCKERHUB_USER = "TU_USUARIO_DOCKERHUB"
    BACKEND_IMAGE  = "${DOCKERHUB_USER}/patient-backend"
    FRONTEND_IMAGE = "${DOCKERHUB_USER}/patient-frontend"
    SONAR_HOST_URL = "http://sonarqube:9000"
    SONAR_PROJECT_KEY = "patient-management"
  }

  stages {
    stage('Debug - find pom.xml') {
      steps {
        sh '''
          echo "=== LIST ROOT ==="
          ls -la
          echo "=== FIND pom.xml ==="
          find . -maxdepth 5 -name pom.xml -print
          echo "=== LIST backend ==="
          ls -la backend || true
          echo "=== TREE backend (2 niveles) ==="
          find backend -maxdepth 3 -type d -print || true
        '''
      }
    }

     stage('Backend - Tests') {
      steps {
        sh '''
          docker run --rm \
            -v "$PWD/backend/patients-backend":/app \
            -w /app \
            maven:3.9-eclipse-temurin-17 \
            mvn -B clean test
        '''
      }
    }

    stage('SonarQube - Analyze (Backend)') {
      steps {
        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
          sh """
            docker run --rm \
              -v "\$PWD/backend/patients-backend":/app \
              -w /app \
              maven:3.9-eclipse-temurin-17 \
              mvn -B sonar:sonar \
                -Dsonar.projectKey=patient-management \
                -Dsonar.host.url=http://sonarqube:9000 \
                -Dsonar.login=\$SONAR_TOKEN
          """
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

    stage('Deploy') {
      steps {
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




