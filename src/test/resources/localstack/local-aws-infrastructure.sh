#!/bin/sh

awslocal sqs create-queue --queue-name thduck-todo-sharing

awslocal ses verify-email-identity --email-address noreply@theduck.com
awslocal ses verify-email-identity --email-address info@theduck.com
awslocal ses verify-email-identity --email-address tom@stheduck.com
awslocal ses verify-email-identity --email-address bjoern@theduck.com
awslocal ses verify-email-identity --email-address philip@theduck.com

awslocal dynamodb create-table \
    --table-name local-todo-app-breadcrumb \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10 \

echo "Initialized."
