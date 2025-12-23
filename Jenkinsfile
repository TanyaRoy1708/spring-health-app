pipeline {
    agent { label 'docker-agent' }

    environment {
        IMAGE_NAME      = "tan1708/spring-app"
        APP_VERSION     = "${BUILD_NUMBER}"
        CONTAINER_NAME  = "spring-app"
        HOST_PORT       = "8081"
        CONTAINER_PORT  = "8080"  // fixed typo
    }

    options {
        timestamps()
    }

    stages {

        stage('Check-out') {
            steps {
                git url: 'https://github.com/TanyaRoy1708/spring-health-app.git', branch: "main"
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
                    docker stop $CONTAINER_NAME || true
                    docker rm $CONTAINER_NAME || true
                    docker run -d \
                        --restart unless-stopped \
                        -e APP_VERSION=$APP_VERSION \
                        -p $HOST_PORT:$CONTAINER_PORT \
                        --name $CONTAINER_NAME \
                        $IMAGE_NAME:$APP_VERSION
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    sleep 10
                    curl -f http://localhost:$HOST_PORT/health
                '''
            }
        }
    }

    post {
        always {
            sh 'docker image prune -f || true'
        }
        success {
            echo "Deployment successful: $IMAGE_NAME:$APP_VERSION"
        }
        failure {
            echo "Deployment failed"
        }
    }
}
