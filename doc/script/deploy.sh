#!/bin/bash
source /etc/profile
day=`date +%Y%m%d_%H%M%S`
BACKUP_HOME=/home/app/backup
APPS_HOME=/home/app/apps
TOMCAT_HOME=/home/app/tomcats/tomcat8280_caicaikan
DIST_HOME=/home/app/deploy
WAR_NAME=$1
DIST_NAME=caicaikan

if test -z $1
then
   echo "Require War Name parm"
   cd $DIST_HOME
   WAR_NAME=`ls $DIST_NAME*.war -t | head -n 1`
fi

echo "Begin to unpack $WAR_NAME and  dist $DIST_NAME"
echo

cd $APPS_HOME
tar zcvf $BACKUP_HOME/$DIST_NAME.$day.tar.gz $DIST_NAME/

echo sleep 10
sleep 10

cd $TOMCAT_HOME
./stop.sh

cd $APPS_HOME
rm $DIST_NAME -rf
mkdir $DIST_NAME
cp $DIST_HOME/$WAR_NAME $APPS_HOME/$DIST_NAME/$WAR_NAME -f
cd $DIST_NAME
jar xvf $WAR_NAME
rm $WAR_NAME -f

cd $TOMCAT_HOME
./start.sh


echo
echo "Finish dist $DIST_NAME"