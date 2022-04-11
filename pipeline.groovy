pipeline {
    agent any 
    stages {
        stage('pull') { 
            steps {
                 git credentialsId: '07deea9e-9d03-490b-8745-1cc1192efdc6', url: 'https://github.com/Mrunal-12345/spring-boot-war-example.git'
            }
        }
        stage('Build') { 
            steps {
                 sh "mvn clean package"
            }
        }
        stage('Test') { 
            steps {
                echo " yes kaila dublu "
            }
        }
    }
}
