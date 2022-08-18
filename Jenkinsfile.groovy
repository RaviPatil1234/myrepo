pipeline {
    agent any
    environment {
        registry = "028054224963.dkr.ecr.us-west-2.amazonaws.com/flikshop-document-api"
    }

    stages {
        stage('Check Out') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/development']], extensions: [], userRemoteConfigs: [[credentialsId: '86febc6d-b93c-4384-a9fc-cd655f142b8f', url: 'git@github.com:flikshop/Flikshop.Services.Document.git']]])
            }
        }
        
        stage('Docker Build'){
            steps {
                script {
                    dockerImage = docker.build registry
                }
            }
        }
        
        stage('Publish To ECR') {
            steps {
                script {
                    sh'aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 028054224963.dkr.ecr.us-west-2.amazonaws.com'
                    sh'docker push 028054224963.dkr.ecr.us-west-2.amazonaws.com/flikshop-document-api:latest'
                    
                }
            }
        }
        
        
        
        stage('Cleaning Up') {
            steps {
                script {
                    sh'docker system prune --force --all'
                }
            }
        }
    }
}
