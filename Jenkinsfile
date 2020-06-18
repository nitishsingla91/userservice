pipeline {

  agent {

      label 'maven'

  }

  stages {

    stage('Build App') {

       steps
             {
              git branch: 'userservice', url: 'https://github.com/nitishsingla91/userservice.git'
              script {
                  def pom = readMavenPom file: 'pom.xml'
                  version = pom.version
              }
              sh "mvn clean install -DskipTests=true"
            }

    }
    
     stage('Test')
          {
            steps
            {
              sh "${mvnCmd} test -Dspring.profiles.active=gcp"
              //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
            }
          }
          

    stage('Create Image Builder') {

      when {

        expression {

          openshift.withCluster() {

            return !openshift.selector("bc", "devopspoc").exists();

          }

        }

      }

      steps {

        script {

          openshift.withCluster() {

            openshift.newBuild("--name=devopspoc", "--image-stream=redhat-openjdk18-openshift:1.1", "--binary")

          }

        }

      }

    }

    stage('Build Image') {

      steps {

        script {

          openshift.withCluster() {

            openshift.selector("bc", "devopspoc").startBuild("--from-file=target/user-service-0.0.1-SNAPSHOT.jar", "--wait")

          }

        }

      }

    }


    stage('Create DEV') {

      when {

        expression {

          openshift.withCluster() {

            return !openshift.selector('dc', 'devopspoc').exists()

          }

        }

      }

      steps {

        script {

          openshift.withCluster() {

            openshift.newApp("devopspoc:latest", "--name=devopspoc").narrow('svc').expose()

          }

        }

      }

    }

  stage('Deploy DEV') {
            steps {
              script {
                openshift.withCluster() {
                 
                    openshift.selector("dc", "devopspoc").rollout().latest()
                  
                }
              }
            }
          }

  }

}