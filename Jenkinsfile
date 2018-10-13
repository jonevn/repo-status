pipeline {
    
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    
    stages {
        stage('clean') {
           	steps {
               	sh 'mvn clean'
           	}
        }
        
        stage('compile') {
           	steps {
               	sh 'mvn compile test-compile'
           	}
        }
        
        stage('test') {
           	steps {
	         	sh 'mvn test'
	         	junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true      
	        }
        }

        
		stage('package') {
		   	steps {
   		   	   	sh 'mvn package' 
   		   	}
		}
		
		stage('deploy') {
		   	steps {
				fileOperations([fileCopyOperation(excludes: '', flattenFiles: true, includes: 'target/repo-status.jar', targetLocation: '/home/johan/bin/')])
   			}
		}
    }
}