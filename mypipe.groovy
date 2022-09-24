pipeline {
     
    agent any
    
    tools{
  maven 'mvn'
    }

    stages {

        stage('Checkout') { 
            steps {
                 git 'https://github.com/Mrunal-12345/studentapp-ui.git'
                 echo 'The code is pulled Sucessfully'
            }
        }
    
        stage('Build Code') { 
            steps {
                 sh 'mvn clean package'
                 echo 'The Source Code is Build Sucessfully'
            }
        }

        stage('Unit Test') { 
            steps {
                 sh 'mvn --batch-mode -Dmaven.test.failure.ignore=true test'
                 echo 'The Unit test is Sucessfull'
            }
        }
        
        stage('Code Coverage') { 
            steps {
                   jacoco exclusionPattern: '**/*Test*.class', inclusionPattern: '**/*.class', sourceExclusionPattern: 'generated/**/*.java'
                echo 'The Code coverage is done'
            }
        }
     /*  
        stage('Sonarqube Test') { 
            steps {
            withSonarQubeEnv('sonar') {
                 sh '''mvn sonar:sonar'''         }
                 echo 'The Application passes All the Quality Gates'
                
            }    
        } 
        
        stage('Artifactory Upload to Nexus') {
            steps {
                nexusArtifactUploader artifacts: [[artifactId: 'studentapp',
                classifier: '',
                file: 'target/studentapp-2.2.war',
                type: 'war']],
                credentialsId: 'nexus',
                groupId: 'com.jdevs',
                nexusUrl: '18.228.6.158',
                nexusVersion: 'nexus3',
                protocol: 'http',
                repository: 'http://18.228.6.158:8081/repository/munnaraw/',
                version: '2.2'
            }
        }
        */
        
        stage('Ansible playbook Pull') { 
            agent {
                label 'ans'
            }
            steps {
                 git branch: 'main', url: 'https://github.com/Mrunal-12345/ansiblerepo.git'
                 echo 'The Ansible Code is Pulled Sucessfully'
            }
        }
        
        stage('Artifactory Pull on ansible') { 
            agent {
                label 'ans'
            }
            steps {
                withCredentials([usernameColonPassword(credentialsId: 'nexus', variable: 'nexusone')]) {
                sh 'wget "http://18.231.172.200:8081/repository/munna-raw/studentapp-2.2.war"'
     }
                 echo 'The Artifact is Pulled Sucessfully'
            }
        }
        
        stage('Ansible Tomcat Deployment') { 
            agent {
                label 'ans'
            }
            steps {
               ansiblePlaybook credentialsId: 'tom-ans', disableHostKeyChecking: true, installation: 'ansible', inventory: 'hosts', playbook: 'pb2.yaml' 
               echo 'Tomcat Deployment is done'
                
            }
        }
        
        
        stage('Dockerfile Pull') { 
            agent {
                label 'doc'
            }
            steps {
                 git branch: 'main', url: 'https://github.com/Mrunal-12345/dockerrepo.git'
                 echo 'The Docker Code is Pulled Sucessfully'
            }
        }
        
        stage('Artifactory Pull on docker') { 
            agent {
                label 'doc'
            }
            steps {
                withCredentials([usernameColonPassword(credentialsId: 'nexus', variable: 'nexusone')]) {
                sh 'wget "http://18.231.172.200:8081/repository/munna-raw/studentapp-2.2.war"'
     }
                 echo 'The Artifact is Pulled Sucessfully'
            }
        }
        
    }
}
