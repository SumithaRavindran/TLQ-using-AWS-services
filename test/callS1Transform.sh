#!/bin/bash
json={"\"bucketname\"":"\"termproject-testing\"","\"filename\"":"\"testdataset25k.csv\""}

#echo " "
echo "Invoking Extract and Transform Services as a lambda function using AWS CLI"
time output=`aws lambda invoke --cli-connect-timeout 600 --cli-read-timeout 600 --invocation-type RequestResponse --function-name S1_Transform --region us-east-1 --payload $json /dev/stdout; echo`
echo $output | jq
