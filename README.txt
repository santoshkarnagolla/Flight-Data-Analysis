Instance Informations:

1. Master:

Instance Name: Ubuntu Server 18.04 LTS (HVM), SSD Volume Type
Family: EBS General purpose
Type: t2.xlarge
vCPUs: 4
Memory(GiB): 16
Instance Storage(GB): EBS only

2. Slaves:

Instance Name: Ubuntu Server 18.04 LTS (HVM), SSD Volume Type
Family: EBS General purpose
Type: t2.micro
vCPUs: 1
Memory(GiB): 11
Instance Storage(GB): EBS only

The Environment of the master instance & the slave instances:

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

export HADOOP_HOME=/home/ubuntu/hadoop-2.9.2
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export YARN_HOME=$HADOOP_HOME

export M2_HOME=/home/ubuntu/apache-maven-3.5.3
export PATH=$PATH:$M2_HOME/bin

export OOZIE_HOME=/home/ubuntu/oozie-4.3.1
export OOZIE_CONFIG=$OOZIE_HOME/conf
export CLASSPATH=$CLASSPATH:$OOZIE_HOME/bin

Local: Windows Operation System
User use WinSCP and PuTTy to manage instances and files

NOTE:
To change the input file, please change the inputFilePath in the job.properties file which we provided. Please refer the format in the job.properties file
version of hadoop: 2.9.2
version of oozie: 4.3.1

COMMANDS:

1) To upload the following files to the directory /usr/local/hadoop-2.9.2 (just pull the java files to the master instance's Sftp area of WinSCP)
CancellationsMapper.java
CancellationsReducer.java
OnScheduleMapper.java
OnScheduleReducer.java
TaxiTimeMapper.java
TaxiTimeReducer.java
job.properties
workflow.xml

2) To create a folder named input in /usr/local/hadoop-2.9.2:-

$ mkdir input

3) To upload the entire data set to the input directory /usr/local/hadoop/input

4) To start Hadoop Cluster & historyserver:-

$ cd $HADOOP_HOME
$ ~/hadoop-2.9.2/bin/hdfs namenode -format
$ ~/hadoop-2.9.2/sbin/start-dfs.sh
$ ~/hadoop-2.9.2/sbin/start-yarn.sh
$ ~/hadoop-2.9.2/sbin/mr-jobhistory-daemon.sh start historyserver

5) To cross-validate the daemons:-

$ jps

6) To upload input file to HDFS:-

$ ~/hadoop-2.9.2/bin/hdfs dfs -mkdir -p input
$ ~/hadoop-2.9.2/bin/hdfs dfs -put input/* input

7) To upload oozie's share file to HDFS:-

$ cd $OOZIE_HOME
$ tar -xzvf oozie-sharelib-4.3.1.tar.gz #change the sharelib name to your local sharelib name
$ cd $HADOOP_HOME
$ ~/hadoop-2.9.2/bin/hdfs dfs -put $OOZIE_HOME/share share

8) To upload workflow.xml to HDFS:-

$ ~/hadoop-2.9.2/bin/hdfs dfs -mkdir BigDataProject
$ ~/hadoop-2.9.2/bin/hdfs dfs -put workflow.xml BigDataProject

9) To compile the java files and make a jar file and upload the jar file to HDFS BigDataProject/lib:-

$ javac -cp  $(hadoop classpath)  *.java
$ jar cf BigDataProject.jar *.class
$ ~/hadoop-2.9.2/bin/hdfs dfs -mkdir BigDataProject/lib
$ ~/hadoop-2.9.2/bin/hdfs dfs -put BigDataProject.jar BigDataProject/lib

10) To initialize the database of oozie:-

$ $OOZIE_HOME/bin/ooziedb.sh create -sqlfile oozie.sql -run

11) To start oozie:-

$ $OOZIE_HOME/bin/oozied.sh start

12) To check the status of oozie, if shows System mode: NORMAL, do next step:-

$ $OOZIE_HOME/bin/oozie admin -oozie http://localhost:11000/oozie -status

13) To run the program:-

$ ~/oozie-4.3.1/bin/oozie job -oozie http://localhost:11000/oozie -config input/job.properties -run

14). After submitting job, oozie will give you a job ID. You can use this Job ID to track your oozie job execution:

$ ~/oozie-4.3.1/bin/oozie job -oozie http://localhost:11000/oozie -info <Job ID>

15). To get results:-

$ ~/hadoop-2.9.2/bin/hdfs dfs -get BigDataProject/output output

