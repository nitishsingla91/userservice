pipeline {
 agent {
    label 'maven'
  }
  
	/*
		All the variables are set here . This will change for every project
	*/

  environment {
     BRANCH_NAME = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
     CLUSTER_NAME = "https://api.ca-central-1.starter.openshift-online.com:6443"
	 PROJECT_DEV="devopspoc" 
	 GITLAB_PROJECT_PATH="https://gitlab.com/nsingla85/userservice.git"
	 OPENSHIFT_PROJECT_NAME="devopspoc"
	 JAR="user-service-0.0.1-SNAPSHOT.jar"
	 PORT='8110'
	
  }
	



stages {
     

		// update the running status to gitlab commit 
  
		stage('Build') {
			steps {
					 git branch: 'userservice', url: 'https://gitlab.com/nsingla85/userservice.git'
              script {
                  def pom = readMavenPom file: 'pom.xml'
                  version = pom.version
              }
              sh "mvn clean install -DskipTests=true"
					
			}
		}
	
		


		 stage('Create Image Builder') {
			  when {
				expression {
				  openshift.withCluster(CLUSTER_NAME) {
					  openshift.withProject( PROJECT_DEV ) {
							return (BRANCH_NAME == 'master' || BRANCH_NAME == 'development') && currentBuild.currentResult !='FAILURE' && !openshift.selector("bc",OPENSHIFT_PROJECT_NAME).exists();
							}
				  }
				}
			  }
			  steps {
				script {
				  openshift.withCluster(CLUSTER_NAME) {
					  openshift.withProject( PROJECT_DEV ) {
							openshift.newBuild("--name=${OPENSHIFT_PROJECT_NAME}", "--image-stream=redhat-openjdk18-openshift:1.1", "--binary")
							}
				  }
				}
			  }
		}

	
		stage('Build Image') {
	
			when{
				expression {
					return (BRANCH_NAME == 'master' || BRANCH_NAME == 'development') && currentBuild.currentResult !='FAILURE' ;
				}
		   }
          
        steps {
           echo "Pushing The JAR Into OpenShift OpenJDK-Container"
			echo "${currentBuild.currentResult}"
            script {
                openshift.withCluster( CLUSTER_NAME ) {
                    openshift.withProject( PROJECT_DEV ) {
                        openshift.selector("bc", OPENSHIFT_PROJECT_NAME).startBuild("--from-file=target/" + JAR,"--wait")
                    }
               }
			  
              }
          }
    	  
          post {
            success {
              archiveArtifacts artifacts: 'target/**.jar', fingerprint: true
            }
          }
        }
			
		stage('Tagging the Image') {
		
			when{
				expression {
					return (BRANCH_NAME == 'master' || BRANCH_NAME == 'development') && currentBuild.currentResult !='FAILURE' ;
				}
		    }
		  steps {
			script {
			  openshift.withCluster( CLUSTER_NAME ) {
			      openshift.withProject( PROJECT_DEV ) {
						openshift.tag(OPENSHIFT_PROJECT_NAME+":latest", OPENSHIFT_PROJECT_NAME+":dev")
					}
			  }
			}
		  }
		}
		
		 stage('Creating App in Openshift') {
			when{
				expression {
					return (BRANCH_NAME == 'master' || BRANCH_NAME == 'development') && currentBuild.currentResult !='FAILURE' &&  !openshift.selector("dc",OPENSHIFT_PROJECT_NAME).exists();
				}
		    }
		  steps {
			script {
			  openshift.withCluster( CLUSTER_NAME ) {
			      openshift.withProject( PROJECT_DEV ) {
						openshift.newApp(OPENSHIFT_PROJECT_NAME+":latest", "--name="+OPENSHIFT_PROJECT_NAME).narrow('svc').expose()
						
						
					}
			  }
			}
		  }
		}
		
		stage ('Verifying deployment'){
		
			 when {
				expression {
				  openshift.withCluster( CLUSTER_NAME ) {
					  openshift.withProject( PROJECT_DEV ) {
							return (BRANCH_NAME == 'master' || BRANCH_NAME == 'development') && currentBuild.currentResult !='FAILURE' && openshift.selector('dc', OPENSHIFT_PROJECT_NAME).exists() 
						}
				  }
				}
			  }
			  steps {
				script {
				  openshift.withCluster(CLUSTER_NAME) {
						openshift.withProject( PROJECT_DEV ){
							
							def latestDeploymentVersion = openshift.selector('dc',OPENSHIFT_PROJECT_NAME).object().status.latestVersion
							def rc = openshift.selector('rc', OPENSHIFT_PROJECT_NAME+"-${latestDeploymentVersion}")
							 timeout (time: 10, unit: 'MINUTES') {
								rc.untilEach(1){
									def rcMap = it.object()
									return (rcMap.status.replicas.equals(rcMap.status.readyReplicas))
								}
							 }
						}
					}
				  
				  
				  
				}
			  }
		
		}
	
    }

}
