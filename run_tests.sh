#!/bin/bash
[ "$TRAVIS_SCALA_VERSION" == "2.10.5" ] && sbt ++$TRAVIS_SCALA_VERSION test
