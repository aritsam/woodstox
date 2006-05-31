#!/bin/sh

# Need to use more than 16 megs, as files may be over 1 meg...
# Note: only one of wstx jars should be there...

java -XX:CompileThreshold=500 -Xmx32m -server\
 -cp lib/stax-api-1.0.1.jar:\
lib/wstx-1.0.jar:\
lib/wstx.jar:\
lib/wstx-asl-2.9.2.jar:\
lib/wstx-asl-2.9.3.jar:\
lib/wstx-asl-2.9.4.jar:\
lib/wstx-asl-2.9.4a.jar:\
lib/wstx-asl-2.9.4b.jar:\
lib/stax-ri-1.2.0.jar:\
lib/sjsxp-1.0.jar:\
lib/wool-asl-0.5.jar:\
lib/xercesImpl.jar:lib/xml-apis.jar:\
lib/jdom.jar:\
build/classes $*
