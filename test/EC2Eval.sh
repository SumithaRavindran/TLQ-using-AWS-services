#!/bin/bash
calc(){ awk "BEGIN { print "$*" }"; }
for i in {1..100};
do
  echo $i
  start=`date +%s`
  json={"\"bucketname\"":"\"termproject-testing\"","\"filename\"":"\"testdataset.csv\""}
  echo "Invoking Transform Service as a Lambda Function using AWS CLI"
  output=`aws lambda invoke --cli-connect-timeout 600 --cli-read-timeout 600 --invocation-type RequestResponse --function-name S1_Transform --region us-east-1 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`
  json={"\"bucketname\"":"\"termproject-testing\"","\"filename\"":"\"result.csv\""}
  echo "Invoking Load Service as a Lambda Function using AWS CLI"
  output=`aws lambda invoke --cli-connect-timeout 600 --cli-read-timeout 600 --invocation-type RequestResponse --function-name S2_Load --region us-east-1 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`
  echo "Invoking Query Service as a Lambda Function using AWS CLI"
  output=`aws lambda invoke --invocation-type RequestResponse --function-name S3_Query --region us-east-1 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`
  echo "Done"
  end=`date +%s`
  runtime=$((end-start))
  echo $runtime >> EC2_65000r_100e1_data_analysis.txt
  sum=`(expr $sum + $runtime)`
done
echo "Average for 100 runs: " >> EC2_65000r_100e1_data_analysis.txt
average=`calc $sum/100`
echo $average >> EC2_65000r_100e1_data_analysis.txt
echo "Throughput for 65000 rows:" >> EC2_65000r_100e1_data_analysis.txt
throughput=`calc 65000/$average`
echo $throughput >> EC2_65000r_100e1_data_analysis.txt
