#!/bin/bash


prj_dir=/home/sponge/IdeaProjects/flume-interceptor
export FLUME_HOME=/home/sponge/app/apache-flume-1.5.2-bin

cp ${prj_dir}/target/flume-interceptor-1.0-SNAPSHOT.jar $FLUME_HOME/lib

${FLUME_HOME}/bin/flume-ng agent --conf ${FLUME_HOME}/conf \
--conf-file ${prj_dir}/src/main/conf/example-interceptors-customer.conf \
--name a1 -Dflume.root.logger=DEBUG,console
