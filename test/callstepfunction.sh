#!/bin/bash
calc(){ awk "BEGIN { print "$*" }"; }
for i in {1..2};
do
  start=`date +%s` 
  json={"\"bucketname\"":"\"termproject-testing\"","\"filename\"":"\"testdataset.csv\""}
  smarn="arn:aws:states:us-east-1:140196509819:stateMachine:term_project_state_machine"
  exearn=$(aws stepfunctions start-execution --state-machine-arn $smarn --input $json | jq -r ".executionArn")

  # poll output
  output="RUNNING"
  while [ "$output" == "RUNNING" ]
  do  
    # echo "Status check call..."    
    alloutput=$(aws stepfunctions describe-execution --execution-arn $exearn)  
    output=$(echo $alloutput | jq -r ".status") 
    # echo "Status check=$output"
  done
  echo ""
  aws stepfunctions describe-execution --execution-arn $exearn | jq -r ".output" | jq
  end=`date +%s`
  runtime=$((end-start))
  echo $runtime >> step_analysis_freeze.txt
  sum=`(expr $sum + $runtime)`
  echo "Completed Iteration $i"
done
echo "Average for 100 runs : " >> step_analysis_freeze.txt
average=`calc $sum/100`
echo $average >> step_analysis_freeze.txt
echo "Throughput for 100000 rows : " >> step_analysis_freeze.txt
throughput=`calc 100000/$average`
echo $throughput >> step_analysis_freeze.txt

