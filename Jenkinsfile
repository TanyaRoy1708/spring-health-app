pipeline {
    agent { label 'docker-agent' }

    environment {
        IMAGE_NAME = "tan1708/spring-app"
        APP_VERSION = "${BUILD_NUMBER}"
    }

    stages {

        stage('Check-out') {
            steps {
                git url:'https://github.com/TanyaRoy1708/spring-health-app.git', branch:"main"
            }
        }


        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$APP_VERSION .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                  credentialsId: 'dockerhub-creds',
                  usernameVariable: 'DOCKER_USER',
                  passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    docker login -u $DOCKER_USER -p $DOCKER_PASS
                    docker push $IMAGE_NAME:$APP_VERSION
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker stop spring-app || true
                docker rm spring-app || true
                docker run -d \
                  -e APP_VERSION=$APP_VERSION \
                  -p 8080:8080 \
                  --name spring-app \
                  $IMAGE_NAME:$APP_VERSION
                '''
            }
        }
    }
}

