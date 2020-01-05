# Serverless data processing pipelines

SAAF is a programming framework that allows for tracing FaaS function server infrastructure for code deployments. This framework includes functions to enable tracing code containers and hosts (VMs) created by FaaS platform providers for hosting FaaS functions. This information can help verify the state of infrastructure (COLD vs. WARM) to understand performance results, and help preserve infrastructure for better FaaS performance.

TLQ(Transform,Load and Query) Pipeline:

Service #1 – Extract and Transform

  -Generating columns “Order Processing Time” and  “Gross Margin” column
  -Replacing the values of “Order Priority” column
  -Removing duplicate records in the “Order ID” column.
  
Service #2 – Load 

  -Connect to S3
  -Collect the transformed file
  -Connect to RDS Aurora
  -Check if the table exists
  -Load the database 
  -Primary key Order_ID
  
Service #3 – Query 

  -Connect to the RDS database
  -Query using filters and aggregators 
