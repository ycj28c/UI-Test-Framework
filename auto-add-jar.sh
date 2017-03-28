#!/usr/bin/env bash
# https://github.com/yuanwhy/zdal/blob/master/auto-add-jar.sh
mvn install:install-file -Dfile=uitest-framework/lib/ojdbc6.jar -DgroupId=com.oracle \
    -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar -DgeneratePom=true