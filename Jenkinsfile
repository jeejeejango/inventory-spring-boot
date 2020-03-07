pipeline {
  agent {
    label 'maven'
  }
  stages {
    stage('Build App') {
      steps {
        sh "mvn clean package -s src/main/config/settings.xml -Dmaven.test.skip=true"
      }
    }
    stage('Integration Test') {
      steps {
        sh "mvn verify -s src/main/config/settings.xml"
      }
    }
	stage('Code Analysis') {
	  steps {
		sh "mvn -s src/main/config/settings.xml sonar:sonar -Dsonar.host.url=http://sonarqube.devops.svc:9000 -Dmaven.test.skip=true"
	  }
	}
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject(env.DEV_PROJECT) {
              openshift.startBuild("inventory", "--from-file=inventory.jar").logs("-f")
            }
          }
        }
      }
    }
    stage('Deploy') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject(env.DEV_PROJECT) {
              dc = openshift.selector("dc", "inventory")
              dc.rollout().latest()
              timeout(10) {
                  dc.rollout().status()
              }
            }
          }
        }
      }
    }
    stage('Component Test') {
      steps {
        script {
          sh "curl -s -X POST http://inventory.${env.DEV_PROJECT}.svc:8080/api/inventory/329299"
        }
      }
    }
	stage('Promote to Stage') {
	  steps {
		timeout(time:15, unit:'MINUTES') {
			input message: "Approve Promotion to Stage?", ok: "Promote"
		}
		script {
		  openshift.withCluster() {
			openshift.tag("${env.DEV_PROJECT}/inventory:latest", "${env.STAGE_PROJECT}/inventory:latest")
		  }
		}
	  }
	}
	stage('Promote to Prod') {
	  steps {
		timeout(time:15, unit:'MINUTES') {
			input message: "Approve Promotion to Prod?", ok: "Promote"
		}
		script {
		  openshift.withCluster() {
			openshift.tag("${env.STAGE_PROJECT}/inventory:latest", "${env.PROD_PROJECT}/inventory:latest")
		  }
		}
	  }
	}
  }
}
