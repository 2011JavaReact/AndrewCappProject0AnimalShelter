pipeline {
    agent any

    stages {
        stage('clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('deploy') {
            steps {
                sh 'cp /home/ec2-user/.jenkins/workspace/project0-animalshelter-backend/target/animalshelter.war /home/ec2-user/apache-tomcat-8.5.60/webapps/'
            }
        }
    }
}